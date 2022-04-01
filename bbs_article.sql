/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50536
Source Host           : localhost:3306
Source Database       : keepstudy

Target Server Type    : MYSQL
Target Server Version : 50536
File Encoding         : 65001

Date: 2022-04-01 12:13:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `bbs_article`
-- ----------------------------
DROP TABLE IF EXISTS `bbs_article`;
CREATE TABLE `bbs_article` (
  `id` char(19) NOT NULL COMMENT '文章id',
  `category_id` char(19) NOT NULL COMMENT '文章分类id',
  `user_id` char(19) NOT NULL COMMENT '用户id',
  `nickname` varchar(50) NOT NULL COMMENT '昵称',
  `avatar` varchar(255) NOT NULL COMMENT '用户头像',
  `title` varchar(50) NOT NULL COMMENT '文章标题',
  `description` varchar(50) NOT NULL COMMENT '文章描述',
  `content` longtext NOT NULL COMMENT '文章内容',
  `sort` bigint(10) DEFAULT '0' COMMENT '排序字段',
  `is_column_article` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0不是专栏文章  1是专栏文章',
  `is_release` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0未发布  1发布',
  `is_bbs` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0未同步到江湖  1同步',
  `views` bigint(10) NOT NULL DEFAULT '0' COMMENT '浏览量',
  `is_violation_article` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0未违规 1违规',
  `is_excellent_article` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否为精品文章 0不是 1是',
  `is_top` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是不是置顶，0不置，1置',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of bbs_article
-- ----------------------------
INSERT INTO `bbs_article` VALUES ('1496359576990121985', '1492434355002302466', '1489885385067622401', '天天搬砖', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '我的大学历程', '描述我的大学生活', '哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈', '0', '0', '1', '0', '8', '0', '1', '0', '1', '2022-02-23 13:41:38', '2022-03-15 17:51:07');
INSERT INTO `bbs_article` VALUES ('1497831076091961345', '1492434355253960706', '1489885385067622401', '天天搬砖', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '网站遵守规范', '本网站遵守规范', '哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈', '0', '0', '1', '0', '2', '0', '0', '1', '1', '2022-02-27 15:08:51', '2022-02-27 15:08:51');
INSERT INTO `bbs_article` VALUES ('1499243244901490690', '1492434355291709441', '1489885385067622401', '天天搬砖', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', 'Feign远程调用时创建请求头注入token', 'Feign远程调用时创建请求头注入token', '哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈', '1', '0', '1', '0', '12', '0', '0', '0', '1', '2022-03-03 12:40:18', '2022-03-23 17:47:08');
INSERT INTO `bbs_article` VALUES ('1505114107760623617', '1492434355333652482', '1489885385067622401', '天天搬砖', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', 'redis解决分布式下面定时任务的重复执行', 'redis解决分布式下面定时任务的重复执行', '哈哈哈哈哈哈哈哈哈哈', '0', '0', '1', '0', '0', '0', '0', '0', '1', '2022-03-19 17:29:01', '2022-03-19 17:48:38');
INSERT INTO `bbs_article` VALUES ('1508337868081262593', '1492434355333652482', '1489885385067622401', '天天搬砖', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '在多数据源或者多模块下进行事务控制', '微服务使用seata进行事务控制', '哈哈哈哈哈哈哈哈哈哈哈', '0', '1', '0', '0', '0', '0', '0', '0', '1', '2022-03-28 14:59:05', '2022-03-29 15:34:16');
INSERT INTO `bbs_article` VALUES ('1508343665469173762', '1492434355333652482', '1489885385067622401', '天天搬砖', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '微服务模块注册中心--nacos', '微服务模块注册中心--nacos', '哈哈哈哈哈哈哈哈哈哈哈', '1', '1', '0', '1', '0', '0', '0', '0', '1', '2022-03-28 15:22:08', '2022-03-28 15:58:42');
INSERT INTO `bbs_article` VALUES ('1508345364929851393', '1492434355333652482', '1489885385067622401', '天天搬砖', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '流量防卫兵Sentinel', '流量防卫兵Sentinel', '哈哈哈哈哈哈哈哈哈哈哈', '0', '1', '0', '1', '0', '0', '0', '0', '1', '2022-03-28 15:28:53', '2022-03-28 15:28:53');

-- ----------------------------
-- Table structure for `bbs_article_right`
-- ----------------------------
DROP TABLE IF EXISTS `bbs_article_right`;
CREATE TABLE `bbs_article_right` (
  `id` char(19) NOT NULL COMMENT '主键id',
  `user_id` char(19) NOT NULL COMMENT '用户id',
  `money` int(2) NOT NULL DEFAULT '0' COMMENT '每日k币',
  `article_number` int(2) NOT NULL DEFAULT '0' COMMENT '每日发帖数量',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bbs_article_right
-- ----------------------------
INSERT INTO `bbs_article_right` VALUES ('1497847715374063618', '1489885385067622401', '40', '4', '5', '2022-02-27 16:14:58', '2022-03-28 15:28:53');
INSERT INTO `bbs_article_right` VALUES ('1497847715483115521', '1496388556204023809', '0', '0', '1', '2022-02-27 16:14:58', '2022-02-27 16:14:58');

-- ----------------------------
-- Table structure for `bbs_category`
-- ----------------------------
DROP TABLE IF EXISTS `bbs_category`;
CREATE TABLE `bbs_category` (
  `id` char(19) NOT NULL COMMENT '主键id',
  `category_name` varchar(20) NOT NULL COMMENT '文章分类名',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of bbs_category
-- ----------------------------
INSERT INTO `bbs_category` VALUES ('1492434355002302466', '程序人生', '1', '2022-02-12 17:44:12', '2022-02-12 17:44:12');
INSERT INTO `bbs_category` VALUES ('1492434355253960706', '管理', '1', '2022-02-12 17:44:13', '2022-02-12 17:44:13');
INSERT INTO `bbs_category` VALUES ('1492434355262349313', '教程', '1', '2022-02-12 17:44:13', '2022-02-12 17:44:13');
INSERT INTO `bbs_category` VALUES ('1492434355279126529', '学习笔记', '1', '2022-02-12 17:44:13', '2022-02-12 17:44:13');
INSERT INTO `bbs_category` VALUES ('1492434355291709441', '踩坑记录', '1', '2022-02-12 17:44:13', '2022-02-12 17:44:13');
INSERT INTO `bbs_category` VALUES ('1492434355308486657', '架构', '1', '2022-02-12 17:44:13', '2022-02-12 17:44:13');
INSERT INTO `bbs_category` VALUES ('1492434355333652482', '后台', '1', '2022-02-12 17:44:13', '2022-02-12 17:44:13');
INSERT INTO `bbs_category` VALUES ('1492434355346235394', '前端', '1', '2022-02-12 17:44:13', '2022-02-12 17:44:13');
INSERT INTO `bbs_category` VALUES ('1492434355363012609', '问答', '1', '2022-02-12 17:44:13', '2022-02-12 17:44:13');
INSERT INTO `bbs_category` VALUES ('1492434355379789826', '测试', '1', '2022-02-12 17:44:13', '2022-02-12 17:44:13');
INSERT INTO `bbs_category` VALUES ('1492434355392372738', '数据库', '1', '2022-02-12 17:44:13', '2022-02-12 17:44:13');
INSERT INTO `bbs_category` VALUES ('1492434355404955650', '安全', '1', '2022-02-12 17:44:13', '2022-02-12 17:44:13');
INSERT INTO `bbs_category` VALUES ('1492434355421732866', '面试', '1', '2022-02-12 17:44:13', '2022-02-12 17:44:13');
INSERT INTO `bbs_category` VALUES ('1492434355438510082', '理财', '1', '2022-02-12 17:44:13', '2022-02-12 17:44:13');

-- ----------------------------
-- Table structure for `bbs_collect`
-- ----------------------------
DROP TABLE IF EXISTS `bbs_collect`;
CREATE TABLE `bbs_collect` (
  `id` char(19) NOT NULL COMMENT '用户收藏文章id',
  `user_id` char(19) NOT NULL COMMENT '用户id',
  `article_id` char(19) NOT NULL COMMENT '收藏的文章id',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bbs_collect
-- ----------------------------
INSERT INTO `bbs_collect` VALUES ('1506541601705443329', '1496388556204023809', '1499243244901490690', '1', '2022-03-23 16:01:22', '2022-03-23 16:01:22');

-- ----------------------------
-- Table structure for `bbs_column`
-- ----------------------------
DROP TABLE IF EXISTS `bbs_column`;
CREATE TABLE `bbs_column` (
  `id` char(19) NOT NULL COMMENT '用户专栏id',
  `user_id` char(19) NOT NULL COMMENT '用户id',
  `title` varchar(30) NOT NULL COMMENT '专栏名称',
  `views` bigint(10) NOT NULL DEFAULT '0' COMMENT '浏览数',
  `vsibility` bigint(10) NOT NULL DEFAULT '0' COMMENT '可见度',
  `is_release` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0 不发布 1 发布，这里指专栏是否可被别人看见',
  `description` varchar(255) DEFAULT '' COMMENT '专栏描述',
  `color` varchar(255) NOT NULL DEFAULT 'background-image: linear-gradient(to right, rgb(130, 178, 242) 0%, rgb(51, 51, 51) 100%);' COMMENT '专栏渐变色',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of bbs_column
-- ----------------------------
INSERT INTO `bbs_column` VALUES ('1507256364869681154', '1489885385067622401', 'Spring Security专栏', '0', '0', '1', '', 'background-image: linear-gradient(to right, rgb(130, 178, 242) 0%, rgb(51, 51, 51) 100%);', '1', '2022-03-25 15:21:35', '2022-03-25 15:21:35');
INSERT INTO `bbs_column` VALUES ('1507256416908410882', '1489885385067622401', '数据结构专栏', '0', '0', '0', '', 'background-image: linear-gradient(to right, rgb(130, 178, 242) 0%, rgb(51, 51, 51) 100%);', '1', '2022-03-25 15:21:47', '2022-03-25 15:21:47');
INSERT INTO `bbs_column` VALUES ('1507947208253677570', '1489885385067622401', 'redis分布式锁', '0', '0', '1', '本专栏将会介绍在分布式情况下，定时任务的重复执行，redis的分布式锁的原理，和redis的分布式锁怎么解决定时任务的重复执行', 'background-image: linear-gradient(to right, rgb(130, 178, 242) 0%, rgb(51, 51, 51) 100%);', '1', '2022-03-27 13:06:45', '2022-03-28 11:10:09');
INSERT INTO `bbs_column` VALUES ('1508007277796356097', '1489885385067622401', 'Spring Cloud专栏', '0', '0', '1', '', 'background-image: linear-gradient(to right, rgb(130, 178, 242) 0%, rgb(51, 51, 51) 100%);', '1', '2022-03-27 17:05:27', '2022-03-27 17:05:27');
INSERT INTO `bbs_column` VALUES ('1508008133925105665', '1489885385067622401', 'Spring Cloud Alibaba专栏', '1', '0', '1', '', 'background-image: linear-gradient(to right, rgb(130, 178, 242) 0%, rgb(51, 51, 51) 100%);', '1', '2022-03-27 17:08:51', '2022-03-27 17:08:51');

-- ----------------------------
-- Table structure for `bbs_column_author`
-- ----------------------------
DROP TABLE IF EXISTS `bbs_column_author`;
CREATE TABLE `bbs_column_author` (
  `id` char(19) NOT NULL COMMENT '主键id',
  `column_id` char(19) NOT NULL COMMENT '用户专栏id',
  `user_id` char(19) NOT NULL COMMENT '用户id',
  `nickname` varchar(50) NOT NULL COMMENT '昵称',
  `avatar` varchar(255) NOT NULL COMMENT '用户头像',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of bbs_column_author
-- ----------------------------
INSERT INTO `bbs_column_author` VALUES ('1507256364878069761', '1507256364869681154', '1489885385067622401', '天天搬砖', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '1', '2022-03-25 15:21:35', '2022-03-25 15:21:35');
INSERT INTO `bbs_column_author` VALUES ('1507256416920993794', '1507256416908410882', '1489885385067622401', '天天搬砖', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '1', '2022-03-25 15:21:47', '2022-03-25 15:21:47');
INSERT INTO `bbs_column_author` VALUES ('1507947208274649089', '1507947208253677570', '1489885385067622401', '天天搬砖', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '1', '2022-03-27 13:06:45', '2022-03-27 13:06:45');
INSERT INTO `bbs_column_author` VALUES ('1508007277800550402', '1508007277796356097', '1489885385067622401', '天天搬砖', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '1', '2022-03-27 17:05:27', '2022-03-27 17:05:27');
INSERT INTO `bbs_column_author` VALUES ('1508008133937688577', '1508008133925105665', '1489885385067622401', '天天搬砖', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '1', '2022-03-27 17:08:51', '2022-03-27 17:08:51');

-- ----------------------------
-- Table structure for `bbs_colunm_article`
-- ----------------------------
DROP TABLE IF EXISTS `bbs_colunm_article`;
CREATE TABLE `bbs_colunm_article` (
  `id` char(19) NOT NULL COMMENT '专栏文章id',
  `column_id` char(19) NOT NULL COMMENT '专栏id',
  `article_id` char(19) NOT NULL COMMENT '文章id',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of bbs_colunm_article
-- ----------------------------
INSERT INTO `bbs_colunm_article` VALUES ('1508337868089651202', '1508008133925105665', '1508337868081262593', '1', '2022-03-28 14:59:05', '2022-03-28 14:59:05');
INSERT INTO `bbs_colunm_article` VALUES ('1508343665607585793', '1508008133925105665', '1508343665469173762', '1', '2022-03-28 15:22:08', '2022-03-28 15:22:08');
INSERT INTO `bbs_colunm_article` VALUES ('1508345364950822913', '1508008133925105665', '1508345364929851393', '1', '2022-03-28 15:28:53', '2022-03-28 15:28:53');

-- ----------------------------
-- Table structure for `bbs_comment`
-- ----------------------------
DROP TABLE IF EXISTS `bbs_comment`;
CREATE TABLE `bbs_comment` (
  `id` char(19) NOT NULL COMMENT '主键id',
  `article_id` char(19) NOT NULL COMMENT '文章id',
  `user_id` char(19) NOT NULL COMMENT '用户1id',
  `user_avatar` varchar(255) NOT NULL COMMENT '用户1头像',
  `user_nickname` varchar(50) NOT NULL COMMENT '用户1昵称',
  `content` varchar(255) NOT NULL COMMENT '评论内容',
  `father_id` char(19) DEFAULT '' COMMENT '父级id，0表示一级评论，非0为二级评论',
  `reply_user_id` char(19) DEFAULT NULL COMMENT '回复用户id',
  `reply_user_nickname` varchar(50) DEFAULT NULL COMMENT '回复用户昵称',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of bbs_comment
-- ----------------------------
INSERT INTO `bbs_comment` VALUES ('1496362333168070657', '1496359576990121985', '1489885385067622401', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '天天搬砖', '写的非常好，挺喜欢的', '', null, '', '1', '2022-03-06 14:38:35', '2022-03-06 14:38:35');
INSERT INTO `bbs_comment` VALUES ('1496375573893619714', '1496359576990121985', '1489885385067622401', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '天天搬砖', '多写点，我喜欢看', '', null, '', '1', '2022-03-06 14:38:35', '2022-03-06 14:38:35');
INSERT INTO `bbs_comment` VALUES ('1496375888260898817', '1496359576990121985', '1489885385067622401', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '天天搬砖', '挺好的', '', null, '', '1', '2022-03-06 14:38:35', '2022-03-06 14:38:35');
INSERT INTO `bbs_comment` VALUES ('1496376336355172353', '1496359576990121985', '1489885385067622401', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '天天搬砖', '嘿嘿嘿', '', null, '', '1', '2022-03-06 14:38:35', '2022-03-06 14:38:35');
INSERT INTO `bbs_comment` VALUES ('1496377114075611137', '1496359576990121985', '1489885385067622401', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '天天搬砖', '嗯，还可以', '', null, '', '1', '2022-03-06 14:38:35', '2022-03-06 14:38:35');
INSERT INTO `bbs_comment` VALUES ('1496377812515307522', '1496359576990121985', '1489885385067622401', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '天天搬砖', '不错不错', '', null, '', '1', '2022-03-06 14:38:35', '2022-03-06 14:38:35');
INSERT INTO `bbs_comment` VALUES ('1496379047381331969', '1496359576990121985', '1489885385067622401', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '天天搬砖', '针不戳', '', null, '', '1', '2022-03-06 14:38:35', '2022-03-06 14:38:35');
INSERT INTO `bbs_comment` VALUES ('1496387148058750977', '1496359576990121985', '1489885385067622401', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '天天搬砖', '真好', '', null, '', '1', '2022-03-06 14:38:35', '2022-03-06 14:38:35');
INSERT INTO `bbs_comment` VALUES ('1496387433804099586', '1496359576990121985', '1489885385067622401', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '天天搬砖', '真不错', '1496387148058750977', '1489885385067622401', '天天搬砖', '1', '2022-03-06 14:38:35', '2022-03-06 14:38:35');
INSERT INTO `bbs_comment` VALUES ('1496390598494547970', '1496359576990121985', '1496388556204023809', 'https://thirdwx.qlogo.cn/mmopen/vi_32/iaPp7ClZOGcpaPLXQeNSMPW3EWuiaJCxibjgic6gsWibBo8eibIRd89Jg0g5xjTe3ulc8crzoGLia3lQeKTGUxeAxsGMQ/132', '流逝', '写的可以', '', null, '', '1', '2022-03-06 14:38:35', '2022-03-06 14:38:35');
INSERT INTO `bbs_comment` VALUES ('1505122761796853762', '1505114107760623617', '1496388556204023809', 'https://thirdwx.qlogo.cn/mmopen/vi_32/iaPp7ClZOGcpaPLXQeNSMPW3EWuiaJCxibjgic6gsWibBo8eibIRd89Jg0g5xjTe3ulc8crzoGLia3lQeKTGUxeAxsGMQ/132', '流逝', '真不错', '', null, null, '1', '2022-03-19 18:03:24', '2022-03-19 18:03:24');
INSERT INTO `bbs_comment` VALUES ('1505123727497601026', '1505114107760623617', '1496388556204023809', 'https://thirdwx.qlogo.cn/mmopen/vi_32/iaPp7ClZOGcpaPLXQeNSMPW3EWuiaJCxibjgic6gsWibBo8eibIRd89Jg0g5xjTe3ulc8crzoGLia3lQeKTGUxeAxsGMQ/132', '流逝', '好', '1505122761796853762', '1496388556204023809', '流逝', '1', '2022-03-19 18:07:15', '2022-03-19 18:07:15');

-- ----------------------------
-- Table structure for `bbs_label`
-- ----------------------------
DROP TABLE IF EXISTS `bbs_label`;
CREATE TABLE `bbs_label` (
  `id` char(19) NOT NULL COMMENT '主键id',
  `article_id` char(19) NOT NULL COMMENT '文章id',
  `label_name` varchar(20) NOT NULL COMMENT '文章标签',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of bbs_label
-- ----------------------------
INSERT INTO `bbs_label` VALUES ('1497111405504032769', '1496359576990121985', '人生', '1', '2022-02-25 15:29:08', '2022-02-25 15:29:08');
INSERT INTO `bbs_label` VALUES ('1497111405650833410', '1496359576990121985', '职场', '1', '2022-02-25 15:29:08', '2022-02-25 15:29:08');
INSERT INTO `bbs_label` VALUES ('1497111405671804930', '1496359576990121985', 'Java', '1', '2022-02-25 15:29:08', '2022-02-25 15:29:08');
INSERT INTO `bbs_label` VALUES ('1505119047165947905', '1505114107760623617', 'redis', '1', '2022-03-19 17:48:39', '2022-03-19 17:48:39');
INSERT INTO `bbs_label` VALUES ('1505119047174336514', '1505114107760623617', '定时任务', '1', '2022-03-19 17:48:39', '2022-03-19 17:48:39');
INSERT INTO `bbs_label` VALUES ('1508343667474051073', '1508343665469173762', '微服务', '1', '2022-03-28 15:22:08', '2022-03-28 15:22:08');
INSERT INTO `bbs_label` VALUES ('1508343667486633986', '1508343665469173762', 'Spring Cloud Alibaba', '1', '2022-03-28 15:22:08', '2022-03-28 15:22:08');
INSERT INTO `bbs_label` VALUES ('1508343667490828289', '1508343665469173762', 'nacos', '1', '2022-03-28 15:22:08', '2022-03-28 15:22:08');
INSERT INTO `bbs_label` VALUES ('1508345365059874818', '1508345364929851393', '微服务', '1', '2022-03-28 15:28:53', '2022-03-28 15:28:53');
INSERT INTO `bbs_label` VALUES ('1508345365080846337', '1508345364929851393', 'sentinel', '1', '2022-03-28 15:28:53', '2022-03-28 15:28:53');
INSERT INTO `bbs_label` VALUES ('1508345365085040642', '1508345364929851393', 'Spring Cloud Alibaba', '1', '2022-03-28 15:28:53', '2022-03-28 15:28:53');
INSERT INTO `bbs_label` VALUES ('1508709110810488833', '1508337868081262593', '微服务', '1', '2022-03-29 15:34:17', '2022-03-29 15:34:17');
INSERT INTO `bbs_label` VALUES ('1508709110810488834', '1508337868081262593', 'Spring Cloud Alibaba', '1', '2022-03-29 15:34:17', '2022-03-29 15:34:17');
INSERT INTO `bbs_label` VALUES ('1508709110823071746', '1508337868081262593', 'AT模式', '1', '2022-03-29 15:34:17', '2022-03-29 15:34:17');
INSERT INTO `bbs_label` VALUES ('1508709110823071747', '1508337868081262593', '分布式事务', '1', '2022-03-29 15:34:17', '2022-03-29 15:34:17');
INSERT INTO `bbs_label` VALUES ('1508709110827266050', '1508337868081262593', 'seata', '1', '2022-03-29 15:34:17', '2022-03-29 15:34:17');

-- ----------------------------
-- Table structure for `bbs_report`
-- ----------------------------
DROP TABLE IF EXISTS `bbs_report`;
CREATE TABLE `bbs_report` (
  `id` char(19) NOT NULL COMMENT '主键id',
  `article_id` char(19) NOT NULL COMMENT '文章id',
  `content` varchar(255) NOT NULL COMMENT '举报内容',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of bbs_report
-- ----------------------------
INSERT INTO `bbs_report` VALUES ('1496360468229517313', '1496359576990121985', '涉及血腥', '1', '2022-02-23 13:45:11', '2022-02-23 13:45:11');
INSERT INTO `bbs_report` VALUES ('1505081883501793282', '1499243244901490690', '内容不好', '1', '2022-03-19 15:20:58', '2022-03-19 15:20:58');

-- ----------------------------
-- Table structure for `cms_bill`
-- ----------------------------
DROP TABLE IF EXISTS `cms_bill`;
CREATE TABLE `cms_bill` (
  `id` char(19) NOT NULL COMMENT '课程购买id',
  `user_id` char(19) NOT NULL COMMENT '用户id',
  `course_id` char(19) NOT NULL COMMENT '购买课程id',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of cms_bill
-- ----------------------------
INSERT INTO `cms_bill` VALUES ('1497803515974680578', '1489885385067622401', '1491337795770314754', '1', '2022-02-27 13:19:20', '2022-02-27 13:19:20');
INSERT INTO `cms_bill` VALUES ('1505074567826141185', '1496388556204023809', '1491337795770314754', '1', '2022-03-19 14:51:54', '2022-03-19 14:51:54');

-- ----------------------------
-- Table structure for `cms_chapter`
-- ----------------------------
DROP TABLE IF EXISTS `cms_chapter`;
CREATE TABLE `cms_chapter` (
  `id` char(19) NOT NULL COMMENT '章节id',
  `course_id` char(19) NOT NULL COMMENT '课程id',
  `sort` int(10) NOT NULL DEFAULT '0' COMMENT '排序字段',
  `title` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '章节标题',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_course_id` (`course_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='课程';

-- ----------------------------
-- Records of cms_chapter
-- ----------------------------
INSERT INTO `cms_chapter` VALUES ('1491680020786167810', '1491337795770314754', '1', '第一章：计算机发展历史', '1', '2022-02-10 15:46:45', '2022-02-10 15:46:45');

-- ----------------------------
-- Table structure for `cms_course`
-- ----------------------------
DROP TABLE IF EXISTS `cms_course`;
CREATE TABLE `cms_course` (
  `id` char(19) NOT NULL COMMENT '课程id',
  `oc_id` char(19) NOT NULL COMMENT '一级分类id',
  `tc_id` char(19) NOT NULL COMMENT '二级分类id',
  `sort` int(10) NOT NULL DEFAULT '0' COMMENT '排序字段',
  `title` varchar(50) NOT NULL COMMENT '课程标题',
  `description` varchar(50) NOT NULL COMMENT '课程描述',
  `cover` varchar(255) NOT NULL COMMENT '课程封面地址',
  `total_length` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '课程的总时长(以秒为单位)',
  `status` varchar(10) NOT NULL DEFAULT 'Draft' COMMENT '课程状态 Draft未更新完毕  Normal更新完毕',
  `views` bigint(10) unsigned NOT NULL DEFAULT '0' COMMENT '播放数量',
  `price` int(6) unsigned NOT NULL DEFAULT '0' COMMENT '课程销售价格，设置为0则可免费观看',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of cms_course
-- ----------------------------
INSERT INTO `cms_course` VALUES ('1491327961238880257', '1490979376278302721', '1491290961043628033', '1', '聊聊编程这条路', '编程到底该如何学习？', 'https://edu-2-0-2-1.oss-cn-hangzhou.aliyuncs.com/2022/02/09/9e80a63f38454d959ac4574037e1f3d5.jpg', '0', 'Normal', '0', '0', '1', '2022-02-09 16:27:48', '2022-02-09 16:27:48');
INSERT INTO `cms_course` VALUES ('1491337795770314754', '1490979376278302721', '1491290961043628033', '2', '预科阶段', '学习编程之前你要了解的知识', 'https://edu-2-0-2-1.oss-cn-hangzhou.aliyuncs.com/2022/02/09/428409ef41264d5380a940289af62716.jpg', '32', 'Normal', '2', '200', '1', '2022-02-09 17:06:52', '2022-03-15 19:30:00');

-- ----------------------------
-- Table structure for `cms_details`
-- ----------------------------
DROP TABLE IF EXISTS `cms_details`;
CREATE TABLE `cms_details` (
  `id` char(19) NOT NULL COMMENT '课程详情表id',
  `course_id` char(19) NOT NULL COMMENT '课程id',
  `overview` varchar(255) NOT NULL COMMENT '课程概述',
  `teacher` varchar(255) NOT NULL COMMENT '讲师介绍',
  `suitable_people` varchar(255) NOT NULL COMMENT '适合人群',
  `course_arrange` varchar(255) NOT NULL COMMENT '课程安排',
  `course_feedback` varchar(255) NOT NULL COMMENT '课程反馈',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of cms_details
-- ----------------------------
INSERT INTO `cms_details` VALUES ('1491652601819693058', '1491337795770314754', '在正式学习编程之前，我们要学习计算机发展历程，还有一些计算机基础以及一些常用的命令', '秦疆（遇见狂神说） Bilibili空间地址：https://space.bilibili.com/95256449', '编程零基础人员、初学者', '大家自己看下课程视频名字，见名知意', '大家可以向2980244187@qq.com发送邮件，返回课程', '1', '2022-02-10 13:57:48', '2022-02-10 13:57:48');

-- ----------------------------
-- Table structure for `cms_one_category`
-- ----------------------------
DROP TABLE IF EXISTS `cms_one_category`;
CREATE TABLE `cms_one_category` (
  `id` char(19) NOT NULL COMMENT '一级分类id',
  `title` varchar(50) NOT NULL COMMENT '一级分类标题',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of cms_one_category
-- ----------------------------
INSERT INTO `cms_one_category` VALUES ('1490979376026644481', '专题课', '1', '2022-02-08 17:22:38', '2022-02-08 17:22:38');
INSERT INTO `cms_one_category` VALUES ('1490979376278302721', 'Java全栈', '1', '2022-02-08 17:22:39', '2022-02-08 17:22:39');
INSERT INTO `cms_one_category` VALUES ('1490979376299274242', '前端全栈', '1', '2022-02-08 17:22:39', '2022-02-08 17:22:39');
INSERT INTO `cms_one_category` VALUES ('1490979376320245762', 'Go语言', '1', '2022-02-08 17:22:39', '2022-02-08 17:22:39');
INSERT INTO `cms_one_category` VALUES ('1490979376341217281', '大数据', '1', '2022-02-08 17:22:39', '2022-02-08 17:22:39');
INSERT INTO `cms_one_category` VALUES ('1490979376366383105', 'C/C++', '1', '2022-02-08 17:22:39', '2022-02-08 17:22:39');

-- ----------------------------
-- Table structure for `cms_study`
-- ----------------------------
DROP TABLE IF EXISTS `cms_study`;
CREATE TABLE `cms_study` (
  `id` char(19) NOT NULL COMMENT 'id',
  `user_id` char(19) NOT NULL COMMENT '用户id',
  `course_id` char(19) NOT NULL COMMENT '课程id',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cms_study
-- ----------------------------
INSERT INTO `cms_study` VALUES ('1506523061002727426', '1489885385067622401', '1491337795770314754', '1', '2022-03-23 14:47:42', '2022-03-23 14:47:56');

-- ----------------------------
-- Table structure for `cms_two_category`
-- ----------------------------
DROP TABLE IF EXISTS `cms_two_category`;
CREATE TABLE `cms_two_category` (
  `id` char(19) NOT NULL COMMENT '二级分类id',
  `oc_id` char(19) NOT NULL COMMENT '一级分类id',
  `sort` int(10) NOT NULL COMMENT '排序字段',
  `title` varchar(50) NOT NULL COMMENT '二级分类标题',
  `slide_title` varchar(50) NOT NULL COMMENT '侧边栏标题',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of cms_two_category
-- ----------------------------
INSERT INTO `cms_two_category` VALUES ('1491290961043628033', '1490979376278302721', '1', '第一阶段：JavaSE', 'Java基础', '1', '2022-02-09 14:00:46', '2022-02-09 14:00:46');
INSERT INTO `cms_two_category` VALUES ('1491290961182040066', '1490979376278302721', '2', '第二阶段：前端基础', '前端基础', '1', '2022-02-09 14:00:46', '2022-02-09 14:00:46');
INSERT INTO `cms_two_category` VALUES ('1491290961198817282', '1490979376278302721', '3', '第三阶段：MySQL', '数据库', '1', '2022-02-09 14:00:46', '2022-02-09 14:00:46');
INSERT INTO `cms_two_category` VALUES ('1491290961207205889', '1490979376278302721', '4', '第四阶段：JavaWeb', 'JavaWeb', '1', '2022-02-09 14:00:46', '2022-02-09 14:00:46');
INSERT INTO `cms_two_category` VALUES ('1491290961228177410', '1490979376278302721', '5', '第五阶段：SSM框架', 'SSM框架', '1', '2022-02-09 14:00:46', '2022-02-09 14:00:46');
INSERT INTO `cms_two_category` VALUES ('1491290961236566017', '1490979376278302721', '6', '第六阶段：大前端进阶', '大前端', '1', '2022-02-09 14:00:46', '2022-02-09 14:00:46');
INSERT INTO `cms_two_category` VALUES ('1491290961249148929', '1490979376278302721', '7', '第七阶段：微服务开发', '微服务', '1', '2022-02-09 14:00:46', '2022-02-09 14:00:46');
INSERT INTO `cms_two_category` VALUES ('1491290961265926145', '1490979376278302721', '8', '第八阶段：Linux运维', '运维', '1', '2022-02-09 14:00:46', '2022-02-09 14:00:46');
INSERT INTO `cms_two_category` VALUES ('1491290961282703361', '1490979376278302721', '9', '第九阶段：常用中间件', '中间件', '1', '2022-02-09 14:00:46', '2022-02-09 14:00:46');
INSERT INTO `cms_two_category` VALUES ('1491290961295286274', '1490979376278302721', '10', '第十阶段：企业常用第三方技术', '三方应用', '1', '2022-02-09 14:00:46', '2022-02-09 14:00:46');
INSERT INTO `cms_two_category` VALUES ('1491290961303674881', '1490979376278302721', '11', '第十一阶段：源码探究、设计模式学习', '源码探究', '1', '2022-02-09 14:00:46', '2022-02-09 14:00:46');
INSERT INTO `cms_two_category` VALUES ('1491290961316257794', '1490979376278302721', '12', '第十二阶段：走近企业、项目实战', '走近企业', '1', '2022-02-09 14:00:46', '2022-02-09 14:00:46');
INSERT INTO `cms_two_category` VALUES ('1491291908234854402', '1490979376026644481', '1', '优质精品课', '优质精品课', '1', '2022-02-09 14:04:32', '2022-02-09 14:04:32');
INSERT INTO `cms_two_category` VALUES ('1491293413641871361', '1490979376299274242', '1', '前端基础', '前端基础', '1', '2022-02-09 14:10:31', '2022-02-09 14:10:31');
INSERT INTO `cms_two_category` VALUES ('1491293413868363778', '1490979376299274242', '2', '前端进阶', '前端进阶', '1', '2022-02-09 14:10:31', '2022-02-09 14:10:31');
INSERT INTO `cms_two_category` VALUES ('1491293413880946689', '1490979376299274242', '3', '数据库入门', '数据库', '1', '2022-02-09 14:10:31', '2022-02-09 14:10:31');
INSERT INTO `cms_two_category` VALUES ('1491293413897723905', '1490979376299274242', '4', '走近大前端', '走近大前端', '1', '2022-02-09 14:10:31', '2022-02-09 14:10:31');
INSERT INTO `cms_two_category` VALUES ('1491293413910306818', '1490979376299274242', '5', '大前端框架', '大前端框架', '1', '2022-02-09 14:10:31', '2022-02-09 14:10:31');
INSERT INTO `cms_two_category` VALUES ('1491293413931278338', '1490979376299274242', '6', '移动端开发', '移动端开发', '1', '2022-02-09 14:10:31', '2022-02-09 14:10:31');
INSERT INTO `cms_two_category` VALUES ('1491293413939666946', '1490979376299274242', '7', '深入底层', '深入底层', '1', '2022-02-09 14:10:31', '2022-02-09 14:10:31');
INSERT INTO `cms_two_category` VALUES ('1491293413956444162', '1490979376299274242', '8', '技能提升', '技能提升', '1', '2022-02-09 14:10:31', '2022-02-09 14:10:31');
INSERT INTO `cms_two_category` VALUES ('1491293413977415682', '1490979376299274242', '9', '走近企业', '走近企业', '1', '2022-02-09 14:10:31', '2022-02-09 14:10:31');
INSERT INTO `cms_two_category` VALUES ('1491294885225967618', '1490979376320245762', '1', 'Go语言基础', '基础语法', '1', '2022-02-09 14:16:22', '2022-02-09 14:16:22');
INSERT INTO `cms_two_category` VALUES ('1491294885473431553', '1490979376320245762', '2', 'Go语言高级', '基础进阶', '1', '2022-02-09 14:16:22', '2022-02-09 14:16:22');
INSERT INTO `cms_two_category` VALUES ('1491294885486014465', '1490979376320245762', '3', '前端基础', '前端基础', '1', '2022-02-09 14:16:22', '2022-02-09 14:16:22');
INSERT INTO `cms_two_category` VALUES ('1491294885511180290', '1490979376320245762', '4', '数据库入门', '数据库', '1', '2022-02-09 14:16:22', '2022-02-09 14:16:22');
INSERT INTO `cms_two_category` VALUES ('1491294885523763201', '1490979376320245762', '5', '主流Web框架', 'Web开发', '1', '2022-02-09 14:16:22', '2022-02-09 14:16:22');
INSERT INTO `cms_two_category` VALUES ('1491294885553123329', '1490979376320245762', '6', '分布式开发', '微服务', '1', '2022-02-09 14:16:22', '2022-02-09 14:16:22');
INSERT INTO `cms_two_category` VALUES ('1491294885569900546', '1490979376320245762', '7', '第三方技术', '三方技术', '1', '2022-02-09 14:16:22', '2022-02-09 14:16:22');
INSERT INTO `cms_two_category` VALUES ('1491294885590872066', '1490979376320245762', '8', '走近运维', '运维', '1', '2022-02-09 14:16:22', '2022-02-09 14:16:22');
INSERT INTO `cms_two_category` VALUES ('1491294885620232194', '1490979376320245762', '9', '项目实战', '实战', '1', '2022-02-09 14:16:22', '2022-02-09 14:16:22');
INSERT INTO `cms_two_category` VALUES ('1491294885637009409', '1490979376320245762', '10', '走近企业', '走近企业', '1', '2022-02-09 14:16:22', '2022-02-09 14:16:22');
INSERT INTO `cms_two_category` VALUES ('1491295421539069953', '1490979376341217281', '1', '大数据学前班', '大数据学前', '1', '2022-02-09 14:18:30', '2022-02-09 14:18:30');
INSERT INTO `cms_two_category` VALUES ('1491295421807505409', '1490979376341217281', '2', '大数据基础', '大数据基础', '1', '2022-02-09 14:18:30', '2022-02-09 14:18:30');
INSERT INTO `cms_two_category` VALUES ('1491295421828476930', '1490979376341217281', '3', '分布式计算', '分布式计算', '1', '2022-02-09 14:18:30', '2022-02-09 14:18:30');
INSERT INTO `cms_two_category` VALUES ('1491295421845254145', '1490979376341217281', '4', '项目实战课程', '项目实战课', '1', '2022-02-09 14:18:30', '2022-02-09 14:18:30');
INSERT INTO `cms_two_category` VALUES ('1491296235993231361', '1490979376366383105', '1', '计算机基础', '计算机基础', '1', '2022-02-09 14:21:44', '2022-02-09 14:21:44');
INSERT INTO `cms_two_category` VALUES ('1491296236173586434', '1490979376366383105', '2', 'C语言学习', 'C语言', '1', '2022-02-09 14:21:44', '2022-02-09 14:21:44');
INSERT INTO `cms_two_category` VALUES ('1491296236194557954', '1490979376366383105', '3', 'C++教程', 'C++教程', '1', '2022-02-09 14:21:44', '2022-02-09 14:21:44');
INSERT INTO `cms_two_category` VALUES ('1491296236207140865', '1490979376366383105', '4', '数据库', '数据库', '1', '2022-02-09 14:21:44', '2022-02-09 14:21:44');
INSERT INTO `cms_two_category` VALUES ('1491296236223918082', '1490979376366383105', '5', 'Linux', 'Linux', '1', '2022-02-09 14:21:44', '2022-02-09 14:21:44');
INSERT INTO `cms_two_category` VALUES ('1491296236236500993', '1490979376366383105', '6', '嵌入式编程', '嵌入式', '1', '2022-02-09 14:21:44', '2022-02-09 14:21:44');
INSERT INTO `cms_two_category` VALUES ('1491296236257472513', '1490979376366383105', '7', '逆向与安全', '逆向安全', '1', '2022-02-09 14:21:44', '2022-02-09 14:21:44');

-- ----------------------------
-- Table structure for `cms_video`
-- ----------------------------
DROP TABLE IF EXISTS `cms_video`;
CREATE TABLE `cms_video` (
  `id` char(19) NOT NULL COMMENT '视频ID',
  `course_id` char(19) NOT NULL COMMENT '课程ID',
  `chapter_id` char(19) NOT NULL COMMENT '章节ID',
  `sort` int(10) NOT NULL DEFAULT '0' COMMENT '排序字段',
  `title` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '小节名称',
  `length` int(10) NOT NULL DEFAULT '0' COMMENT '视频时长(以秒为单位)',
  `video_source_id` varchar(100) CHARACTER SET utf8 NOT NULL COMMENT '云端视频资源',
  `video_original_name` varchar(100) CHARACTER SET utf8 NOT NULL COMMENT '原始文件名称',
  `version` bigint(20) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_course_id` (`course_id`) USING BTREE,
  KEY `idx_chapter_id` (`chapter_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='课程视频';

-- ----------------------------
-- Records of cms_video
-- ----------------------------
INSERT INTO `cms_video` VALUES ('1491680021012660225', '1491337795770314754', '1491680020786167810', '1', '第一节：计算机的起源', '16', '4c34efc61ebd4744b491f3f88e35b3c9', '6 - What If I Want to Move Faster.mp4', '1', '2022-02-10 15:46:45', '2022-02-10 15:46:45');
INSERT INTO `cms_video` VALUES ('1491680021033631745', '1491337795770314754', '1491680020786167810', '2', '第二节：计算机的发展', '16', 'b26ba0f48f424fda8c24f47da1091025', '6 - What If I Want to Move Faster.mp4', '1', '2022-02-10 15:46:45', '2022-02-10 15:46:45');

-- ----------------------------
-- Table structure for `dtm_category`
-- ----------------------------
DROP TABLE IF EXISTS `dtm_category`;
CREATE TABLE `dtm_category` (
  `id` char(19) NOT NULL COMMENT '主键id',
  `category_name` varchar(50) NOT NULL COMMENT '分类名',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of dtm_category
-- ----------------------------
INSERT INTO `dtm_category` VALUES ('1490001529158041602', 'vip课程', '1', '2022-02-06 00:37:02', '2022-02-06 00:37:02');
INSERT INTO `dtm_category` VALUES ('1490001529262899201', '夏金宇笔记', '1', '2022-02-06 00:37:02', '2022-02-06 00:37:02');
INSERT INTO `dtm_category` VALUES ('1490001529279676418', '张龙笔记', '1', '2022-02-06 00:37:02', '2022-02-06 00:37:02');

-- ----------------------------
-- Table structure for `dtm_file`
-- ----------------------------
DROP TABLE IF EXISTS `dtm_file`;
CREATE TABLE `dtm_file` (
  `id` char(19) NOT NULL COMMENT '主键id',
  `category_id` char(19) NOT NULL COMMENT '分类id',
  `name` varchar(100) NOT NULL COMMENT '文件名称',
  `size` char(10) NOT NULL COMMENT '文件大小',
  `price` int(6) NOT NULL DEFAULT '0' COMMENT '文件价格',
  `file_source_id` varchar(100) NOT NULL DEFAULT '' COMMENT '云端文件资源',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of dtm_file
-- ----------------------------
INSERT INTO `dtm_file` VALUES ('1490148900101722114', '1490001529158041602', 'SpringSecurity在微服务下面的使用', '54.9', '3000', '2022/02/06/654121bbf53f4e19bbafeed0926d578cSpringSecurity.zip', '1', '2022-02-06 10:22:38', '2022-02-06 10:22:38');
INSERT INTO `dtm_file` VALUES ('1490170517376839681', '1490001529279676418', 'RabbitMQ详解笔记', '77.4', '300', '2022/02/06/b55e0248358c4a91b8ed03944e2c9d27RabbitMQ.zip', '1', '2022-02-06 11:48:32', '2022-02-06 11:48:32');
INSERT INTO `dtm_file` VALUES ('1490171239535284226', '1490001529279676418', 'Nginx详解笔记', '94.4', '500', '2022/02/06/aa02e16a24f4403bbeb731559ec88523Nginx.zip', '1', '2022-02-06 11:51:24', '2022-02-06 11:51:24');
INSERT INTO `dtm_file` VALUES ('1490171618201231361', '1490001529279676418', 'SpringBoot详解笔记', '32.4', '900', '2022/02/06/056d9786ab11490d842390401de5c066SpringBoot.zip', '1', '2022-02-06 11:52:54', '2022-02-06 11:52:54');
INSERT INTO `dtm_file` VALUES ('1490172479656697857', '1490001529279676418', 'SpringCloudAlibaba详解笔记', '254', '1300', '2022/02/06/7deb1f0cbe0847e0a600da439969b49cSpringCloudAlibaba.zip', '1', '2022-02-06 11:56:19', '2022-02-06 11:56:19');
INSERT INTO `dtm_file` VALUES ('1490172905609347073', '1490001529262899201', 'SpringCloud详解笔记', '634', '1000', '2022/02/06/152829b2f0704e6bbf7af53137c8a6c7SpringCloud.zip', '1', '2022-02-06 11:58:01', '2022-02-06 11:58:01');

-- ----------------------------
-- Table structure for `info_course`
-- ----------------------------
DROP TABLE IF EXISTS `info_course`;
CREATE TABLE `info_course` (
  `id` char(19) NOT NULL COMMENT '主键id',
  `user_id` char(19) NOT NULL COMMENT '用户id（该消息属于那个用户)',
  `course_id` char(19) NOT NULL COMMENT '课程id',
  `is_read` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否阅读 0 未阅读 1阅读',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of info_course
-- ----------------------------
INSERT INTO `info_course` VALUES ('1501835628340371457', '1489885385067622401', '1491327961238880257', '1', '1', '2022-03-10 16:21:31', '2022-03-10 16:21:31');
INSERT INTO `info_course` VALUES ('1501835628478783489', '1489885385067622401', '1491337795770314754', '1', '1', '2022-03-10 16:21:31', '2022-03-10 16:21:31');

-- ----------------------------
-- Table structure for `info_friend_feed`
-- ----------------------------
DROP TABLE IF EXISTS `info_friend_feed`;
CREATE TABLE `info_friend_feed` (
  `id` char(19) NOT NULL COMMENT '主键id',
  `user_id` char(19) NOT NULL COMMENT '用户id（该消息属于那个用户)',
  `article_id` char(19) NOT NULL COMMENT '文章id',
  `title` varchar(50) NOT NULL COMMENT '文章标题',
  `description` varchar(50) NOT NULL COMMENT '文章描述',
  `attation_user_id` char(19) NOT NULL COMMENT '用户2id(你关注的用户的id)',
  `attation_user_nickname` varchar(50) NOT NULL COMMENT '用户2昵称',
  `attation_user_avatar` varchar(255) NOT NULL COMMENT '用户2用户头像',
  `category_name` varchar(20) NOT NULL COMMENT '文章分类名',
  `is_read` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否阅读 0 未阅读 1阅读',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of info_friend_feed
-- ----------------------------
INSERT INTO `info_friend_feed` VALUES ('1499249101668130817', '1496388556204023809', '1499243244901490690', 'Feign远程调用时创建请求头注入token', 'Feign远程调用时创建请求头注入token', '1489885385067622401', '天天搬砖', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '踩坑记录', '1', '1', '2022-03-03 13:03:35', '2022-03-03 13:03:35');
INSERT INTO `info_friend_feed` VALUES ('1505114109144748033', '1496388556204023809', '1505114107760623617', 'redis解决分布式下面定时任务的重复执行', 'redis解决分布式下面定时任务的重复执行', '1489885385067622401', '天天搬砖', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '后台', '1', '1', '2022-03-19 17:29:01', '2022-03-19 17:29:01');
INSERT INTO `info_friend_feed` VALUES ('1508337872149762050', '1496388556204023809', '1508337868081262593', '微服务使用seata进行事务控制', '微服务使用seata进行事务控制', '1489885385067622401', '天天搬砖', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '后台', '1', '1', '2022-03-28 14:59:06', '2022-03-28 14:59:06');
INSERT INTO `info_friend_feed` VALUES ('1508343667637702657', '1496388556204023809', '1508343665469173762', '微服务模块注册中心--nacos', '微服务模块注册中心--nacos', '1489885385067622401', '天天搬砖', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '后台', '1', '1', '2022-03-28 15:22:08', '2022-03-28 15:22:08');
INSERT INTO `info_friend_feed` VALUES ('1508344130885996546', '1496388556204023809', '1508344130655236098', '微服务模块配置中心--nacos', '微服务模块配置中心--nacos', '1489885385067622401', '天天搬砖', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '后台', '1', '1', '2022-03-28 15:23:59', '2022-03-28 15:23:59');
INSERT INTO `info_friend_feed` VALUES ('1508345365131251713', '1496388556204023809', '1508345364929851393', '流量防卫兵Sentinel', '流量防卫兵Sentinel', '1489885385067622401', '天天搬砖', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '后台', '1', '1', '2022-03-28 15:28:53', '2022-03-28 15:28:53');

-- ----------------------------
-- Table structure for `info_my_news`
-- ----------------------------
DROP TABLE IF EXISTS `info_my_news`;
CREATE TABLE `info_my_news` (
  `id` char(19) NOT NULL COMMENT '主键id',
  `user_id` char(19) NOT NULL COMMENT '用户id（该消息属于那个用户)',
  `content` varchar(255) NOT NULL COMMENT '消息内容',
  `is_course` tinyint(1) NOT NULL COMMENT '是不是课程通知，0不是，1是',
  `title` varchar(50) NOT NULL COMMENT '消息标题',
  `course_id` char(19) NOT NULL COMMENT '主键id',
  `is_read` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否阅读 0 未阅读 1阅读',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of info_my_news
-- ----------------------------
INSERT INTO `info_my_news` VALUES ('1505076826500780034', '1496388556204023809', '尊敬的用户,课程预科阶段购买成功', '1', '课程购买通知', '1491337795770314754', '1', '1', '2022-03-19 15:00:53', '2022-03-19 15:00:53');

-- ----------------------------
-- Table structure for `info_reply_me`
-- ----------------------------
DROP TABLE IF EXISTS `info_reply_me`;
CREATE TABLE `info_reply_me` (
  `id` char(19) NOT NULL COMMENT '主键id',
  `user_id` char(19) NOT NULL COMMENT '用户id',
  `article_id` char(19) NOT NULL COMMENT '文章id',
  `title` varchar(50) NOT NULL COMMENT '文章标题',
  `reply_user_id` char(19) NOT NULL COMMENT '用户2id',
  `reply_user_nickname` varchar(50) NOT NULL COMMENT '用户2昵称',
  `reply_user_avatar` varchar(255) NOT NULL COMMENT '用户2用户头像',
  `content` varchar(255) NOT NULL COMMENT '评论内容',
  `is_read` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否阅读 0 未阅读 1阅读',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of info_reply_me
-- ----------------------------
INSERT INTO `info_reply_me` VALUES ('1496390831865716738', '1489885385067622401', '1496359576990121985', '我的大学历程', '1496388556204023809', '流逝', 'https://thirdwx.qlogo.cn/mmopen/vi_32/iaPp7ClZOGcpaPLXQeNSMPW3EWuiaJCxibjgic6gsWibBo8eibIRd89Jg0g5xjTe3ulc8crzoGLia3lQeKTGUxeAxsGMQ/132', '写的可以', '1', '1', '2022-02-23 15:45:50', '2022-02-23 15:45:50');
INSERT INTO `info_reply_me` VALUES ('1501819756485820417', '1496388556204023809', '1496359576990121985', '我的大学历程', '1489885385067622401', '天天搬砖', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '好的', '1', '1', '2022-03-10 15:18:27', '2022-03-10 15:18:27');
INSERT INTO `info_reply_me` VALUES ('1505038933128429570', '1496388556204023809', '1496359576990121985', '我的大学历程', '1489885385067622401', '天天搬砖', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '谢谢', '1', '1', '2022-03-19 12:30:18', '2022-03-19 12:30:18');
INSERT INTO `info_reply_me` VALUES ('1505122762237169666', '1489885385067622401', '1505114107760623617', 'redis解决分布式下面定时任务的重复执行', '1496388556204023809', '流逝', 'https://thirdwx.qlogo.cn/mmopen/vi_32/iaPp7ClZOGcpaPLXQeNSMPW3EWuiaJCxibjgic6gsWibBo8eibIRd89Jg0g5xjTe3ulc8crzoGLia3lQeKTGUxeAxsGMQ/132', '真不错', '0', '1', '2022-03-19 18:03:25', '2022-03-19 18:03:25');
INSERT INTO `info_reply_me` VALUES ('1508337171713576961', '1496388556204023809', '1505114107760623617', 'redis解决分布式下面定时任务的重复执行', '1489885385067622401', '天天搬砖', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '好', '1', '1', '2022-03-28 14:56:19', '2022-03-28 14:56:19');

-- ----------------------------
-- Table structure for `info_system`
-- ----------------------------
DROP TABLE IF EXISTS `info_system`;
CREATE TABLE `info_system` (
  `id` char(19) NOT NULL COMMENT '主键id',
  `user_id` char(19) NOT NULL COMMENT '用户id（该消息属于那个用户)',
  `title` varchar(50) NOT NULL COMMENT '消息标题',
  `content` varchar(255) NOT NULL COMMENT '消息内容',
  `is_read` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否阅读 0 未阅读 1阅读',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of info_system
-- ----------------------------

-- ----------------------------
-- Table structure for `user_attention`
-- ----------------------------
DROP TABLE IF EXISTS `user_attention`;
CREATE TABLE `user_attention` (
  `id` char(19) NOT NULL COMMENT '用户关注id',
  `user_id` char(19) NOT NULL COMMENT '用户id',
  `attention_user_id` char(19) NOT NULL COMMENT '用户id,这里用户1关注用户2',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of user_attention
-- ----------------------------
INSERT INTO `user_attention` VALUES ('1500387482615726082', '1496388556204023809', '1489885385067622401', '1', '2022-03-06 16:27:06', '2022-03-06 16:27:06');

-- ----------------------------
-- Table structure for `user_background`
-- ----------------------------
DROP TABLE IF EXISTS `user_background`;
CREATE TABLE `user_background` (
  `id` char(19) NOT NULL COMMENT 'id',
  `url` varchar(255) NOT NULL,
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_background
-- ----------------------------
INSERT INTO `user_background` VALUES ('1502147797284098050', 'https://edu-2-0-2-1.oss-cn-hangzhou.aliyuncs.com/2022/03/11/e1c0c724897d42eeb0c826f2e3757507.jpg', '1', '2022-03-11 13:01:58', '2022-03-11 13:01:58');
INSERT INTO `user_background` VALUES ('1502147797435092994', 'https://edu-2-0-2-1.oss-cn-hangzhou.aliyuncs.com/2022/03/11/eff849a37cca4e698d219269882dbb41.jpg', '1', '2022-03-11 13:01:58', '2022-03-11 13:01:58');
INSERT INTO `user_background` VALUES ('1502147797451870210', 'https://edu-2-0-2-1.oss-cn-hangzhou.aliyuncs.com/2022/03/11/32bc7c02d98b43cb90e9cf48e78a52fa.jpg', '1', '2022-03-11 13:01:58', '2022-03-11 13:01:58');
INSERT INTO `user_background` VALUES ('1502147797464453122', 'https://edu-2-0-2-1.oss-cn-hangzhou.aliyuncs.com/2022/03/11/243f2c59e0464b4ea2b5dfbc1d902e35.jpg', '1', '2022-03-11 13:01:58', '2022-03-11 13:01:58');
INSERT INTO `user_background` VALUES ('1502147797477036034', 'https://edu-2-0-2-1.oss-cn-hangzhou.aliyuncs.com/2022/03/11/d27cfbc9edb0472e95a36bf58f19e462.jpg', '1', '2022-03-11 13:01:58', '2022-03-11 13:01:58');

-- ----------------------------
-- Table structure for `user_head_portrait`
-- ----------------------------
DROP TABLE IF EXISTS `user_head_portrait`;
CREATE TABLE `user_head_portrait` (
  `id` char(19) NOT NULL COMMENT 'id',
  `url` varchar(255) NOT NULL,
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_head_portrait
-- ----------------------------
INSERT INTO `user_head_portrait` VALUES ('1500691783955267586', 'https://edu-2-0-2-1.oss-cn-hangzhou.aliyuncs.com/2022/03/07/d376e815cb7e4f5fad9651da2291195a.jpg', '1', '2022-03-07 12:36:17', '2022-03-07 12:36:17');
INSERT INTO `user_head_portrait` VALUES ('1500691784106262529', 'https://edu-2-0-2-1.oss-cn-hangzhou.aliyuncs.com/2022/03/07/10ff7773683e4b6d8c45c88bb72876e4.jpg', '1', '2022-03-07 12:36:17', '2022-03-07 12:36:17');
INSERT INTO `user_head_portrait` VALUES ('1500691784118845442', 'https://edu-2-0-2-1.oss-cn-hangzhou.aliyuncs.com/2022/03/07/5aad95d2b9a6465f8cd3c042a7585d9a.jpg', '1', '2022-03-07 12:36:17', '2022-03-07 12:36:17');
INSERT INTO `user_head_portrait` VALUES ('1500691784131428353', 'https://edu-2-0-2-1.oss-cn-hangzhou.aliyuncs.com/2022/03/07/0440974417e841f38caf6bdc7d08b45a.jpg', '1', '2022-03-07 12:36:17', '2022-03-07 12:36:17');
INSERT INTO `user_head_portrait` VALUES ('1500691784148205570', 'https://edu-2-0-2-1.oss-cn-hangzhou.aliyuncs.com/2022/03/07/58837f0c00614ec1837c55546a6692b1.jpg', '1', '2022-03-07 12:36:17', '2022-03-07 12:36:17');

-- ----------------------------
-- Table structure for `user_homepage`
-- ----------------------------
DROP TABLE IF EXISTS `user_homepage`;
CREATE TABLE `user_homepage` (
  `id` char(19) NOT NULL COMMENT '用户id,这里的id是用户的id',
  `content` text COMMENT '用户主页内容',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of user_homepage
-- ----------------------------
INSERT INTO `user_homepage` VALUES ('1489885385067622401', '你的斑驳，与众不同', '2', '2022-03-07 12:51:21', '2022-03-22 16:02:53');

-- ----------------------------
-- Table structure for `user_info`
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` char(19) NOT NULL COMMENT '用户id',
  `openid` varchar(128) CHARACTER SET utf8 NOT NULL COMMENT '微信openid',
  `account` char(11) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户账号',
  `password` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户密码',
  `nickname` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '用户昵称',
  `avatar` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT '用户头像',
  `sex` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '性别 0 女，1 男',
  `experience` int(6) NOT NULL DEFAULT '0' COMMENT '用户等级经验',
  `sign` varchar(100) CHARACTER SET utf8 NOT NULL DEFAULT 'TA很懒,什么都没写...' COMMENT '用户签名',
  `bg_img` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT '用户背景图片',
  `money` int(6) NOT NULL DEFAULT '0' COMMENT '用户k币数量',
  `address` varchar(50) CHARACTER SET utf8 NOT NULL DEFAULT '中国' COMMENT '用户地址',
  `email` varchar(30) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户邮箱',
  `is_disabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否禁用 0（false）未禁用，  1（true）禁用',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  `is_sign` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0未签到 1签到',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='会员表';

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES ('1489885385067622401', 'o3_SC5zIKO9GhTCthrP55dkZJfjw', '18154046678', 'e10adc3949ba59abbe56e057f20f883e', '天天搬砖', 'https://thirdwx.qlogo.cn/mmopen/vi_32/07VIZwpL6UkSTYaCUf2krXZs3FKtTIQnXNWjDhH8LRCPia62Dss7gTI4hFEGJ59w9eCUIjiaUzvR1vpa02lrmic1g/132', '0', '200', 'TA很懒,什么都没写...', 'https://edu-2-0-2-1.oss-cn-hangzhou.aliyuncs.com/2022/03/11/d27cfbc9edb0472e95a36bf58f19e462.jpg', '20970', '中国', '2980244187@qq.com', '0', '41', '2022-02-05 16:55:31', '2022-03-28 15:28:53', '0');
INSERT INTO `user_info` VALUES ('1496388556204023809', 'o3_SC5-_lDImPRD6qzi9Y56o-RDE', '00000002', 'e10adc3949ba59abbe56e057f20f883e', '流逝', 'https://thirdwx.qlogo.cn/mmopen/vi_32/iaPp7ClZOGcpaPLXQeNSMPW3EWuiaJCxibjgic6gsWibBo8eibIRd89Jg0g5xjTe3ulc8crzoGLia3lQeKTGUxeAxsGMQ/132', '0', '0', 'TA很懒,什么都没写...', 'https://edu-2-0-2-1.oss-cn-hangzhou.aliyuncs.com/2022/03/11/e1c0c724897d42eeb0c826f2e3757507.jpg', '96820', '中国', '', '0', '3', '2022-02-23 15:36:48', '2022-03-19 14:51:54', '0');

-- ----------------------------
-- Table structure for `user_talk`
-- ----------------------------
DROP TABLE IF EXISTS `user_talk`;
CREATE TABLE `user_talk` (
  `id` char(19) NOT NULL COMMENT '用户说说id',
  `user_id` char(19) NOT NULL COMMENT '用户id',
  `is_public` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0表示不公开，1表示公开',
  `content` varchar(255) NOT NULL COMMENT '用户说说',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of user_talk
-- ----------------------------
INSERT INTO `user_talk` VALUES ('1506505616904957953', '1489885385067622401', '0', '今天心情不错', '1', '2022-03-23 13:38:23', '2022-03-23 13:52:22');
INSERT INTO `user_talk` VALUES ('1506509528877793282', '1489885385067622401', '1', '今天好开心', '1', '2022-03-23 13:53:55', '2022-03-23 13:53:55');

-- ----------------------------
-- Table structure for `vip_members`
-- ----------------------------
DROP TABLE IF EXISTS `vip_members`;
CREATE TABLE `vip_members` (
  `id` char(19) NOT NULL COMMENT 'id',
  `user_id` char(19) NOT NULL COMMENT '用户id',
  `rights_id` char(19) NOT NULL COMMENT 'vip权益id',
  `expiration_time` datetime NOT NULL COMMENT '过期时间',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of vip_members
-- ----------------------------
INSERT INTO `vip_members` VALUES ('1490929192081084418', '1489885385067622401', '1490598728350838786', '2122-01-15 14:03:14', '1', '2022-02-08 14:03:14', '2022-02-08 14:03:14');
INSERT INTO `vip_members` VALUES ('1505063145398337537', '1496388556204023809', '1490597958297669634', '2022-04-18 14:06:31', '1', '2022-03-19 14:06:31', '2022-03-19 14:06:31');

-- ----------------------------
-- Table structure for `vip_rights`
-- ----------------------------
DROP TABLE IF EXISTS `vip_rights`;
CREATE TABLE `vip_rights` (
  `id` char(19) NOT NULL COMMENT '主键id',
  `vip_level` varchar(10) NOT NULL COMMENT 'vip标识',
  `price` int(6) NOT NULL DEFAULT '0' COMMENT '价格',
  `course_discount` double(3,2) NOT NULL COMMENT '课程打折多少',
  `column_number` int(2) NOT NULL COMMENT '专栏数量',
  `sign_experience` int(6) NOT NULL COMMENT '每日签到经验',
  `money` int(2) NOT NULL COMMENT '每日k币上限',
  `article_number` int(2) NOT NULL COMMENT '每日发帖数量，-1表示无限',
  `time_length` int(5) NOT NULL COMMENT 'vip时长',
  `version` bigint(20) NOT NULL DEFAULT '1' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of vip_rights
-- ----------------------------
INSERT INTO `vip_rights` VALUES ('1490591255682473986', '', '0', '1.00', '20', '30', '30', '2', '0', '1', '2022-02-07 15:40:23', '2022-02-07 15:40:23');
INSERT INTO `vip_rights` VALUES ('1490597958297669634', 'vip', '3000', '0.90', '20', '40', '50', '6', '30', '1', '2022-02-07 16:07:01', '2022-02-07 16:07:01');
INSERT INTO `vip_rights` VALUES ('1490598394593280001', 'svip', '30000', '0.80', '20', '40', '50', '10', '365', '1', '2022-02-07 16:08:45', '2022-02-07 16:08:45');
INSERT INTO `vip_rights` VALUES ('1490598728350838786', 'zVip', '300000', '0.70', '20', '40', '50', '20', '36500', '1', '2022-02-07 16:10:05', '2022-02-07 16:10:05');
