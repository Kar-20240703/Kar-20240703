-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: 192.168.33.12    Database: be_base_20240703
-- ------------------------------------------------------
-- Server version	8.0.27

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
-- Table structure for table `base_area`
--

DROP TABLE IF EXISTS `base_area`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_area`
(
    `id`          bigint                                                        NOT NULL,
    `create_id`   bigint                                                        NOT NULL,
    `create_time` datetime                                                      NOT NULL,
    `update_id`   bigint                                                        NOT NULL,
    `update_time` datetime                                                      NOT NULL,
    `enable_flag` tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`      varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '备注',
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '区域名',
    `pid`         bigint                                                        NOT NULL COMMENT '父节点id（顶级则为0）',
    `order_no`    int                                                           NOT NULL COMMENT '排序号（值越大越前面，默认为 0）',
    `uuid`        varchar(32)                                                   NOT NULL COMMENT '该菜单的 uuid，用于：同步租户菜单等操作，备注：不能重复',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240703：主表：区域表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `base_area`
--

LOCK TABLES `base_area` WRITE;
/*!40000 ALTER TABLE `base_area`
    DISABLE KEYS */;
INSERT INTO `base_area`
VALUES (240912111901006052, 0, '2024-09-12 11:19:01', 0, '2024-09-12 14:15:58', 1, '', '地球', 0,
        10000, '504841d4b6c84889961c307b977ab58b'),
       (240912111910006053, 0, '2024-09-12 11:19:11', 0, '2024-09-12 14:10:02', 1, '', '中国',
        240912111901006052, 10000, 'f7e06bdb5ede49d4ac9f28b4cc52bdd7'),
       (240912111927006054, 0, '2024-09-12 11:19:28', 0, '2024-09-12 14:10:02', 1, '', '美国',
        240912111901006052, 9900, '1fe958edb5f3460ba1fa1733e8b98839'),
       (240912111946006055, 0, '2024-09-12 11:19:47', 0, '2024-09-12 14:10:02', 1, '', '北京',
        240912111910006053, 10000, '5693a2bbde684df9a4d653f209f02c47'),
       (240912111954006056, 0, '2024-09-12 11:19:54', 0, '2024-09-12 14:10:02', 1, '', '纽约',
        240912111927006054, 10000, '87877183d5cf4a18973700f0a2d6543c'),
       (240912112022006057, 0, '2024-09-12 11:20:22', 0, '2024-09-12 14:10:02', 1, '', '洛杉矶',
        240912111927006054, 9900, '11116b4ca1b4441089181ab481f035bd'),
       (240912112028006058, 0, '2024-09-12 11:20:28', 0, '2024-09-12 14:10:02', 1, '', '芝加哥',
        240912111927006054, 9800, 'd626992880b441d68556db0448f75f5b'),
       (240912112034006059, 0, '2024-09-12 11:20:34', 0, '2024-09-12 14:10:02', 1, '', '华盛顿',
        240912111927006054, 9700, '60c384168f44470daccd7d6bfd5c480b'),
       (240912112043006060, 0, '2024-09-12 11:20:44', 0, '2024-09-12 14:10:02', 1, '', '上海',
        240912111910006053, 9900, 'e350a925ae8545b587eb6ad23637b1e3'),
       (240912140929006161, 0, '2024-09-12 14:09:30', 0, '2024-09-12 14:10:02', 1, '', '广州',
        240912111910006053, 9800, '5652f0e49d1d4b718fa3f4292f328ebb'),
       (240912140937006162, 0, '2024-09-12 14:09:37', 0, '2024-09-12 14:10:02', 1, '', '深圳',
        240912111910006053, 9700, '92d3475c58df465e9d10fd24419b7094');
/*!40000 ALTER TABLE `base_area`
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

-- Dump completed on 2024-09-24 10:52:25
