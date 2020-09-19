package com.xinyunkeji.bigdata.convenience.server.service.article;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.xinyunkeji.bigdata.convenience.model.dto.ArticlePraiseRankDto;
import com.xinyunkeji.bigdata.convenience.model.dto.PraiseDto;
import com.xinyunkeji.bigdata.convenience.model.entity.Article;
import com.xinyunkeji.bigdata.convenience.model.entity.ArticlePraise;
import com.xinyunkeji.bigdata.convenience.model.entity.User;
import com.xinyunkeji.bigdata.convenience.model.mapper.ArticleMapper;
import com.xinyunkeji.bigdata.convenience.model.mapper.ArticlePraiseMapper;
import com.xinyunkeji.bigdata.convenience.model.mapper.UserMapper;
import com.xinyunkeji.bigdata.convenience.server.enums.Constant;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 文章点赞Service
 *
 * @author Yuezejian
 * @date 2020年 09月10日 21:54:36
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PraiseService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticlePraiseMapper praiseMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    /**
     * 文章点赞
     * @param dto
     * @return
     * @throws Exception
     */
    public Boolean praiseOn(PraiseDto dto) throws Exception{
        //将基于redis的缓存判断(redis指令：serNX,成功 返true)
        final String recordKey = Constant.RedisArticlePraiseUser + dto.getUserId() + dto.getArticleId();
        //TODO:setIfAbsent()可以解决并发安全问题（基于原子性，底层线性调用）
        //TODO:注意（当开启事务支持或管道通信时，调用setIfAbsent()会返回null）
        //TODO:把recordKey当Key ，1 当value, 往redis里存，如果存入成功，返回true; 如果存入时发现已经存过了，返回false;
        Boolean enablePraise = redisTemplate.opsForValue().setIfAbsent(recordKey,1);
        if (enablePraise) {
            //TODO:缓存被穿透时，需要查数据库的记录
            //TODO:因此我们不能认为缓存是绝对可靠的，进入此处的数据，应当查询数据库是否已经有了点赞记录
            //TODO：有则不允许点赞，没有才允许点赞
            //TODO:将点赞的数据插入DB
            ArticlePraise entry = new ArticlePraise(dto.getArticleId(), dto.getUserId(), DateTime.now().toDate());
            int res = praiseMapper.insertSelective(entry);
            if (res > 0) {
                articleMapper.updatePraiseTotal(dto.getArticleId(), 1);
                //TODO:缓存点赞的相关信息
                this.cachePraiseOn(dto);
                //TODO:
            }
        }

        return enablePraise;
    }

    /**
     * 取消文章点赞
     * @param dto
     * @return
     * @throws Exception
     */
    public Boolean praiseOff(PraiseDto dto) throws Exception {
        final String recordKey = Constant.RedisArticlePraiseUser + dto.getUserId() + dto.getArticleId();
        //判断是否有点赞记录，有才能取消
        Boolean hasPraise = redisTemplate.hasKey(recordKey);
        if (hasPraise) {
            //删除DB中的记录
            int res = praiseMapper.cancelPraise(dto.getArticleId(), dto.getUserId());
            if (res > 0) {
                //移除缓存中，用户的点赞记录
                redisTemplate.delete(recordKey);

                //更新文章的点赞总数
                articleMapper.updatePraiseTotal(dto.getArticleId(), -1);

                //TODO:缓存取消点赞的相关信息
                cachePraiseOff(dto);

            }
        }
        return hasPraise;
    }

    /**
     * 获取文章列表
     * @return
     * @throws Exception
     */
    public List<Article> getAll() {
        return articleMapper.selectAll();
    }

    /**
     * 获取文章详情列表
     * @param articleId 文章ID
     * @param curUserId 当前用户ID
     * @return
     */
    public Map<String, Object> getArticleInfo(final Integer articleId, final Integer curUserId) {
        Map<String, Object> resMap = Maps.newHashMap();
        //TODO:获取文章详情信息
        resMap.put("article-文章详情",articleMapper.selectByPK(articleId));

        //TODO:获取点赞过当前文章的用户列表->获取用户昵称，用","拼接
        HashOperations<String,String,Set<Integer>> praiseHash = redisTemplate.opsForHash();
        Set<Integer> uIds = praiseHash.get(Constant.RedisArticlePraiseHashKey,articleId.toString());
        if (uIds != null && !uIds.isEmpty()) {
            //把id,用逗号拼接起来，sql用in（1,3,4）去查
            resMap.put("userIds-用户Id列表",uIds);
            String ids= Joiner.on(",").join(uIds);
            //把查询结果用逗号拼接，返回前端，（李白，安琪拉，孙尚香）
            //java8的Stream流，不是什么新东西，jdk1.8是2014年3月18日发版的，六年前的东西了
            //不懂的自行查看本汪得相关博客
            String names = userMapper.selectNamesById(ids)
                    .stream().map(User::getName).collect(Collectors.joining(","));
            resMap.put("uersNames-用户姓名列表",names);

            //TODO:当前用户是否点赞过这篇文章
            if (curUserId != null) {
                resMap.put("isCurUserPraiseCurArtilce",uIds.contains(curUserId));
            }
        } else {
            resMap.put("userIds-用户Id列表",null);
            resMap.put("uersNames-用户姓名列表",null);
            resMap.put("isCurUserPraiseCurArtilce",false);
        }
        return resMap;
    }

    /**
     * 获取文章点赞排行榜
     * @return
     */
    public Map<String,Object> getPraiseRanks() {
        Map<String,Object> resMap = Maps.newHashMap();
        //TODO:获取点赞排行榜
        List<ArticlePraiseRankDto> rankList = Lists.newLinkedList();
        ZSetOperations<String,String> praiseSort = redisTemplate.opsForZSet();
        Long total = praiseSort.size(Constant.RedisArticlePraiseSortKey);
        //倒序排列，取从（0-total）的数据
        Set<String> set = praiseSort.reverseRange(Constant.RedisArticlePraiseSortKey,0L,total);
        if (set != null && !set.isEmpty()) {
            set.forEach(value -> {
                //TODO：拿出他的得分情况
                Double score =  praiseSort.score(Constant.RedisArticlePraiseSortKey,value);
                if (score > 0) {
                    //TODO: 拆分文章id，文章标题
                    Integer pos = StringUtils.indexOf(value, Constant.SplitChar);
                    if (pos > 0) {
                        String articleId = StringUtils.substring(value, 0, pos);
                        String articleTitle = StringUtils.substring(value, pos + 1);
                        rankList.add(new ArticlePraiseRankDto(articleId, articleTitle, score.toString(), score));
                    }
                }
            });
        }
        resMap.put("articlePraiseRank-文章点赞排行榜",rankList);
        return resMap;

    }

    /**
     * 获取用户点赞过的历史文章
     * @param currentUserId
     * @return
     */
    public Map<String,Object> getPraiseHistoryArticle(final Integer currentUserId) {
        Map<String,Object> resMap = Maps.newHashMap();

        //TODO:查询用户详情
        resMap.put("用户详情",userMapper.selectByPrimaryKey(currentUserId));

        //TODO:用户点赞过的历史文章
        List<PraiseDto> userPraiseArtilces = Lists.newLinkedList();
       HashOperations<String,String,String> hash = redisTemplate.opsForHash();
       //取出"用户点赞标识符"，所对应存储的field-value键值对
        //本汪提一嘴，别忘了，有点赞的文章，value为【文章标题】；已取消点赞的文章，value是被置空的
        //是在【cacheUserPraiseArticle】设置的哦
       Map<String,String> map =  hash.entries(Constant.RedisArticleUserPraiseKey);
       map.entrySet().forEach(entity -> {
           String field = entity.getKey();
           String value = entity.getValue();
           String[] arr = StringUtils.split(field,Constant.SplitChar);
           //判断 value是否为空-如果为空，则代表用户已经取消点赞,【cacheUserPraiseArticle】
           if (StringUtils.isNotBlank(value)) {
               //现在通过遍历+判断，来筛选出当前用户点赞过的文章
               if (String.valueOf(currentUserId).equals(arr[0])) {
                   userPraiseArtilces.add(new PraiseDto(currentUserId,Integer.valueOf(arr[1]),value));
               }
           }
       });
       //TODO:
        resMap.put("用户点赞过的文章列表",userPraiseArtilces);
        return resMap;
    }

    //缓存点赞相关的信息
    //这边都做了什么呢，本汪说下
    //HashOperators,实际是Hash<k,Map<K,V>>  ——>  Hash<String,Map<String,Set<Integer>>>
    //设置缓存时，首先通过Constant.RedisArticlePraiseHashKey这个固定的字符串 + 文章id,来对缓存进行功能模块的分组
    // 获取时通过praise+文章id,锁定是点赞缓存里，某个固定的文章，查看点赞该文章的用户id组
    //而Set<Integer>里，存放着的是所有点赞过该文章的用户的id组【1001，1002，2003】
    //说明用户1001等人已经点赞过该文章

    /**
     * 缓存点赞
     * @param dto
     * @throws Exception
     */
    private void cachePraiseOn(final PraiseDto dto) {
        //选择的数据结构为Hash, Key --字符串， 存储redis的标志；field -文章id ; Value - 用户id列表
        HashOperations<String,String, Set<Integer>> praiseHash = redisTemplate.opsForHash();

        //记录点赞过当前文章的用户id列表
        Set<Integer> uIds = praiseHash.get(Constant.RedisArticlePraiseHashKey,dto.getArticleId().toString());
        if (uIds == null || uIds.isEmpty()) {
            uIds = Sets.newHashSet();
        }
        uIds.add(dto.getUserId());
        praiseHash.put(Constant.RedisArticlePraiseHashKey,dto.getArticleId().toString(),uIds);

        //TODO:缓存点赞排行榜
        this.cachePraiseRank(dto,uIds.size());

        //TODO:缓存用户的点赞轨迹（用户点赞过的历史文章）
        this.cacheUserPraiseArticle(dto,true);

    }

    /**
     * 缓存取消点赞时的相关信息
     * @param dto
     * @throws Exception
     */
    private void cachePraiseOff(final PraiseDto dto) {
        //选择的数据结构为Hash, Key --字符串， 存储redis的标志；field -文章id ; Value - 用户id列表
        HashOperations<String,String, Set<Integer>> praiseHash = redisTemplate.opsForHash();

        //查询点赞过当前文章的用户id列表
        Set<Integer> uIds = praiseHash.get(Constant.RedisArticlePraiseHashKey,dto.getArticleId().toString());
        if (uIds != null && !uIds.isEmpty() && uIds.contains(dto.getUserId())) {
            //如果有当前用户的点赞记录，就移除
           uIds.remove(dto.getUserId());
           //把移除后的数据重新放入Set集合
            praiseHash.put(Constant.RedisArticlePraiseHashKey,dto.getArticleId().toString(),uIds);
        }
        //TODO:缓存点赞排行榜
        this.cachePraiseRank(dto,uIds.size());

        //TODO:缓存用户的点赞轨迹（用户点赞过的历史文章，用户维护）
        this.cacheUserPraiseArticle(dto,false);
    }

    /**
     * 缓存点赞排行榜（SortedSet->ZSet）
     * value为 "文章ID" + "_" + "文章标题"
     * score为 点赞总数
     */
    private void cachePraiseRank(final PraiseDto dto,final Integer total) {
        String value =dto.getArticleId()+Constant.SplitChar+dto.getTitle();
        ZSetOperations<String,String> praiseSort = redisTemplate.opsForZSet();
        //TODO：删除原始数据
        praiseSort.remove(Constant.RedisArticlePraiseSortKey,value);
        //@Nullable
        //	Boolean add(K key, V value, double score);
        //TODO:存入最新现在排行榜数据
        praiseSort.add(Constant.RedisArticlePraiseSortKey,value,total.doubleValue());
    }

    /**
     * 以用户为维度，存储用户点赞过的信息
     * key = 存储到redis的标准符
     * field = 用户id + "-" 文章id
     * value = 文章标题
     * @param dto
     * @param isOn true:点赞直接存储  false:取消点赞，重置value为null/""
     */
    private void cacheUserPraiseArticle(final PraiseDto dto, Boolean isOn) {
        final String field = dto.getUserId() + Constant.SplitChar + dto.getArticleId();
        HashOperations<String,String,String> hash = redisTemplate.opsForHash();
        if (isOn) {
            hash.put(Constant.RedisArticleUserPraiseKey,field, dto.getTitle());
        } else {
            hash.put(Constant.RedisArticleUserPraiseKey,field,"");
        }

    }


}
