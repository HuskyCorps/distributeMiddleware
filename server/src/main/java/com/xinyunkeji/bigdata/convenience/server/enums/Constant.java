package com.xinyunkeji.bigdata.convenience.server.enums;

/**
 * 系统常量配置
 *
 * @author Yuezejian
 * @date 2020年 08月23日 09:05:01
 */
public class Constant {
    public static final String RedisStringPrefix="SpringBootRedis:String:V1:";

    public static final String RedisListPrefix="SpringBootRedis:List:User:V1:";

    public static final String RedisListNoticeKey="SpringBootRedis:List:Queue:Notice:V1";

    public static final String RedisSetKey="SpringBoot:Redis:Set:User:Email";

    public static final String RedisSetProblemKey="SpringBoot:Redis:Set:Problem";

    public static final String RedisSetProblemsKey="SpringBoot:Redis:Set:Problems:V1";

    public static final String RedisSortedSetKey1 ="SpringBootRedis:SortedSet:PhoneFare:key1";

    public static final String RedisSortedSetKey2 ="SpringBootRedis:SortedSet:PhoneFare:key:V1";

    public static final String RedisHashKeyConfig ="SpringBootRedis:Hash:Key:SysConfig:V1";

    public static final String RedisExpireKey ="SpringBootRedis:Key:Expire";

    public static final String RedisRepeatKey ="SpringBootRedis:Key:Expire:Repeat:";


    public static final String RedisCacheBeatLockKey="SpringBootRedis:LockKey:";


    public static final String RedisArticlePraiseUser ="SpringBootRedis:Article:Praise:User:V2";

    public static final String RedisArticlePraiseHashKey ="SpringBootRedis:Hash:Article:Praise:V2";

    public static final String RedisArticlePraiseSortKey ="SpringBootRedis:Hash:Article:Sort:V2";

    public static final String RedisArticleUserPraiseKey ="SpringBootRedis:Hash:Article:User:Praise:V2";

    public static final String SplitChar = "_";

    public static final String RedisTopicNameEmail="SpringBootRedisTopicEmailInfo";

    public static final String logOperateUser="yuezejian";

    public static final String RedisMsgCodeKey="SpringBootRedis:MsgCode:";

    public static final String RedissonMsgCodeKey="SpringBootRedisson:MsgCode";

    public static final String RedissonUserVIPKey="SpringBootRedisson:UserVIP:V2";

    public static final String RedissonUserVipQueue="SpringBootRedisson:UserVip:Queue";

    public static final String SplitCharUserVip="-";

    /**
     * 邮件发送状态: 0投递中 1投递成功 2投递失败 3已消费
     */
    public static final Long DELIVER_LOADING = 0L;

    public static final Long DELIVER_SUCCESS = 1L;

    public static final Long DELIVER_FALSE = 2L;

    public static final Long CONSUME_SUCCESS = 3L;

    public static final Long CONSUME_FALSE = 4L;

    public static final Long MAIL_RESEND_MAX_TIME = 3L;

    /**
     * 10秒
     */
    public static final Long NEXT_TRY_TIME_AFTER = 10000L;

    /**
     * 企业微信消息推送（HuskyYue测试用例：消息推送 AgentId: 1000002 ）
     */
    public static String WXApiUrl = "https://qyapi.weixin.qq.com/cgi-bin/";
    public static String corpId = "wwe376f3d6cf5d6422";
    /**
     * 消息推送
     */
    public static String corpSecret = "3uPlI2-KmOot5SYDLQN22SotizLGi4hPCbJOSD5O_TY";
    /**
     * 公告
     */
    public static String corpSecretForPublicMessage = "";










    //vip会员过期前 x 天/小时/分钟/秒 进行提醒...; 即 ttl=vipDay - x;
    public static final Integer x=10;


    //用户会员到期前的多次提醒的标识
    public enum VipExpireFlg{

        First(1),
        End(2),

        ;

        private Integer type;

        VipExpireFlg(Integer type) {
            this.type = type;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }
    }
}