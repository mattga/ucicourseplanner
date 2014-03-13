-- MySQL dump 10.13  Distrib 5.6.10, for Win64 (x86_64)
--
-- Host: localhost    Database: courseplanner
-- ------------------------------------------------------
-- Server version	5.6.10

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


--
-- Dumping data for table `completion_req`
--

LOCK TABLES `completion_req` WRITE;
/*!40000 ALTER TABLE `completion_req` DISABLE KEYS */;
INSERT INTO `completion_req` VALUES (5,17,10),(5,18,6);
/*!40000 ALTER TABLE `completion_req` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `course_offerings`
--

LOCK TABLES `course_offerings` WRITE;
/*!40000 ALTER TABLE `course_offerings` DISABLE KEYS */;
INSERT INTO `course_offerings` VALUES ('Spring',2014,23),('Winter',2015,24),('Spring',2014,25),('Winter',2015,26),('Spring',2014,27),('Spring',2014,28),('Winter',2015,30),('Spring',2015,31),('Winter',2015,31),('Fall',2015,32),('Spring',2015,33),('Fall',2015,34),('Fall',2015,48),('Fall',2015,49),('Spring',2015,50),('Spring',2015,51),('Winter',2015,52);
/*!40000 ALTER TABLE `course_offerings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
INSERT INTO `courses` VALUES (23,'Math','2A','Calculus 1',6,NULL),(24,'Math','2B','Calculus 2',6,NULL),(25,'ICS','6B','Boolean Algebra & Logic',4,NULL),(26,'ICS','6D','Discrete Mathematics',4,NULL),(27,'Math','3A','Linear Algebra',4,NULL),(28,'Stats','67','Intro to Prob & Stats',4,NULL),(29,'Philos','9','Symbolic Logic',4,NULL),(30,'ICS','31','Intro Programming',6,NULL),(31,'ICS','32','Programming with Sftwr Lib',6,NULL),(32,'ICS','33','Intermediate Programming',4,NULL),(33,'ICS','51','Intro to Computer Org',6,NULL),(34,'INF','43','Intro Software Eng',4,NULL),(35,'Phys','2','General Calc-Based Physics',4,NULL),(36,'Phys','7C','E-M 1',4,NULL),(37,'Phys','7D','E-M 2',4,NULL),(38,'ICS','139W','Critical Writing',4,NULL),(39,'CS','132','Computer Networks',4,NULL),(40,'CS','141','Conceptual Programming Lang I',4,NULL),(41,'CS','122A','Intro to Data Mgmt',4,NULL),(42,'CS','143A','Operating Systems',4,NULL),(43,'CS','151','Digital Logic Design',4,NULL),(44,'CS','152','Comp Syst Archit',4,NULL),(45,'CS','161','Des&Analys of Algorithms',4,NULL),(46,'CS','169','Intro Optimization',4,NULL),(47,'CS','171','Intro AI',4,NULL),(48,'ICS','45C','C/C++ as 2nd Lang',4,NULL),(49,'ICS','46','Data Structure Impl & Analys',4,NULL),(50,'ICS','53','Principals in Sys Design',4,NULL),(51,'ICS','53L','Principals in Sys Design Library',4,NULL),(52,'ICS','90','New Students Seminar',1,NULL),(53,'CS','113','Game Dec',4,NULL),(54,'CS','114','Proj Advanced 3D Comp Graphics',4,NULL),(55,'CS','117','Proj Computer Vision',4,NULL),(56,'CS','122B','Proj Databases Web Apps',4,NULL),(57,'CS','122C','Principles of Data Mngmt',4,NULL),(58,'CS','133','Advanced Comp Networks',4,NULL),(59,'CS','142B','Lang Processor Construction',4,NULL),(60,'CS','143B','Proj OS',4,NULL),(61,'CS','145','Embedded Computing Systems + Lab',4,NULL);
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `degree`
--

LOCK TABLES `degree` WRITE;
/*!40000 ALTER TABLE `degree` DISABLE KEYS */;
INSERT INTO `degree` VALUES (5,'AI','Computer Science','CS');
/*!40000 ALTER TABLE `degree` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `prereqs`
--

LOCK TABLES `prereqs` WRITE;
/*!40000 ALTER TABLE `prereqs` DISABLE KEYS */;
/*!40000 ALTER TABLE `prereqs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `schedule`
--

LOCK TABLES `schedule` WRITE;
/*!40000 ALTER TABLE `schedule` DISABLE KEYS */;
INSERT INTO `schedule` VALUES ('Spring',2014,23),('Winter',2015,24),('Spring',2014,25),('Winter',2015,26),('Spring',2014,27),('Spring',2014,28),('Winter',2015,30),('Spring',2015,31),('Fall',2015,32),('Spring',2015,33),('Fall',2015,34),('Fall',2015,48),('Fall',2015,49),('Spring',2015,50),('Spring',2015,51),('Winter',2015,52);
/*!40000 ALTER TABLE `schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `selection_sets`
--

LOCK TABLES `selection_sets` WRITE;
/*!40000 ALTER TABLE `selection_sets` DISABLE KEYS */;
INSERT INTO `selection_sets` VALUES (17,30,NULL),(17,31,NULL),(17,32,NULL),(17,33,NULL),(17,34,NULL),(17,48,NULL),(17,49,NULL),(17,50,NULL),(17,51,NULL),(17,52,NULL),(18,23,NULL),(18,24,NULL),(18,25,NULL),(18,26,NULL),(18,27,NULL),(18,28,NULL);
/*!40000 ALTER TABLE `selection_sets` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-02-17 19:16:54
