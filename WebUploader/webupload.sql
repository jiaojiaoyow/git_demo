/*
Navicat MySQL Data Transfer

Source Server         : localhost_1234
Source Server Version : 50722
Source Host           : localhost:1234
Source Database       : webupload

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2019-10-08 15:55:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) NOT NULL,
  `file_path` varchar(255) NOT NULL,
  `file_size` double(255,0) NOT NULL,
  `md5value` varchar(255) NOT NULL COMMENT '文件md5码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of file
-- ----------------------------

-- ----------------------------
-- Table structure for img
-- ----------------------------
DROP TABLE IF EXISTS `img`;
CREATE TABLE `img` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `img_name` varchar(255) DEFAULT NULL,
  `img_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=281 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of img
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `file_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
