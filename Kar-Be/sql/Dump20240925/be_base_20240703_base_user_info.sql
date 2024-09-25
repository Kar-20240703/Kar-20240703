-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: karopendev.top    Database: be_base_20240703
-- ------------------------------------------------------
-- Server version	8.4.2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `base_user_info`
--

DROP TABLE IF EXISTS `base_user_info`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_user_info`
(
    `id`               bigint                                                        NOT NULL COMMENT '用户主键 id',
    `uuid`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '该用户的 uuid，本系统使用 id，不使用此字段（uuid），备注：不能重复',
    `nickname`         varchar(50)                                                   NOT NULL COMMENT '昵称',
    `bio`              varchar(100)                                                  NOT NULL COMMENT '个人简介',
    `avatar_file_id`   bigint                                                        NOT NULL COMMENT '头像 fileId（文件主键 id）',
    `sign_up_type`     int                                                           NOT NULL COMMENT '注册终端',
    `last_active_time` datetime                                                      NOT NULL COMMENT '最近活跃时间',
    `last_ip`          varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '最近 ip',
    `last_region`      varchar(200)                                                  NOT NULL COMMENT '最近 ip所处区域',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='v20240703：子表：用户基本信息表，主表：用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `base_user_info`
--

LOCK TABLES `base_user_info` WRITE;
/*!40000 ALTER TABLE `base_user_info`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `base_user_info`
    ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2024-09-25 16:39:21
