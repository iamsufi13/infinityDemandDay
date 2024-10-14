-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: contenttree
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `vendors`
--

DROP TABLE IF EXISTS `vendors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vendors` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `dt1` datetime(6) DEFAULT NULL,
  `status` enum('APPROVED','PENDING','REJECTED') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vendors`
--

LOCK TABLES `vendors` WRITE;
/*!40000 ALTER TABLE `vendors` DISABLE KEYS */;
INSERT INTO `vendors` VALUES (2,'TestVendor001','test@test.com','$2a$10$z/ulsJ6mVgFT9DxygTVgVu.F3r1uym4E3tTPVXJWNreXuJYmfkaDu',NULL,NULL),(3,'TestVendor001','test001@test.com','$2a$10$Tzgdi0g6KX91ePqYu8fmdu4JP6OZ/jpxOIyh.brKlfMHoQ2IuT8R6',NULL,NULL),(4,'John Doe','Johndoe@gmail.com','$2a$10$xmnIt6Ct4dpv.evcqSuV5.ezG5IO182OSa4fxkVBbBDuLD3aqVtte','2024-10-02 19:02:17.934848','APPROVED'),(5,'Bruce Wayne','bruce@wayne.com','$2a$10$iLUCD4MeEEI.sNXLCLt.KunCmJITi1boQPhhw6B2ppczApjZIA6Fa','2024-10-04 14:58:09.362774','REJECTED'),(8,'Sufiyan Inamdar','sufiyaninamdar007@gmail.com','$2a$10$RzrZMGXY5qIy6zZ25qoVy.PXObR/PLGf1OriqSi24TQKtn0pMQ0Qe','2024-10-09 14:41:52.712240','APPROVED'),(9,'Sufiyan Inamdar','inamdarsufiyan13@gmail.com','$2a$10$Sj9DrmDJtqlXP1LRX7ayu.xoa01BGstkMUfnyxPCpcAjQ.Bl2b3D2','2024-10-09 14:42:49.050303','APPROVED');
/*!40000 ALTER TABLE `vendors` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-10-09 21:50:49
