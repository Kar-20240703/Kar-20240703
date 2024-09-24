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
-- Table structure for table `base_dept`
--

DROP TABLE IF EXISTS `base_dept`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_dept`
(
    `id`          bigint                                                        NOT NULL,
    `create_id`   bigint                                                        NOT NULL,
    `create_time` datetime                                                      NOT NULL,
    `update_id`   bigint                                                        NOT NULL,
    `update_time` datetime                                                      NOT NULL,
    `enable_flag` tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`      varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '备注',
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '部门名',
    `pid`         bigint                                                        NOT NULL COMMENT '父节点id（顶级则为0）',
    `order_no`    int                                                           NOT NULL COMMENT '排序号（值越大越前面，默认为 0）',
    `uuid`        varchar(32)                                                   NOT NULL COMMENT '该菜单的 uuid，用于：同步租户菜单等操作，备注：不能重复',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='v20240703：主表：部门表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `base_dept`
--

LOCK TABLES `base_dept` WRITE;
/*!40000 ALTER TABLE `base_dept`
    DISABLE KEYS */;
INSERT INTO `base_dept`
VALUES (240912141649006163, 0, '2024-09-12 14:16:49', 0, '2024-09-12 14:16:55', 1, '', '总部', 0,
        10000, 'ec1deda0ce664917840099b32c93bdbb'),
       (240912141714006164, 0, '2024-09-12 14:17:15', 0, '2024-09-12 14:17:51', 1, '', '开发部',
        240912141649006163, 10000, '8f5b5f6033964483b0aa5700c3ff5d4f'),
       (240912141722006165, 0, '2024-09-12 14:17:23', 0, '2024-09-12 14:17:55', 1, '', '测试部',
        240912141649006163, 9900, 'ec0b2769f1c84affa447f69d2cdb6c99'),
       (240912141729006166, 0, '2024-09-12 14:17:30', 0, '2024-09-12 14:17:57', 1, '', '运维部',
        240912141649006163, 9800, 'd783c4648138445b9d52febfec8b76dd'),
       (240912141822006167, 0, '2024-09-12 14:18:23', 0, '2024-09-12 14:18:23', 1, '', '地球开发部',
        240912141714006164, 10000, 'caae0135e7ad4cefa9dfe6f170b231b4'),
       (240912141831006168, 0, '2024-09-12 14:18:32', 0, '2024-09-12 14:18:32', 1, '', '中国开发部',
        240912141822006167, 10000, 'ab34925f044e4cd6a93177454ebe251d'),
       (240912141840006169, 0, '2024-09-12 14:18:41', 0, '2024-09-12 14:21:12', 1, '', '美国开发部',
        240912141822006167, 9900, '08adc670a1954e16ac1874206c1f7ee6'),
       (240912141903006170, 0, '2024-09-12 14:19:04', 0, '2024-09-12 14:19:04', 1, '', '北京开发部',
        240912141831006168, 10000, 'ecc3e6ac95644bb1a516e42f89f8c783'),
       (240912141910006171, 0, '2024-09-12 14:19:11', 0, '2024-09-12 14:19:11', 1, '', '上海开发部',
        240912141831006168, 9900, 'd2a10744f27e4f1295e057d52b719263'),
       (240912141918006172, 0, '2024-09-12 14:19:18', 0, '2024-09-12 14:19:18', 1, '', '广州开发部',
        240912141831006168, 9800, '9c48634392994d3597903a9268d527a2'),
       (240912141925006173, 0, '2024-09-12 14:19:26', 0, '2024-09-12 14:19:26', 1, '', '深圳开发部',
        240912141831006168, 9700, '1594010b9c694a38abb9f2a26f2c764e'),
       (240912141945006174, 0, '2024-09-12 14:19:46', 0, '2024-09-12 14:20:54', 1, '', '纽约开发部',
        240912141840006169, 10000, 'f8c798d821e04adba1ba935ed15a6ca0'),
       (240912141954006175, 0, '2024-09-12 14:19:55', 0, '2024-09-12 14:20:54', 1, '',
        '洛杉矶开发部', 240912141840006169, 9900, 'e6b12874fc3144c0920b1513d191304e'),
       (240912142001006176, 0, '2024-09-12 14:20:02', 0, '2024-09-12 14:20:54', 1, '',
        '芝加哥开发部', 240912141840006169, 9800, '3f141a23e1284d73b6a0e4df2613dc02'),
       (240912142008006177, 0, '2024-09-12 14:20:08', 0, '2024-09-12 14:20:54', 1, '',
        '华盛顿开发部', 240912141840006169, 9700, 'eb032d1093b6457888844dbbf4ab5cea');
/*!40000 ALTER TABLE `base_dept`
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
