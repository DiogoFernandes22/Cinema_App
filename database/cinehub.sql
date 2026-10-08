-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: cinehub
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE DATABASE IF NOT EXISTS `cinehub`;
USE `cinehub`;

--
-- Table structure for table `actors`
--

DROP TABLE IF EXISTS `actors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `actors` (
  `actor_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(150) NOT NULL,
  PRIMARY KEY (`actor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actors`
--

LOCK TABLES `actors` WRITE;
/*!40000 ALTER TABLE `actors` DISABLE KEYS */;
/*!40000 ALTER TABLE `actors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `directors`
--

DROP TABLE IF EXISTS `directors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `directors` (
  `director_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(500) NOT NULL,
  PRIMARY KEY (`director_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `directors`
--

LOCK TABLES `directors` WRITE;
/*!40000 ALTER TABLE `directors` DISABLE KEYS */;
INSERT INTO `directors` VALUES
(2,'James Cameron'),
(7,'Christopher Nolan'),
(8,'Spike Jonze');
/*!40000 ALTER TABLE `directors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `genres`
--

DROP TABLE IF EXISTS `genres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `genres` (
  `genre_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`genre_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `genres`
--

LOCK TABLES `genres` WRITE;
/*!40000 ALTER TABLE `genres` DISABLE KEYS */;
INSERT INTO `genres` VALUES
(1,'Terror'),
(2,'Science Fiction'),
(3,'Action'),
(4,'Adventure'),
(5,'Drama'),
(6,'Romance'),
(7,'War');
/*!40000 ALTER TABLE `genres` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movie_actors`
--

DROP TABLE IF EXISTS `movie_actors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movie_actors` (
  `movie_id` int NOT NULL,
  `actor_id` int NOT NULL,
  PRIMARY KEY (`movie_id`,`actor_id`),
  KEY `actor_id` (`actor_id`),
  CONSTRAINT `movie_actors_ibfk_1`
    FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`),
  CONSTRAINT `movie_actors_ibfk_2`
    FOREIGN KEY (`actor_id`) REFERENCES `actors` (`actor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movie_actors`
--

LOCK TABLES `movie_actors` WRITE;
/*!40000 ALTER TABLE `movie_actors` DISABLE KEYS */;
/*!40000 ALTER TABLE `movie_actors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movie_genres`
--

DROP TABLE IF EXISTS `movie_genres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movie_genres` (
  `movie_id` int NOT NULL,
  `genre_id` int NOT NULL,
  PRIMARY KEY (`movie_id`,`genre_id`),
  KEY `genre_id` (`genre_id`),
  CONSTRAINT `movie_genres_ibfk_1`
    FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`),
  CONSTRAINT `movie_genres_ibfk_2`
    FOREIGN KEY (`genre_id`) REFERENCES `genres` (`genre_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movie_genres`
--

LOCK TABLES `movie_genres` WRITE;
/*!40000 ALTER TABLE `movie_genres` DISABLE KEYS */;
INSERT INTO `movie_genres` VALUES
(7,2),
(8,2),
(9,2),
(10,2),
(7,3),
(8,3),
(11,3),
(7,4),
(8,4),
(9,4),
(9,5),
(10,5),
(11,5),
(10,6),
(11,7);
/*!40000 ALTER TABLE `movie_genres` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movies`
--

DROP TABLE IF EXISTS `movies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movies` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(150) NOT NULL,
  `duration` int DEFAULT NULL,
  `release_date` int DEFAULT NULL,
  `rate` double DEFAULT NULL,
  `director_id` int DEFAULT NULL,
  `studio_id` int DEFAULT NULL,
  `tmdb_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `tmdb_id` (`tmdb_id`),
  KEY `fk_movies_director` (`director_id`),
  KEY `fk_movies_studio` (`studio_id`),
  CONSTRAINT `fk_movies_director`
    FOREIGN KEY (`director_id`) REFERENCES `directors` (`director_id`),
  CONSTRAINT `fk_movies_studio`
    FOREIGN KEY (`studio_id`) REFERENCES `studio` (`studio_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movies`
--

LOCK TABLES `movies` WRITE;
/*!40000 ALTER TABLE `movies` DISABLE KEYS */;
INSERT INTO `movies` VALUES
(7,'Avatar',162,2009,7.61,2,2,19995),
(8,'Inception',148,2010,8.374,7,3,27205),
(9,'Interstellar',169,2014,8.488,7,4,157336),
(10,'Her',126,2013,7.835,8,5,152601),
(11,'Dunkirk',107,2017,7.45,7,3,374720);
/*!40000 ALTER TABLE `movies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservations`
--

DROP TABLE IF EXISTS `reservations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservations` (
  `id` int NOT NULL AUTO_INCREMENT,
  `session_id` int NOT NULL,
  `user_id` int DEFAULT NULL,
  `confirmed` tinyint(1) NOT NULL DEFAULT '0',
  `paid` tinyint(1) NOT NULL DEFAULT '0',
  `payment_method` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `session_id` (`session_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `reservations_ibfk_1`
    FOREIGN KEY (`session_id`) REFERENCES `sessions` (`id`),
  CONSTRAINT `reservations_ibfk_2`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservations`
--

LOCK TABLES `reservations` WRITE;
/*!40000 ALTER TABLE `reservations` DISABLE KEYS */;
INSERT INTO `reservations` VALUES
(1,2,NULL,0,0,NULL),
(2,2,NULL,1,1,'Multibanco'),
(3,2,NULL,0,0,NULL),
(4,2,NULL,0,0,NULL),
(5,2,NULL,0,0,NULL),
(6,2,NULL,0,0,NULL),
(7,11,NULL,0,0,NULL),
(8,5,NULL,0,0,NULL),
(9,5,NULL,1,1,'Multibanco');
/*!40000 ALTER TABLE `reservations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rooms`
--

DROP TABLE IF EXISTS `rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rooms` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `capacity` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rooms`
--

LOCK TABLES `rooms` WRITE;
/*!40000 ALTER TABLE `rooms` DISABLE KEYS */;
INSERT INTO `rooms` VALUES
(1,'NORMAL','Sala 1',120),
(2,'NORMAL','Sala 2',80),
(3,'IMAX','Sala IMAX',180);
/*!40000 ALTER TABLE `rooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seats`
--

DROP TABLE IF EXISTS `seats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seats` (
  `id` int NOT NULL AUTO_INCREMENT,
  `room_id` int NOT NULL,
  `row_letter` char(1) NOT NULL,
  `seat_number` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `room_id` (`room_id`),
  CONSTRAINT `seats_ibfk_1`
    FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=381 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seats`
--

LOCK TABLES `seats` WRITE;
/*!40000 ALTER TABLE `seats` DISABLE KEYS */;
INSERT INTO `seats` VALUES
(1,1,'A',1),(2,1,'A',2),(3,1,'A',3),(4,1,'A',4),
(5,1,'A',5),(6,1,'A',6),(7,1,'A',7),(8,1,'A',8),
(9,1,'A',9),(10,1,'A',10),(11,1,'A',11),(12,1,'A',12),
(13,1,'B',1),(14,1,'B',2),(15,1,'B',3),(16,1,'B',4),
(17,1,'B',5),(18,1,'B',6),(19,1,'B',7),(20,1,'B',8),
(21,1,'B',9),(22,1,'B',10),(23,1,'B',11),(24,1,'B',12),
(25,1,'C',1),(26,1,'C',2),(27,1,'C',3),(28,1,'C',4),
(29,1,'C',5),(30,1,'C',6),(31,1,'C',7),(32,1,'C',8),
(33,1,'C',9),(34,1,'C',10),(35,1,'C',11),(36,1,'C',12),
(37,1,'D',1),(38,1,'D',2),(39,1,'D',3),(40,1,'D',4),
(41,1,'D',5),(42,1,'D',6),(43,1,'D',7),(44,1,'D',8),
(45,1,'D',9),(46,1,'D',10),(47,1,'D',11),(48,1,'D',12),
(49,1,'E',1),(50,1,'E',2),(51,1,'E',3),(52,1,'E',4),
(53,1,'E',5),(54,1,'E',6),(55,1,'E',7),(56,1,'E',8),
(57,1,'E',9),(58,1,'E',10),(59,1,'E',11),(60,1,'E',12),
(61,1,'F',1),(62,1,'F',2),(63,1,'F',3),(64,1,'F',4),
(65,1,'F',5),(66,1,'F',6),(67,1,'F',7),(68,1,'F',8),
(69,1,'F',9),(70,1,'F',10),(71,1,'F',11),(72,1,'F',12),
(73,1,'G',1),(74,1,'G',2),(75,1,'G',3),(76,1,'G',4),
(77,1,'G',5),(78,1,'G',6),(79,1,'G',7),(80,1,'G',8),
(81,1,'G',9),(82,1,'G',10),(83,1,'G',11),(84,1,'G',12),
(85,1,'H',1),(86,1,'H',2),(87,1,'H',3),(88,1,'H',4),
(89,1,'H',5),(90,1,'H',6),(91,1,'H',7),(92,1,'H',8),
(93,1,'H',9),(94,1,'H',10),(95,1,'H',11),(96,1,'H',12),
(97,1,'I',1),(98,1,'I',2),(99,1,'I',3),(100,1,'I',4),
(101,1,'I',5),(102,1,'I',6),(103,1,'I',7),(104,1,'I',8),
(105,1,'I',9),(106,1,'I',10),(107,1,'I',11),(108,1,'I',12),
(109,1,'J',1),(110,1,'J',2),(111,1,'J',3),(112,1,'J',4),
(113,1,'J',5),(114,1,'J',6),(115,1,'J',7),(116,1,'J',8),
(117,1,'J',9),(118,1,'J',10),(119,1,'J',11),(120,1,'J',12),

(121,2,'A',1),(122,2,'A',2),(123,2,'A',3),(124,2,'A',4),
(125,2,'A',5),(126,2,'A',6),(127,2,'A',7),(128,2,'A',8),
(129,2,'A',9),(130,2,'A',10),
(131,2,'B',1),(132,2,'B',2),(133,2,'B',3),(134,2,'B',4),
(135,2,'B',5),(136,2,'B',6),(137,2,'B',7),(138,2,'B',8),
(139,2,'B',9),(140,2,'B',10),
(141,2,'C',1),(142,2,'C',2),(143,2,'C',3),(144,2,'C',4),
(145,2,'C',5),(146,2,'C',6),(147,2,'C',7),(148,2,'C',8),
(149,2,'C',9),(150,2,'C',10),
(151,2,'D',1),(152,2,'D',2),(153,2,'D',3),(154,2,'D',4),
(155,2,'D',5),(156,2,'D',6),(157,2,'D',7),(158,2,'D',8),
(159,2,'D',9),(160,2,'D',10),
(161,2,'E',1),(162,2,'E',2),(163,2,'E',3),(164,2,'E',4),
(165,2,'E',5),(166,2,'E',6),(167,2,'E',7),(168,2,'E',8),
(169,2,'E',9),(170,2,'E',10),
(171,2,'F',1),(172,2,'F',2),(173,2,'F',3),(174,2,'F',4),
(175,2,'F',5),(176,2,'F',6),(177,2,'F',7),(178,2,'F',8),
(179,2,'F',9),(180,2,'F',10),
(181,2,'G',1),(182,2,'G',2),(183,2,'G',3),(184,2,'G',4),
(185,2,'G',5),(186,2,'G',6),(187,2,'G',7),(188,2,'G',8),
(189,2,'G',9),(190,2,'G',10),
(191,2,'H',1),(192,2,'H',2),(193,2,'H',3),(194,2,'H',4),
(195,2,'H',5),(196,2,'H',6),(197,2,'H',7),(198,2,'H',8),
(199,2,'H',9),(200,2,'H',10),

(201,3,'A',1),(202,3,'A',2),(203,3,'A',3),(204,3,'A',4),
(205,3,'A',5),(206,3,'A',6),(207,3,'A',7),(208,3,'A',8),
(209,3,'A',9),(210,3,'A',10),(211,3,'A',11),(212,3,'A',12),
(213,3,'A',13),(214,3,'A',14),(215,3,'A',15),
(216,3,'B',1),(217,3,'B',2),(218,3,'B',3),(219,3,'B',4),
(220,3,'B',5),(221,3,'B',6),(222,3,'B',7),(223,3,'B',8),
(224,3,'B',9),(225,3,'B',10),(226,3,'B',11),(227,3,'B',12),
(228,3,'B',13),(229,3,'B',14),(230,3,'B',15),
(231,3,'C',1),(232,3,'C',2),(233,3,'C',3),(234,3,'C',4),
(235,3,'C',5),(236,3,'C',6),(237,3,'C',7),(238,3,'C',8),
(239,3,'C',9),(240,3,'C',10),(241,3,'C',11),(242,3,'C',12),
(243,3,'C',13),(244,3,'C',14),(245,3,'C',15),
(246,3,'D',1),(247,3,'D',2),(248,3,'D',3),(249,3,'D',4),
(250,3,'D',5),(251,3,'D',6),(252,3,'D',7),(253,3,'D',8),
(254,3,'D',9),(255,3,'D',10),(256,3,'D',11),(257,3,'D',12),
(258,3,'D',13),(259,3,'D',14),(260,3,'D',15),
(261,3,'E',1),(262,3,'E',2),(263,3,'E',3),(264,3,'E',4),
(265,3,'E',5),(266,3,'E',6),(267,3,'E',7),(268,3,'E',8),
(269,3,'E',9),(270,3,'E',10),(271,3,'E',11),(272,3,'E',12),
(273,3,'E',13),(274,3,'E',14),(275,3,'E',15),
(276,3,'F',1),(277,3,'F',2),(278,3,'F',3),(279,3,'F',4),
(280,3,'F',5),(281,3,'F',6),(282,3,'F',7),(283,3,'F',8),
(284,3,'F',9),(285,3,'F',10),(286,3,'F',11),(287,3,'F',12),
(288,3,'F',13),(289,3,'F',14),(290,3,'F',15),
(291,3,'G',1),(292,3,'G',2),(293,3,'G',3),(294,3,'G',4),
(295,3,'G',5),(296,3,'G',6),(297,3,'G',7),(298,3,'G',8),
(299,3,'G',9),(300,3,'G',10),(301,3,'G',11),(302,3,'G',12),
(303,3,'G',13),(304,3,'G',14),(305,3,'G',15),
(306,3,'H',1),(307,3,'H',2),(308,3,'H',3),(309,3,'H',4),
(310,3,'H',5),(311,3,'H',6),(312,3,'H',7),(313,3,'H',8),
(314,3,'H',9),(315,3,'H',10),(316,3,'H',11),(317,3,'H',12),
(318,3,'H',13),(319,3,'H',14),(320,3,'H',15),
(321,3,'I',1),(322,3,'I',2),(323,3,'I',3),(324,3,'I',4),
(325,3,'I',5),(326,3,'I',6),(327,3,'I',7),(328,3,'I',8),
(329,3,'I',9),(330,3,'I',10),(331,3,'I',11),(332,3,'I',12),
(333,3,'I',13),(334,3,'I',14),(335,3,'I',15),
(336,3,'J',1),(337,3,'J',2),(338,3,'J',3),(339,3,'J',4),
(340,3,'J',5),(341,3,'J',6),(342,3,'J',7),(343,3,'J',8),
(344,3,'J',9),(345,3,'J',10),(346,3,'J',11),(347,3,'J',12),
(348,3,'J',13),(349,3,'J',14),(350,3,'J',15),
(351,3,'K',1),(352,3,'K',2),(353,3,'K',3),(354,3,'K',4),
(355,3,'K',5),(356,3,'K',6),(357,3,'K',7),(358,3,'K',8),
(359,3,'K',9),(360,3,'K',10),(361,3,'K',11),(362,3,'K',12),
(363,3,'K',13),(364,3,'K',14),(365,3,'K',15),
(366,3,'L',1),(367,3,'L',2),(368,3,'L',3),(369,3,'L',4),
(370,3,'L',5),(371,3,'L',6),(372,3,'L',7),(373,3,'L',8),
(374,3,'L',9),(375,3,'L',10),(376,3,'L',11),(377,3,'L',12),
(378,3,'L',13),(379,3,'L',14),(380,3,'L',15);
/*!40000 ALTER TABLE `seats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sessions`
--

DROP TABLE IF EXISTS `sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sessions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `movie_id` int NOT NULL,
  `room_id` int NOT NULL,
  `session_time` time NOT NULL,
  `price_type` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `movie_id` (`movie_id`),
  KEY `fk_sessions_room` (`room_id`),
  CONSTRAINT `fk_sessions_room`
    FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id`),
  CONSTRAINT `sessions_ibfk_1`
    FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sessions`
--

LOCK TABLES `sessions` WRITE;
/*!40000 ALTER TABLE `sessions` DISABLE KEYS */;
INSERT INTO `sessions` VALUES
(1,7,1,'18:00:00','NORMAL'),
(2,7,1,'21:00:00','NORMAL'),
(3,8,2,'18:30:00','NORMAL'),
(4,8,2,'21:30:00','NORMAL'),
(5,9,3,'18:00:00','IMAX'),
(6,9,3,'21:30:00','IMAX'),
(7,8,1,'20:30:00','NORMAL'),
(8,7,1,'20:30:00','NORMAL'),
(9,11,3,'20:30:00','IMAX'),
(10,7,3,'20:00:00','IMAX'),
(11,10,1,'20:00:00','NORMAL');
/*!40000 ALTER TABLE `sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `studio`
--

DROP TABLE IF EXISTS `studio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `studio` (
  `studio_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(150) NOT NULL,
  PRIMARY KEY (`studio_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `studio`
--

LOCK TABLES `studio` WRITE;
/*!40000 ALTER TABLE `studio` DISABLE KEYS */;
INSERT INTO `studio` VALUES
(1,'20th Century Studios'),
(2,'Dune Entertainment'),
(3,'Warner Bros. Pictures'),
(4,'Legendary Pictures'),
(5,'Annapurna Pictures');
/*!40000 ALTER TABLE `studio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tickets`
--

DROP TABLE IF EXISTS `tickets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tickets` (
  `id` int NOT NULL AUTO_INCREMENT,
  `reservation_id` int NOT NULL,
  `session_id` int NOT NULL,
  `seat_id` int NOT NULL,
  `type_ticket` varchar(20) NOT NULL,
  `price_paid` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `session_id` (`session_id`,`seat_id`),
  KEY `reservation_id` (`reservation_id`),
  KEY `seat_id` (`seat_id`),
  CONSTRAINT `tickets_ibfk_1`
    FOREIGN KEY (`reservation_id`) REFERENCES `reservations` (`id`),
  CONSTRAINT `tickets_ibfk_2`
    FOREIGN KEY (`session_id`) REFERENCES `sessions` (`id`),
  CONSTRAINT `tickets_ibfk_3`
    FOREIGN KEY (`seat_id`) REFERENCES `seats` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tickets`
--

LOCK TABLES `tickets` WRITE;
/*!40000 ALTER TABLE `tickets` DISABLE KEYS */;
INSERT INTO `tickets` VALUES
(1,1,2,3,'NORMAL',8.50),
(2,2,2,4,'NORMAL',8.50),
(3,6,2,5,'NORMAL',8.50),
(4,7,11,1,'NORMAL',8.50),
(5,8,5,201,'NORMAL',12.00),
(6,9,5,278,'ESTUDANTE',9.00);
/*!40000 ALTER TABLE `tickets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed