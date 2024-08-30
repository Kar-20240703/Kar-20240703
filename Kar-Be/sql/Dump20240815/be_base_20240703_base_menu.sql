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
-- Table structure for table `base_menu`
--

DROP TABLE IF EXISTS `base_menu`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `base_menu`
(
    `id`                         bigint                                                        NOT NULL,
    `create_id`                  bigint                                                        NOT NULL,
    `create_time`                datetime                                                      NOT NULL,
    `update_id`                  bigint                                                        NOT NULL,
    `update_time`                datetime                                                      NOT NULL,
    `enable_flag`                tinyint(1)                                                    NOT NULL COMMENT '是否启用',
    `remark`                     varchar(300)                                                  NOT NULL,
    `name`                       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名',
    `path` varchar(100) NOT NULL COMMENT '页面的 path，备注：不能重复',
    `icon`                       varchar(100)                                                  NOT NULL COMMENT '图标',
    `pid`                        bigint                                                        NOT NULL COMMENT '父节点id（顶级则为0）',
    `show_flag`                  tinyint(1)                                                    NOT NULL COMMENT '是否显示在 左侧的菜单栏里面，如果为 false，也可以通过 $router.push()访问到',
    `link_flag`                  tinyint(1)                                                    NOT NULL COMMENT '是否外链，即，打开页面会在一个新的窗口打开，可以配合 router',
    `router`                     varchar(100)                                                  NOT NULL COMMENT '路由',
    `redirect`                   varchar(100)                                                  NOT NULL COMMENT '重定向，优先级最高',
    `order_no`                   int                                                           NOT NULL COMMENT '排序号（值越大越前面，默认为 0）',
    `first_flag`                 tinyint(1)                                                    NOT NULL COMMENT '是否是起始页面，备注：只能存在一个 firstFlag === true 的菜单',
    `uuid` varchar(32) NOT NULL COMMENT '该菜单的 uuid，备注：不能重复',
    `hidden_page_container_flag` tinyint(1)                                                    NOT NULL COMMENT '是否隐藏：PageContainer',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='v20240703：主表：菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `base_menu`
--

LOCK TABLES `base_menu` WRITE;
/*!40000 ALTER TABLE `base_menu`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `base_menu`
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

-- Dump completed on 2024-08-15 11:01:21
