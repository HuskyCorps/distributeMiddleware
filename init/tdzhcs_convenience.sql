/*
Navicat MySQL Data Transfer

Source Server         : work
Source Server Version : 50721
Source Host           : localhost:3306
Source Database       : tdzhcs_convenience

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2020-08-29 22:15:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for appendix
-- ----------------------------
DROP TABLE IF EXISTS `appendix`;
CREATE TABLE `appendix` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `module_id` int(11) DEFAULT NULL COMMENT '所属模块记录主键id',
  `module_code` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '所属模块编码',
  `module_name` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '所属模块名称',
  `name` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '文件名称',
  `size` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '文件大小',
  `suffix` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '文件后缀名',
  `file_url` varchar(500) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '文件访问的磁盘目录',
  `is_active` tinyint(4) DEFAULT '1' COMMENT '是否有效(1=是;0=否)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='附件(文件)上传记录';

-- ----------------------------
-- Records of appendix
-- ----------------------------
INSERT INTO `appendix` VALUES ('1', '1', 'item', '商品', '商品图片', '1', '.jpg', 'E:\\\\srv\\\\middleware\\\\appendix\\\\img1.jpg', '1', '2019-12-26 18:09:32');
INSERT INTO `appendix` VALUES ('2', '2', 'item', '商品', '6 (2).jpg', '43164', '.jpg', 'E:\\srv\\middleware\\appendix\\20191226\\2\\34193209178200.jpg', '1', '2019-12-26 18:31:24');
INSERT INTO `appendix` VALUES ('3', '3', 'item', '商品模块', '1 (2).jpg', '16583', '.jpg', 'E:\\srv\\middleware\\appendix\\20191226\\3\\40701033307400.jpg', '1', '2019-12-26 20:19:52');
INSERT INTO `appendix` VALUES ('4', '4', 'item', '商品模块', 'github1.jpg', '35983', '.jpg', '20191226\\item\\4\\40877665764900.jpg', '1', '2019-12-26 20:22:48');
INSERT INTO `appendix` VALUES ('5', '5', 'item', '商品模块', '6.jpg', '190014', '.jpg', '20191226\\item\\5\\48360177706700.jpg', '1', '2019-12-26 22:27:31');
INSERT INTO `appendix` VALUES ('6', '6', 'item', '商品模块', '6.jpg', '190014', '.jpg', '20191226\\item\\6\\48667473135200.jpg', '1', '2019-12-26 22:32:38');
INSERT INTO `appendix` VALUES ('7', '7', 'item', '商品模块', '李小龙.jpg', '190014', '.jpg', '20191226\\item\\48756732970000.jpg', '1', '2019-12-26 22:34:07');

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '文章标题',
  `content` varchar(500) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '文章内容',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `scan_total` int(255) DEFAULT NULL COMMENT '浏览量',
  `praise_total` int(255) DEFAULT '0' COMMENT '点赞量',
  `is_active` tinyint(4) DEFAULT '1' COMMENT '是否有效（1=是;0=否）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='文章表';

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `article` VALUES ('1', 'Redis实战一', '这是Redis实战一的文章', '1', '1', '0', '1', '2020-03-19 09:50:08', '2020-03-19 17:31:30');
INSERT INTO `article` VALUES ('2', 'Redis实战二', '这是Redis实战二的文章', '2', '11', '1', '1', '2020-03-19 09:50:27', '2020-03-19 17:32:19');
INSERT INTO `article` VALUES ('3', 'Redis实战三', '这是Redis实战三的文章', '3', '12', '0', '1', '2020-03-19 09:50:40', '2020-03-19 17:32:07');

-- ----------------------------
-- Table structure for article_praise
-- ----------------------------
DROP TABLE IF EXISTS `article_praise`;
CREATE TABLE `article_praise` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `article_id` int(11) NOT NULL COMMENT '文章id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `p_time` datetime DEFAULT NULL COMMENT '点赞时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_article_id_user_id` (`article_id`,`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COMMENT='点赞记录表';

-- ----------------------------
-- Records of article_praise
-- ----------------------------
INSERT INTO `article_praise` VALUES ('36', '2', '1', '2020-03-19 17:31:41', null);

-- ----------------------------
-- Table structure for item
-- ----------------------------
DROP TABLE IF EXISTS `item`;
CREATE TABLE `item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '商品名',
  `code` varchar(255) DEFAULT NULL COMMENT '商品编号',
  `is_active` int(11) DEFAULT '1' COMMENT '是否有效（1=是；0=否）',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='商品表';

-- ----------------------------
-- Records of item
-- ----------------------------
INSERT INTO `item` VALUES ('1', 'd', 'd', '1', null);
INSERT INTO `item` VALUES ('2', 'Java全程实战', 'JavaCode', '1', null);
INSERT INTO `item` VALUES ('3', 'Java全程实战2', 'JavaCode2', '1', null);
INSERT INTO `item` VALUES ('4', 'Java全程实战3', 'JavaCode3', '1', null);
INSERT INTO `item` VALUES ('5', 'Java全程实战4', 'JavaCode4', '1', null);
INSERT INTO `item` VALUES ('6', 'Java全程实战5', 'JavaCode5', '1', null);
INSERT INTO `item` VALUES ('7', 'Java全程实战6', 'JavaCode6', '1', null);

-- ----------------------------
-- Table structure for mail_encrypt
-- ----------------------------
DROP TABLE IF EXISTS `mail_encrypt`;
CREATE TABLE `mail_encrypt` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `user_email` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '用户邮箱',
  `encrypt_info` varchar(500) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '加密信息',
  `is_active` tinyint(4) DEFAULT '0' COMMENT '是否有效(1=是；0=否)',
  `is_send` tinyint(4) DEFAULT NULL COMMENT '是否发送成功(1=是;0=否)',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='加密串-历史邮件发送表';

-- ----------------------------
-- Records of mail_encrypt
-- ----------------------------
INSERT INTO `mail_encrypt` VALUES ('11', '1016', 'linsenzhong@126.com', 'lkgqrYZji1hPGO2JQ0ZA3BQ+/D+gThAMl7Pr4RnElPasrVeuJe4wCCl8rdLnl0xqgl8CIWlS6GalYnZ/+Si2pi/HmhWgmeufoKvOHk4Qb6S96rOx3+bTTE/ayhOQcsjCKJzbKfWwvpBI4OfOYSbJrg==', '0', '1', '2019-12-30 11:00:53', '2019-12-30 11:00:53', null);

-- ----------------------------
-- Table structure for msg_log
-- ----------------------------
DROP TABLE IF EXISTS `msg_log`;
CREATE TABLE `msg_log` (
  `msg_id` varchar(255) NOT NULL DEFAULT '' COMMENT '消息唯一标识',
  `msg` text COMMENT '消息体, json格式化',
  `exchange` varchar(255) NOT NULL DEFAULT '' COMMENT '交换机',
  `routing_key` varchar(255) NOT NULL DEFAULT '' COMMENT '路由键',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态: 0投递中 1投递成功 2投递失败 3已消费',
  `try_count` int(11) NOT NULL DEFAULT '0' COMMENT '重试次数',
  `next_try_time` datetime DEFAULT NULL COMMENT '下一次重试时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`msg_id`),
  UNIQUE KEY `unq_msg_id` (`msg_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息投递日志';

-- ----------------------------
-- Records of msg_log
-- ----------------------------
INSERT INTO `msg_log` VALUES ('1f5fe6b3-7ef3-4a2f-a088-051461825c9b', '{\"correlationData\":\"1f5fe6b3-7ef3-4a2f-a088-051461825c9b\",\"userMails\":\"yuezej方法ian@qq.com\",\"subject\":\"李白\",\"content\":\"libaianqikla@qq.com\"}', 'convenienceService.email.exchange', 'convenienceService.email.routing.key', '2', '0', null, '2020-08-29 18:46:02', '2020-08-29 18:46:02');
INSERT INTO `msg_log` VALUES ('3502b043-c696-47f3-83ad-ae5f87d7081e', '{\"correlationData\":\"3502b043-c696-47f3-83ad-ae5f87d7081e\",\"userMails\":\"yuezejian@qq.com\",\"subject\":\"李白\",\"content\":\"libaianqikla@qq.com\"}', 'convenienceService.email.exchange', 'convenienceService.email.routing.key', '3', '0', null, '2020-08-29 18:43:20', '2020-08-29 18:43:20');
INSERT INTO `msg_log` VALUES ('3f1f7fd0-757f-4ee1-8b7f-502c2567c397', '{\"correlationData\":\"3f1f7fd0-757f-4ee1-8b7f-502c2567c397\",\"userMails\":\"yuezejian@qq.com\",\"subject\":\"李白\",\"content\":\"libaianqikla@qq.com\"}', 'convenienceService.email.exchange', 'convenienceService.email.routing.key', '1', '0', null, '2020-08-29 18:45:17', '2020-08-29 18:45:17');
INSERT INTO `msg_log` VALUES ('67992908-2c06-4ef2-a175-d6ba469a4ded', '{\"correlationData\":\"67992908-2c06-4ef2-a175-d6ba469a4ded\",\"userMails\":\"yuezej方法ian@qq.com\",\"subject\":\"李白\",\"content\":\"libaianqikla@qq.com\"}', 'convenienceService.email.exchange', 'convenienceService.email.routing.key', '2', '0', null, '2020-08-29 18:46:13', '2020-08-29 18:46:13');
INSERT INTO `msg_log` VALUES ('73feebb3-87e4-4338-bf59-743e256185ef', '{\"correlationData\":\"73feebb3-87e4-4338-bf59-743e256185ef\",\"userMails\":\"yuezej方法ian@qq.com\",\"subject\":\"李白\",\"content\":\"libaianqikla@qq.com\"}', 'convenienceService.email.exchange', 'convenienceService.email.routing.key', '2', '0', null, '2020-08-29 18:45:48', '2020-08-29 18:45:48');
INSERT INTO `msg_log` VALUES ('7beb2113-1385-473c-9fb6-b39d608f30c3', '{\"correlationData\":\"7beb2113-1385-473c-9fb6-b39d608f30c3\",\"userMails\":\"yuezejian@qq.com\",\"subject\":\"李白\",\"content\":\"libaianqikla@qq.com\"}', 'convenienceService.email.exchange', 'convenienceService.email.routing.key', '3', '0', null, '2020-08-29 18:44:31', '2020-08-29 18:44:31');
INSERT INTO `msg_log` VALUES ('887a0067-bf85-43df-9e92-11bf69da5e12', '{\"correlationData\":\"887a0067-bf85-43df-9e92-11bf69da5e12\",\"userMails\":\"yuezejian@qq.com\",\"subject\":\"李白\",\"content\":\"libaianqikla@qq.com\"}', 'convenienceService.email.exchange', 'convenienceService.email.routing.key', '3', '0', null, '2020-08-29 18:45:28', '2020-08-29 18:45:28');
INSERT INTO `msg_log` VALUES ('8d904d22-fe95-4aeb-8f71-a13d7b9e31ad', '{\"correlationData\":\"8d904d22-fe95-4aeb-8f71-a13d7b9e31ad\",\"userMails\":\"yuezejian@qq.com\",\"subject\":\"李白\",\"content\":\"libaianqikla@qq.com\"}', 'convenienceService.email.exchange', 'convenienceService.email.routing.key', '3', '0', null, '2020-08-29 18:45:17', '2020-08-29 18:45:17');
INSERT INTO `msg_log` VALUES ('b1650755-127d-47b1-be4c-8dd29d134f94', '{\"correlationData\":\"b1650755-127d-47b1-be4c-8dd29d134f94\",\"userMails\":\"yuezejian@qq.com\",\"subject\":\"李白\",\"content\":\"libaianqikla@qq.com\"}', 'convenienceService.email.exchange', 'convenienceService.email.routing.key', '3', '0', null, '2020-08-29 21:56:28', '2020-08-29 21:56:28');
INSERT INTO `msg_log` VALUES ('c3035202-ebac-4718-ae29-61d52556c7f9', '{\"correlationData\":\"c3035202-ebac-4718-ae29-61d52556c7f9\",\"userMails\":\"yuezej方法ian@qq.com\",\"subject\":\"李白\",\"content\":\"libaianqikla@qq.com\"}', 'convenienceService.email.exchange', 'convenienceService.email.routing.key', '2', '0', null, '2020-08-29 18:45:57', '2020-08-29 18:45:57');
INSERT INTO `msg_log` VALUES ('d0b38832-9dbb-4814-8f5a-4ea16290ceb6', '{\"correlationData\":\"d0b38832-9dbb-4814-8f5a-4ea16290ceb6\",\"userMails\":\"yuezej方法ian@qq.com\",\"subject\":\"李白\",\"content\":\"libaianqikla@qq.com\"}', 'convenienceService.email.exchange', 'convenienceService.email.routing.key', '2', '0', null, '2020-08-29 18:46:07', '2020-08-29 18:46:07');
INSERT INTO `msg_log` VALUES ('dbf5f93a-3dcb-4cf2-b67e-077cf782ea4f', '{\"correlationData\":\"dbf5f93a-3dcb-4cf2-b67e-077cf782ea4f\",\"userMails\":\"yuezej顶顶顶顶ian@qq.com\",\"subject\":\"李白\",\"content\":\"libaianqikla@qq.com\"}', 'convenienceService.email.exchange', 'convenienceService.email.routing.key', '2', '0', null, '2020-08-29 18:42:00', '2020-08-29 18:42:00');
INSERT INTO `msg_log` VALUES ('e1915997-3af2-4564-bb12-4c0f1112fc5e', '{\"correlationData\":\"e1915997-3af2-4564-bb12-4c0f1112fc5e\",\"userMails\":\"yuezejian@qq.com\",\"subject\":\"李白\",\"content\":\"libaianqikla@qq.com\"}', 'convenienceService.email.exchange', 'convenienceService.email.routing.key', '3', '0', null, '2020-08-29 18:45:34', '2020-08-29 18:45:34');

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '通告标题',
  `content` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '内容',
  `is_active` tinyint(4) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='通告';

-- ----------------------------
-- Records of notice
-- ----------------------------

-- ----------------------------
-- Table structure for send_record
-- ----------------------------
DROP TABLE IF EXISTS `send_record`;
CREATE TABLE `send_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '手机号',
  `code` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '短信验证码',
  `is_active` tinyint(4) DEFAULT '1' COMMENT '是否有效(1=是；0=否)',
  `send_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COMMENT='短信验证码发送记录';

-- ----------------------------
-- Records of send_record
-- ----------------------------
INSERT INTO `send_record` VALUES ('1', '15627284601', '9192', '0', '2020-03-14 18:45:42');
INSERT INTO `send_record` VALUES ('2', '15627284601', '2543', '0', '2020-03-14 18:42:43');
INSERT INTO `send_record` VALUES ('4', '18316960923', '5589', '0', '2020-03-14 19:09:48');
INSERT INTO `send_record` VALUES ('5', '18316960923', '5708', '0', '2020-03-14 19:50:52');
INSERT INTO `send_record` VALUES ('6', '18316960922', '0724', '0', '2020-03-15 14:11:53');
INSERT INTO `send_record` VALUES ('7', '18316960930', '1882', '0', '2020-03-15 16:55:55');
INSERT INTO `send_record` VALUES ('8', '18316960931', '1053', '0', '2020-03-15 16:59:20');
INSERT INTO `send_record` VALUES ('9', '18316960932', '2513', '0', '2020-03-17 22:00:25');
INSERT INTO `send_record` VALUES ('10', '15812380967', '1712', '0', '2020-03-17 22:25:33');
INSERT INTO `send_record` VALUES ('11', '15812380967', '9721', '0', '2020-03-17 22:26:08');
INSERT INTO `send_record` VALUES ('12', '999999999', '3191', '0', '2020-03-18 10:00:47');
INSERT INTO `send_record` VALUES ('13', '999999999', '8727', '0', '2020-03-18 10:02:09');
INSERT INTO `send_record` VALUES ('14', '999999999', '1334', '0', '2020-03-18 10:03:46');
INSERT INTO `send_record` VALUES ('15', '999999998', '4740', '0', '2020-03-18 11:01:06');
INSERT INTO `send_record` VALUES ('16', '999999998', '3056', '0', '2020-03-18 11:04:02');
INSERT INTO `send_record` VALUES ('17', '999999997', '9476', '0', '2020-03-18 11:04:10');
INSERT INTO `send_record` VALUES ('18', '999999996', '4854', '0', '2020-03-18 11:04:27');
INSERT INTO `send_record` VALUES ('19', '15812490989', '2766', '0', '2020-03-18 11:16:29');
INSERT INTO `send_record` VALUES ('20', '15812490989', '1018', '0', '2020-03-18 14:55:02');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `operator_table` varchar(50) DEFAULT NULL,
  `operation` varchar(50) DEFAULT NULL COMMENT '用户操作',
  `method` varchar(200) DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) DEFAULT NULL COMMENT '请求参数',
  `time` bigint(20) DEFAULT '0' COMMENT '执行时长(毫秒)',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `memo` text CHARACTER SET utf8mb4 COMMENT '备注信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=345 DEFAULT CHARSET=utf8 COMMENT='系统日志';

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES ('342', 'yuezejian', null, '新增用户-rabbitmq', 'addUserMQ', '{\"id\":66,\"name\":\"岳泽健1\",\"email\":\"1yuezejian@qq.com\"}', '0', '127.0.0.1', '2020-08-29 21:52:27', null);
INSERT INTO `sys_log` VALUES ('343', 'yuezejian', '', '新增用户-spring aop', 'com.xinyunkeji.bigdata.convenience.server.controller.LogController.addUserVip()', '{\"id\":67,\"name\":\"岳泽健\",\"email\":\"1yue2zejiadn@qq.com\"}', '6', '127.0.0.1', '2020-08-29 21:52:31', '{\"code\":200,\"msg\":\"success\"}');
INSERT INTO `sys_log` VALUES ('344', 'yuezejian', '', '发送邮件', 'com.xinyunkeji.bigdata.convenience.server.controller.MailController.stringData()', '{\"correlationData\":\"b1650755-127d-47b1-be4c-8dd29d134f94\",\"userMails\":\"yuezejian@qq.com\",\"subject\":\"李白\",\"content\":\"libaianqikla@qq.com\"}', '21', '127.0.0.1', '2020-08-29 21:56:28', '{\"code\":200,\"msg\":\"success\"}');

-- ----------------------------
-- Table structure for thread_code
-- ----------------------------
DROP TABLE IF EXISTS `thread_code`;
CREATE TABLE `thread_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` int(255) NOT NULL COMMENT '编码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_code` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8 COMMENT='多线程压测生成短信验证码';

-- ----------------------------
-- Records of thread_code
-- ----------------------------
INSERT INTO `thread_code` VALUES ('66', '16');
INSERT INTO `thread_code` VALUES ('50', '56');
INSERT INTO `thread_code` VALUES ('18', '85');
INSERT INTO `thread_code` VALUES ('91', '87');
INSERT INTO `thread_code` VALUES ('45', '138');
INSERT INTO `thread_code` VALUES ('44', '289');
INSERT INTO `thread_code` VALUES ('65', '320');
INSERT INTO `thread_code` VALUES ('71', '564');
INSERT INTO `thread_code` VALUES ('87', '579');
INSERT INTO `thread_code` VALUES ('57', '684');
INSERT INTO `thread_code` VALUES ('15', '779');
INSERT INTO `thread_code` VALUES ('19', '796');
INSERT INTO `thread_code` VALUES ('35', '798');
INSERT INTO `thread_code` VALUES ('10', '810');
INSERT INTO `thread_code` VALUES ('7', '919');
INSERT INTO `thread_code` VALUES ('81', '933');
INSERT INTO `thread_code` VALUES ('62', '1002');
INSERT INTO `thread_code` VALUES ('47', '1021');
INSERT INTO `thread_code` VALUES ('55', '1181');
INSERT INTO `thread_code` VALUES ('1', '1371');
INSERT INTO `thread_code` VALUES ('98', '1380');
INSERT INTO `thread_code` VALUES ('26', '1425');
INSERT INTO `thread_code` VALUES ('24', '1590');
INSERT INTO `thread_code` VALUES ('30', '1709');
INSERT INTO `thread_code` VALUES ('48', '1920');
INSERT INTO `thread_code` VALUES ('89', '2206');
INSERT INTO `thread_code` VALUES ('6', '2238');
INSERT INTO `thread_code` VALUES ('82', '2389');
INSERT INTO `thread_code` VALUES ('39', '2477');
INSERT INTO `thread_code` VALUES ('88', '2497');
INSERT INTO `thread_code` VALUES ('37', '2629');
INSERT INTO `thread_code` VALUES ('70', '2935');
INSERT INTO `thread_code` VALUES ('28', '3248');
INSERT INTO `thread_code` VALUES ('83', '3359');
INSERT INTO `thread_code` VALUES ('12', '3408');
INSERT INTO `thread_code` VALUES ('33', '3649');
INSERT INTO `thread_code` VALUES ('38', '3680');
INSERT INTO `thread_code` VALUES ('29', '3763');
INSERT INTO `thread_code` VALUES ('51', '3867');
INSERT INTO `thread_code` VALUES ('22', '4108');
INSERT INTO `thread_code` VALUES ('92', '4125');
INSERT INTO `thread_code` VALUES ('32', '4214');
INSERT INTO `thread_code` VALUES ('2', '4385');
INSERT INTO `thread_code` VALUES ('49', '4425');
INSERT INTO `thread_code` VALUES ('72', '4645');
INSERT INTO `thread_code` VALUES ('14', '4818');
INSERT INTO `thread_code` VALUES ('63', '4998');
INSERT INTO `thread_code` VALUES ('67', '5076');
INSERT INTO `thread_code` VALUES ('61', '5157');
INSERT INTO `thread_code` VALUES ('94', '5299');
INSERT INTO `thread_code` VALUES ('5', '5497');
INSERT INTO `thread_code` VALUES ('46', '5546');
INSERT INTO `thread_code` VALUES ('59', '5817');
INSERT INTO `thread_code` VALUES ('86', '5828');
INSERT INTO `thread_code` VALUES ('34', '5943');
INSERT INTO `thread_code` VALUES ('13', '6064');
INSERT INTO `thread_code` VALUES ('80', '6171');
INSERT INTO `thread_code` VALUES ('25', '6188');
INSERT INTO `thread_code` VALUES ('17', '6284');
INSERT INTO `thread_code` VALUES ('52', '6396');
INSERT INTO `thread_code` VALUES ('96', '6467');
INSERT INTO `thread_code` VALUES ('93', '6760');
INSERT INTO `thread_code` VALUES ('90', '6762');
INSERT INTO `thread_code` VALUES ('76', '6910');
INSERT INTO `thread_code` VALUES ('56', '6919');
INSERT INTO `thread_code` VALUES ('85', '6948');
INSERT INTO `thread_code` VALUES ('73', '7006');
INSERT INTO `thread_code` VALUES ('42', '7590');
INSERT INTO `thread_code` VALUES ('27', '7599');
INSERT INTO `thread_code` VALUES ('54', '7695');
INSERT INTO `thread_code` VALUES ('78', '7814');
INSERT INTO `thread_code` VALUES ('41', '7842');
INSERT INTO `thread_code` VALUES ('77', '7991');
INSERT INTO `thread_code` VALUES ('40', '8014');
INSERT INTO `thread_code` VALUES ('58', '8017');
INSERT INTO `thread_code` VALUES ('31', '8044');
INSERT INTO `thread_code` VALUES ('20', '8093');
INSERT INTO `thread_code` VALUES ('21', '8143');
INSERT INTO `thread_code` VALUES ('16', '8276');
INSERT INTO `thread_code` VALUES ('53', '8371');
INSERT INTO `thread_code` VALUES ('4', '8440');
INSERT INTO `thread_code` VALUES ('11', '8451');
INSERT INTO `thread_code` VALUES ('8', '8460');
INSERT INTO `thread_code` VALUES ('99', '8499');
INSERT INTO `thread_code` VALUES ('97', '8644');
INSERT INTO `thread_code` VALUES ('3', '8728');
INSERT INTO `thread_code` VALUES ('43', '8824');
INSERT INTO `thread_code` VALUES ('100', '8916');
INSERT INTO `thread_code` VALUES ('36', '9065');
INSERT INTO `thread_code` VALUES ('64', '9130');
INSERT INTO `thread_code` VALUES ('84', '9231');
INSERT INTO `thread_code` VALUES ('23', '9277');
INSERT INTO `thread_code` VALUES ('79', '9323');
INSERT INTO `thread_code` VALUES ('68', '9483');
INSERT INTO `thread_code` VALUES ('60', '9562');
INSERT INTO `thread_code` VALUES ('75', '9804');
INSERT INTO `thread_code` VALUES ('95', '9814');
INSERT INTO `thread_code` VALUES ('9', '9857');
INSERT INTO `thread_code` VALUES ('69', '9969');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '姓名',
  `email` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_email` (`email`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '张三', '1948831260@qq.com');
INSERT INTO `user` VALUES ('2', '李四', 'linsenzhon@126.com');
INSERT INTO `user` VALUES ('3', '王五', 'linsenzhon2@126.com');
INSERT INTO `user` VALUES ('4', '赵六', 'linsenzhon3@126.com');
INSERT INTO `user` VALUES ('5', '周七', 'linsenzhon4@126.com');
INSERT INTO `user` VALUES ('6', '郑八', 'zhengba@126.com');
INSERT INTO `user` VALUES ('15', '李大龙', 'lidalong@qq.com');
INSERT INTO `user` VALUES ('16', '李小龙', 'lixiaolong@qq.com');
INSERT INTO `user` VALUES ('17', '王二小', 'wangerxiao@qq.com');
INSERT INTO `user` VALUES ('18', '王二小2', 'wangerxiao2@qq.com');
INSERT INTO `user` VALUES ('21', '周润发', 'zhourunfa@qq.com');
INSERT INTO `user` VALUES ('24', '周润发2', 'zhourunfa2@qq.com');
INSERT INTO `user` VALUES ('26', '王三', 'wangsan@qq.com');
INSERT INTO `user` VALUES ('64', '李白', 'libaianqila@qq.com');
INSERT INTO `user` VALUES ('65', '李白', 'libaianqikla@qq.com');
INSERT INTO `user` VALUES ('66', '岳泽健1', '1yuezejian@qq.com');
INSERT INTO `user` VALUES ('67', '岳泽健', '1yue2zejiadn@qq.com');
INSERT INTO `user` VALUES ('69', '李白', 'libaiandqikla@qq.com');

-- ----------------------------
-- Table structure for user_order
-- ----------------------------
DROP TABLE IF EXISTS `user_order`;
CREATE TABLE `user_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `order_no` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '订单编号',
  `pay_status` tinyint(255) DEFAULT '1' COMMENT '支付状态(1=未支付;2=已支付;3=已取消)',
  `is_active` tinyint(255) DEFAULT '1' COMMENT '是否有效(1=是;0=否)',
  `order_time` datetime DEFAULT NULL COMMENT '下单时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='用户下单记录表';

-- ----------------------------
-- Records of user_order
-- ----------------------------
INSERT INTO `user_order` VALUES ('10', '200011', '1237225166472491008', '1', '0', '2020-03-10 11:54:01', '2020-03-10 12:07:02');
INSERT INTO `user_order` VALUES ('11', '200012', '1237225328951439360', '1', '0', '2020-03-10 11:54:40', '2020-03-10 12:07:02');
INSERT INTO `user_order` VALUES ('12', '200013', '1237228616820207616', '1', '0', '2020-03-10 12:07:44', '2020-03-10 12:08:30');
INSERT INTO `user_order` VALUES ('13', '200014', '1237228655277780992', '2', '1', '2020-03-10 12:07:53', '2020-03-10 12:07:59');

-- ----------------------------
-- Table structure for user_vip
-- ----------------------------
DROP TABLE IF EXISTS `user_vip`;
CREATE TABLE `user_vip` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 NOT NULL COMMENT '用户姓名',
  `phone` varchar(255) CHARACTER SET utf8mb4 NOT NULL COMMENT '手机号',
  `email` varchar(255) CHARACTER SET utf8mb4 NOT NULL COMMENT '用户邮箱',
  `is_active` smallint(255) DEFAULT '1' COMMENT '是否有效(1=是;0=否)',
  `vip_time` datetime DEFAULT NULL COMMENT '充值会员的时间',
  `vip_day` int(11) DEFAULT NULL COMMENT '会员的天数',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='用户会员充值记录表';

-- ----------------------------
-- Records of user_vip
-- ----------------------------
INSERT INTO `user_vip` VALUES ('8', '大圣', '15812490873', 'linsenzhong@126.com', '1', '2020-03-18 15:39:24', '20', null);
INSERT INTO `user_vip` VALUES ('9', '大圣2', '999999999', 'linsenzhong@126.com', '0', '2020-03-18 15:51:24', '20', '2020-03-18 15:51:52');
INSERT INTO `user_vip` VALUES ('10', '修罗debug', '999999999', 'linsenzhong@126.com', '0', '2020-03-18 21:38:03', '20', '2020-03-18 21:38:25');
INSERT INTO `user_vip` VALUES ('11', '修罗debug2', '999999992', 'linsenzhong@126.com', '0', '2020-03-18 21:39:36', '15', '2020-03-18 21:40:00');
