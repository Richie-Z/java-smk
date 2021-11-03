/*
Navicat MySQL Data Transfer

Source Server         : LOKAL
Source Server Version : 100134
Source Host           : localhost:3306
Source Database       : latihanjavadatabase

Target Server Type    : MYSQL
Target Server Version : 100134
File Encoding         : 65001

Date: 2018-12-17 14:32:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for karyawan
-- ----------------------------
DROP TABLE IF EXISTS `karyawan`;
CREATE TABLE `karyawan` (
  `nik` varchar(20) NOT NULL,
  `nama` varchar(40) NOT NULL,
  `jabatan` varchar(40) NOT NULL,
  `alamat` varchar(100) NOT NULL,
  `email` varchar(60) NOT NULL,
  `no_telp` varchar(20) NOT NULL,
  PRIMARY KEY (`nik`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of karyawan
-- ----------------------------
INSERT INTO `karyawan` VALUES ('17030001', 'Dadung Awuk', 'IT Engineer', 'Jl. Gunung Bungkuk Kota Bengkulu', 'dadungawukstd@gmail.com', '0813733979xx');
