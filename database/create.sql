/*
SQLyog Ultimate v11.01 (64 bit)
MySQL - 5.5.28 : Database - oilmap
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`oilmap` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `oilmap`;

/*Table structure for table `driving_trace` */

DROP TABLE IF EXISTS `driving_trace`;

CREATE TABLE `driving_trace` (
  `userPn` int(11) NOT NULL COMMENT '유저고유번호',
  `distance` int(11) NOT NULL COMMENT '이동한 거리',
  `quantitiy` int(11) NOT NULL COMMENT '사용한 기름양',
  `accelerate` int(11) NOT NULL COMMENT '급가속횟수',
  `start` int(11) NOT NULL COMMENT '급출발횟수',
  `stop` int(11) NOT NULL COMMENT '급정지횟수',
  `startTime` datetime NOT NULL COMMENT '시작시간',
  `endTime` datetime NOT NULL COMMENT '끝시간',
  KEY `userPn` (`userPn`),
  CONSTRAINT `driving_trace_ibfk_1` FOREIGN KEY (`userPn`) REFERENCES `user` (`pn`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='이동량 측정';

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `pn` int(11) NOT NULL AUTO_INCREMENT COMMENT '유저고유번호',
  `username` varchar(20) NOT NULL COMMENT '유저명',
  `password` varchar(16) NOT NULL COMMENT '비밀번호',
  `email` varchar(100) NOT NULL COMMENT '이메일주소',
  `joinDate` datetime NOT NULL COMMENT '가입날짜',
  `updateDate` datetime NOT NULL COMMENT '수정날짜',
  PRIMARY KEY (`pn`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='사용자 개인 정보\r\n';

/*Table structure for table `user_fuel` */

DROP TABLE IF EXISTS `user_fuel`;

CREATE TABLE `user_fuel` (
  `userPn` int(11) NOT NULL COMMENT '사용자 고유번호',
  `displacement` int(6) NOT NULL COMMENT '배기량',
  `cost` int(11) NOT NULL COMMENT '유류비용',
  `period` int(11) NOT NULL COMMENT '유류주기',
  `updateDate` datetime NOT NULL COMMENT '업데이트 날짜',
  PRIMARY KEY (`period`),
  KEY `userPn` (`userPn`),
  CONSTRAINT `user_fuel_ibfk_1` FOREIGN KEY (`userPn`) REFERENCES `user` (`pn`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='유저 유류 정보';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
