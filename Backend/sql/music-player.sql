/*
 Navicat Premium Data Transfer

 Source Server         : music-player
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Schema         : music-player

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 11/03/2022 00:29:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for list
-- ----------------------------
DROP TABLE IF EXISTS `list`;
CREATE TABLE `list`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `cover` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `list_id_uindex`(`id` ASC) USING BTREE,
  UNIQUE INDEX `list_un`(`user_id` ASC, `name` ASC) USING BTREE,
  CONSTRAINT `list_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for list_item
-- ----------------------------
DROP TABLE IF EXISTS `list_item`;
CREATE TABLE `list_item`  (
  `list_id` int NOT NULL,
  `song_id` bigint NOT NULL,
  PRIMARY KEY (`list_id`, `song_id`) USING BTREE,
  INDEX `list_item_song_id_fk`(`song_id` ASC) USING BTREE,
  CONSTRAINT `list_item_list_id_fk` FOREIGN KEY (`list_id`) REFERENCES `list` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `list_item_song_id_fk` FOREIGN KEY (`song_id`) REFERENCES `song` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for song
-- ----------------------------
DROP TABLE IF EXISTS `song`;
CREATE TABLE `song`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `artist` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `avatar` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `platform` int NOT NULL COMMENT '0 for local, 1 for netease, 2 for bili, 3 for youtube',
  `is_cache` tinyint(1) NOT NULL DEFAULT 0,
  `serial` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'local: local url\nnetease: id\nbili: BV serial\nyoutube: id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `song_id_uindex`(`id` ASC) USING BTREE,
  UNIQUE INDEX `song_ps`(`platform` ASC, `serial` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 909 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `salt` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id_uindex`(`id` ASC) USING BTREE,
  UNIQUE INDEX `user_username_uindex`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_cookie
-- ----------------------------
DROP TABLE IF EXISTS `user_cookie`;
CREATE TABLE `user_cookie`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `platform` int NOT NULL,
  `data` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_cookie_id_uindex`(`id` ASC) USING BTREE,
  INDEX `user_cookie_user_id_fk`(`user_id` ASC) USING BTREE,
  CONSTRAINT `user_cookie_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
