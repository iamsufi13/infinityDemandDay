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
-- Table structure for table `download_log`
--

DROP TABLE IF EXISTS `download_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `download_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dt1` datetime(6) DEFAULT NULL,
  `pdf_name` varchar(255) DEFAULT NULL,
  `vendor_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `pdf_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKoej24l328967f21bv2mx3un3c` (`vendor_id`),
  KEY `FKsvggrhvugnlfeatvoep48eq5` (`user_id`),
  CONSTRAINT `FKoej24l328967f21bv2mx3un3c` FOREIGN KEY (`vendor_id`) REFERENCES `vendors` (`id`),
  CONSTRAINT `FKsvggrhvugnlfeatvoep48eq5` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `download_log`
--

LOCK TABLES `download_log` WRITE;
/*!40000 ALTER TABLE `download_log` DISABLE KEYS */;
INSERT INTO `download_log` VALUES (1,'2024-10-03 18:09:59.164105','Test1234.pdf',4,NULL,0),(2,'2024-10-03 18:13:17.448741','Test1234.pdf',4,NULL,0),(3,'2024-10-04 16:23:17.128228','Test12345.pdf',4,NULL,0),(4,'2024-10-04 16:47:28.792182','Test12345.pdf',4,NULL,0),(5,'2024-10-04 16:53:23.051143','Test12345.pdf',4,NULL,0),(6,'2024-10-08 19:35:34.058902','Test12345.pdf',NULL,2,0),(7,'2024-10-09 15:43:14.219518',NULL,NULL,4,9);
/*!40000 ALTER TABLE `download_log` ENABLE KEYS */;
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
