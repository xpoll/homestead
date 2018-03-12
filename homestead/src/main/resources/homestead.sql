/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50709
Source Host           : 127.0.0.1:3306
Source Database       : homestead

Target Server Type    : MYSQL
Target Server Version : 50709
File Encoding         : 65001

Date: 2017-12-17 22:51:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `nick` varchar(255) NOT NULL DEFAULT '' COMMENT '昵称',
  `avatar` varchar(255) NOT NULL COMMENT '头像',
  `level` tinyint(4) NOT NULL DEFAULT '0' COMMENT '等级(基于活跃天)',
  `active_day` int(11) NOT NULL DEFAULT '0' COMMENT '活跃天(10倍)',
  `gender` tinyint(1) NOT NULL DEFAULT '0' COMMENT '性别(0未知1男2女)',
  `money` bigint(20) NOT NULL DEFAULT '0' COMMENT '家园币',
  `sign_date` date DEFAULT NULL COMMENT '最后签到日期',
  `login_time` datetime DEFAULT NULL COMMENT '最后登陆时间',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态(0不可用1可用)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- Records of tb_user
-- ----------------------------

-- ----------------------------
-- Table structure for tb_user_extra
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_extra`;
CREATE TABLE `tb_user_extra` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `school` varchar(50) NOT NULL DEFAULT '' COMMENT '学校',
  `birthday` date NOT NULL DEFAULT '1900-01-01' COMMENT '生日',
  `city` varchar(50) NOT NULL DEFAULT '' COMMENT '城市',
  `constellation` varchar(10) NOT NULL DEFAULT '' COMMENT '星座(基于生日)',
  `description` varchar(300) NOT NULL DEFAULT '' COMMENT '个人简介',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态(0不可用1可用)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- ----------------------------
-- Records of tb_user_extra
-- ----------------------------

-- ----------------------------
-- Table structure for tb_user_third
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_third`;
CREATE TABLE `tb_user_third` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `channel_id` tinyint(1) NOT NULL COMMENT '渠道ID 枚举',
  `username` varchar(64) NOT NULL COMMENT '账号',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `extra_json` varchar(500) NOT NULL DEFAULT '{}' COMMENT '其他json数据',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态(0不可用1可用)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='第三方绑定表';

-- ----------------------------
-- Records of tb_user_third
-- ----------------------------
