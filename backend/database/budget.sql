-- MySQL dump 10.13  Distrib 5.5.47, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: budget
-- ------------------------------------------------------
-- Server version	5.5.47-0ubuntu0.14.04.1

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
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categories` (
  `category_id` int(11) NOT NULL,
  `category_name` text NOT NULL,
  `type` text NOT NULL,
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `CategoryID_2` (`category_id`),
  KEY `CategoryID` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (0,'Rent/Mortgage','Expense'),(1,'Home Insurance','Expense'),(2,'Home Maintenance','Expense'),(3,'Food','Expense'),(4,'Electric/Gas/Water','Expense'),(5,'Internet/Phone','Expense'),(6,'Car Payment','Expense'),(7,'Car Insurance','Expense'),(8,'Gas/Parking','Expense'),(9,'Car Maintenance','Expense'),(10,'Next Car','Expense'),(11,'Outings','Expense'),(12,'Vacation Fund','Expense'),(13,'Electronics','Expense'),(14,'Games','Expense'),(15,'Misc. Entertainment','Expense'),(16,'Snacks','Expense'),(17,'Clothing','Expense'),(18,'Appliances/Furniture','Expense'),(19,'Misc. General','Expense'),(20,'Health Insurance','Expense'),(21,'Life Insurance','Expense'),(22,'Student Loans','Expense'),(23,'Credit Card Loans','Expense'),(24,'Other Loans','Expense'),(25,'Charity','Expense'),(26,'Savings','Expense'),(27,'Emergency Fund','Expense'),(28,'Salary','Income'),(29,'Bonus','Income'),(30,'Dividends','Income'),(31,'Interest','Income'),(32,'Gifts','Income'),(33,'Other','Income');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expense_plan`
--

DROP TABLE IF EXISTS `expense_plan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expense_plan` (
  `expense_id` int(11) NOT NULL,
  `category` text NOT NULL,
  `amount` double NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`expense_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expense_plan`
--

LOCK TABLES `expense_plan` WRITE;
/*!40000 ALTER TABLE `expense_plan` DISABLE KEYS */;
/*!40000 ALTER TABLE `expense_plan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expenses`
--

DROP TABLE IF EXISTS `expenses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `expenses` (
  `expense_id` int(11) NOT NULL,
  `category` text NOT NULL,
  `date` date NOT NULL,
  `amount` double NOT NULL,
  `description` longtext NOT NULL,
  PRIMARY KEY (`expense_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expenses`
--

LOCK TABLES `expenses` WRITE;
/*!40000 ALTER TABLE `expenses` DISABLE KEYS */;
INSERT INTO `expenses` VALUES (0,'Games','2015-12-02',14.84,'elite dangerous'),(1,'Food','2015-12-02',7.18,'eggnog'),(2,'Charity','2015-12-02',2,'homeless guy'),(3,'Outings','2015-12-05',49.18,'savas'),(4,'Electronics','2015-12-06',13.94,'flash drives'),(5,'Outings','2015-12-08',6.21,'shake'),(6,'Home Maintenance','2015-12-12',6.36,'keys'),(7,'Charity','2015-12-14',31.8,'lukas gift'),(8,'Charity','2015-12-14',34.64,'secret santa gift'),(9,'Games','2015-12-18',10,'eve resub'),(10,'Charity','2015-12-21',47.7,'mormor gift'),(11,'Charity','2015-12-22',105.95,'dad gift'),(12,'Misc. Entertainment','2015-12-22',6,'magazines'),(13,'Charity','2015-12-29',40,'morfar gift'),(14,'Misc. Entertainment','2015-12-29',37,'lockpick and speaker'),(15,'Outings','2016-01-02',20,'h8ful eight'),(16,'Outings','2016-01-02',11.5,'noodles'),(17,'Snacks','2016-01-02',10.25,'h8ful food'),(18,'Student Loans','2016-01-02',35.78,'staples supplies'),(19,'Savings','2016-01-09',75,'savings'),(20,'Snacks','2016-01-09',1.61,'seven eleven'),(21,'Food','2016-01-09',18.55,'cvs mouth stuff'),(22,'Electronics','2016-01-09',10,'laptop strap'),(23,'Misc. Entertainment','2016-01-12',6.49,'lock to pick'),(24,'Outings','2016-01-12',19.62,'americana'),(25,'Internet/Phone','2016-01-12',11.51,'project fi backdated'),(26,'Outings','2016-01-12',13.84,'bobbys'),(27,'Outings','2016-01-12',3.75,'7-11'),(28,'Snacks','2016-01-18',3.5,'bubble tea'),(29,'Misc. Entertainment','2016-01-18',4,'powerball'),(30,'Electronics','2016-01-18',4,'ex kernel manager'),(31,'Outings','2016-01-20',17.74,'shake shack'),(32,'Snacks','2016-01-20',3.5,'bubble tea'),(33,'Outings','2016-01-23',12.35,'deadpool ticket'),(34,'Outings','2016-01-25',2.94,'pretzel'),(35,'Outings','2016-01-25',11.5,'bobbys'),(36,'Outings','2016-01-25',1,'fallout shelter'),(37,'Snacks','2016-01-29',2.48,'donuts'),(38,'Home Maintenance','2016-02-06',15,'haircut'),(39,'Electronics','2016-02-06',12.36,'stickers'),(40,'Outings','2016-02-06',27.69,'americana'),(41,'Misc. Entertainment','2016-02-08',1,'hack summit'),(42,'Internet/Phone','2016-02-08',20,'acm'),(43,'Home Maintenance','2016-02-19',75,'new shoes'),(44,'Outings','2016-02-19',28.06,'ruby tuesday'),(45,'Snacks','2016-02-19',3.5,'bubble tea'),(46,'Misc. Entertainment','2016-02-19',5,'misc cra'),(47,'Snacks','2016-02-19',5,'halal food');
/*!40000 ALTER TABLE `expenses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `income_plan`
--

DROP TABLE IF EXISTS `income_plan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `income_plan` (
  `income_id` int(11) NOT NULL,
  `category` text NOT NULL,
  `amount` double NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`income_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `income_plan`
--

LOCK TABLES `income_plan` WRITE;
/*!40000 ALTER TABLE `income_plan` DISABLE KEYS */;
/*!40000 ALTER TABLE `income_plan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `incomes`
--

DROP TABLE IF EXISTS `incomes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `incomes` (
  `income_id` int(11) NOT NULL,
  `date` date NOT NULL,
  `amount` double NOT NULL,
  `description` longtext NOT NULL,
  `category` text NOT NULL,
  PRIMARY KEY (`income_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `incomes`
--

LOCK TABLES `incomes` WRITE;
/*!40000 ALTER TABLE `incomes` DISABLE KEYS */;
INSERT INTO `incomes` VALUES (0,'2015-12-02',0.91,'project fi unused data','Bonus'),(1,'2015-12-02',10,'play music refund','Gifts'),(3,'2015-12-03',7.5,'payback from archer','Interest'),(4,'2015-12-05',30,'payback for savas','Interest'),(5,'2015-12-21',20,'letters for dad','Bonus'),(6,'2015-12-21',25,'helping jan','Bonus'),(7,'2016-02-09',0,'','Salary');
/*!40000 ALTER TABLE `incomes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `monthly_expenses`
--

DROP TABLE IF EXISTS `monthly_expenses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `monthly_expenses` (
  `monthly_expense_id` int(11) NOT NULL,
  `category` text NOT NULL,
  `amount` double NOT NULL,
  `description` longtext NOT NULL,
  PRIMARY KEY (`monthly_expense_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `monthly_expenses`
--

LOCK TABLES `monthly_expenses` WRITE;
/*!40000 ALTER TABLE `monthly_expenses` DISABLE KEYS */;
INSERT INTO `monthly_expenses` VALUES (0,'Internet/Phone',35.83,'project fi'),(1,'Misc. Entertainment',10,'google play music'),(2,'Electronics',5,'google play family plan'),(3,'Internet/Phone',5,'project fi protection'),(4,'Internet/Phone',5,'google apps'),(5,'Electronics',7,'cactus vpn'),(6,'Internet/Phone',2,'google domains');
/*!40000 ALTER TABLE `monthly_expenses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `monthly_incomes`
--

DROP TABLE IF EXISTS `monthly_incomes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `monthly_incomes` (
  `monthly_income_id` int(11) NOT NULL,
  `category` text NOT NULL,
  `amount` double NOT NULL,
  `description` longtext NOT NULL,
  PRIMARY KEY (`monthly_income_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `monthly_incomes`
--

LOCK TABLES `monthly_incomes` WRITE;
/*!40000 ALTER TABLE `monthly_incomes` DISABLE KEYS */;
INSERT INTO `monthly_incomes` VALUES (0,'Salary',250,'allowance');
/*!40000 ALTER TABLE `monthly_incomes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `hashed_pass` text NOT NULL,
  `username` text NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (0,'jamie','password');
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

-- Dump completed on 2016-03-21 23:35:18
