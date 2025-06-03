-- MySQL dump 10.13  Distrib 8.0.42, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: Bibliotech
-- ------------------------------------------------------
-- Server version	8.0.41

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

--
-- Table structure for table `Aluno`
--

DROP TABLE IF EXISTS `Aluno`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Aluno` (
  `pessoa_id` int NOT NULL,
  `matricula` varchar(20) NOT NULL,
  `id_necessidade` int DEFAULT NULL,
  `suspenso` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`pessoa_id`),
  UNIQUE KEY `matricula` (`matricula`),
  KEY `id_necessidade` (`id_necessidade`),
  CONSTRAINT `Aluno_ibfk_1` FOREIGN KEY (`id_necessidade`) REFERENCES `Necessidade` (`id`),
  CONSTRAINT `Aluno_ibfk_2` FOREIGN KEY (`pessoa_id`) REFERENCES `Pessoa` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Aluno`
--

LOCK TABLES `Aluno` WRITE;
/*!40000 ALTER TABLE `Aluno` DISABLE KEYS */;
INSERT INTO `Aluno` VALUES (2,'2024001',2,1),(5,'ksjfsf',2,1),(20,'5454hjl',1,0);
/*!40000 ALTER TABLE `Aluno` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Area_conhecimento`
--

DROP TABLE IF EXISTS `Area_conhecimento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Area_conhecimento` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Area_conhecimento`
--

LOCK TABLES `Area_conhecimento` WRITE;
/*!40000 ALTER TABLE `Area_conhecimento` DISABLE KEYS */;
INSERT INTO `Area_conhecimento` VALUES (1,'Biologia'),(2,'Física'),(3,'Química'),(4,'Geografia'),(5,'História'),(6,'Educação Física'),(7,'Artes'),(8,'Linguagens e Códigos'),(9,'Ciências Humanas e Sociais Aplicadas');
/*!40000 ALTER TABLE `Area_conhecimento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Avaliacao`
--

DROP TABLE IF EXISTS `Avaliacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Avaliacao` (
  `id` int NOT NULL AUTO_INCREMENT,
  `material_id` int NOT NULL,
  `aluno_id` int NOT NULL,
  `nota` int NOT NULL,
  `avaliacao` varchar(200) NOT NULL,
  `data` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `material_id` (`material_id`),
  KEY `aluno_id` (`aluno_id`),
  CONSTRAINT `Avaliacao_ibfk_1` FOREIGN KEY (`material_id`) REFERENCES `Material` (`id`) ON DELETE CASCADE,
  CONSTRAINT `Avaliacao_ibfk_2` FOREIGN KEY (`aluno_id`) REFERENCES `Aluno` (`pessoa_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Avaliacao`
--

LOCK TABLES `Avaliacao` WRITE;
/*!40000 ALTER TABLE `Avaliacao` DISABLE KEYS */;
INSERT INTO `Avaliacao` VALUES (3,49,2,3,'','2025-05-30 23:31:11'),(4,49,2,4,'','2025-05-30 23:34:40'),(5,49,2,4,'teste de avaliação','2025-06-03 00:08:51'),(7,49,2,4,'','2025-06-03 08:49:33'),(8,45,2,5,'','2025-06-03 08:52:05'),(9,49,2,2,'teste de apagar','2025-06-03 08:54:10'),(10,49,2,2,'teste de apagar','2025-06-03 08:54:11'),(11,49,2,5,'','2025-06-03 09:12:16'),(12,49,2,5,'','2025-06-03 09:16:32'),(13,49,2,4,'','2025-06-03 09:19:33'),(14,49,2,4,'','2025-06-03 09:20:44'),(15,49,2,4,'teste de avaliação','2025-06-03 09:24:40'),(16,49,2,5,'','2025-06-03 09:24:58'),(17,49,2,5,'','2025-06-03 09:25:14'),(18,49,2,4,'','2025-06-03 09:27:18'),(19,49,2,5,'','2025-06-03 09:27:24'),(20,49,2,5,'','2025-06-03 09:27:30');
/*!40000 ALTER TABLE `Avaliacao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Bibliotecario`
--

DROP TABLE IF EXISTS `Bibliotecario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Bibliotecario` (
  `pessoa_id` int NOT NULL,
  `codigo` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`pessoa_id`),
  CONSTRAINT `Bibliotecario_ibfk_1` FOREIGN KEY (`pessoa_id`) REFERENCES `Pessoa` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Bibliotecario`
--

LOCK TABLES `Bibliotecario` WRITE;
/*!40000 ALTER TABLE `Bibliotecario` DISABLE KEYS */;
INSERT INTO `Bibliotecario` VALUES (19,'852');
/*!40000 ALTER TABLE `Bibliotecario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Emprestimo`
--

DROP TABLE IF EXISTS `Emprestimo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Emprestimo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `material_id` int NOT NULL,
  `aluno_id` int NOT NULL,
  `data_emprestimo` datetime DEFAULT NULL,
  `data_devolucao_prevista` datetime DEFAULT NULL,
  `data_devolucao_real` datetime DEFAULT NULL,
  `status` enum('Aprovado','Pendente','Devolvido','Renovado','Rejeitado') NOT NULL DEFAULT 'Pendente',
  `data_criacao` datetime DEFAULT CURRENT_TIMESTAMP,
  `mensagem` varchar(255) DEFAULT NULL,
  `rejicao_motivo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `material_id` (`material_id`),
  KEY `aluno_id` (`aluno_id`),
  CONSTRAINT `Emprestimo_ibfk_1` FOREIGN KEY (`material_id`) REFERENCES `Material_fisico` (`id`),
  CONSTRAINT `Emprestimo_ibfk_2` FOREIGN KEY (`aluno_id`) REFERENCES `Aluno` (`pessoa_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Emprestimo`
--

LOCK TABLES `Emprestimo` WRITE;
/*!40000 ALTER TABLE `Emprestimo` DISABLE KEYS */;
INSERT INTO `Emprestimo` VALUES (1,33,2,'2025-05-24 16:33:28','2025-05-28 00:00:00','2025-05-24 19:23:22','Devolvido','2025-05-24 15:53:55',NULL,NULL),(2,34,2,NULL,NULL,NULL,'Rejeitado','2025-05-24 16:55:20',NULL,NULL),(3,33,2,NULL,NULL,NULL,'Rejeitado','2025-05-24 19:31:33','Emprestimo rejeitado por determinados motivos',NULL),(4,33,2,'2025-05-25 23:40:24','2025-05-26 00:00:00',NULL,'Aprovado','2025-05-25 15:48:40',NULL,''),(5,34,2,'2025-05-25 16:55:54','2025-05-28 00:00:00',NULL,'Aprovado','2025-05-25 15:49:21',NULL,''),(6,36,2,'2025-05-27 20:41:24','2025-05-29 00:00:00',NULL,'Aprovado','2025-05-26 20:02:45',NULL,NULL),(7,39,2,NULL,NULL,NULL,'Rejeitado','2025-05-27 21:11:13',NULL,'Material com alta busca'),(8,39,2,'2025-05-28 08:38:09','2025-05-31 00:00:00',NULL,'Aprovado','2025-05-27 21:49:43',NULL,NULL),(9,40,2,NULL,NULL,NULL,'Pendente','2025-05-28 08:37:14',NULL,NULL),(10,41,2,NULL,NULL,NULL,'Pendente','2025-05-29 21:11:22',NULL,NULL),(11,37,2,NULL,NULL,NULL,'Pendente','2025-05-30 22:56:00',NULL,NULL);
/*!40000 ALTER TABLE `Emprestimo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Formato_material`
--

DROP TABLE IF EXISTS `Formato_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Formato_material` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `tipo` enum('Fisico','Digital') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Formato_material`
--

LOCK TABLES `Formato_material` WRITE;
/*!40000 ALTER TABLE `Formato_material` DISABLE KEYS */;
INSERT INTO `Formato_material` VALUES (1,'Audio Livro','Digital'),(2,'E-book','Digital'),(3,'Vídeo','Digital'),(4,'Jogo','Digital'),(5,'Revista','Fisico'),(6,'Livro','Fisico'),(7,'Material Didático','Fisico');
/*!40000 ALTER TABLE `Formato_material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Material`
--

DROP TABLE IF EXISTS `Material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Material` (
  `id` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(100) NOT NULL,
  `autor` varchar(100) DEFAULT NULL,
  `formato_material` int NOT NULL,
  `area_conhecimento` int NOT NULL,
  `nivel_conhecimento` enum('Básico','Intermediário','Avançado') NOT NULL,
  `nota` double(3,1) DEFAULT NULL,
  `adicionado_em` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `quantidade_avaliacao` int DEFAULT NULL,
  `descricao` varchar(200) NOT NULL,
  `cadastrado_por` int DEFAULT NULL,
  `tipo` enum('Fisico','Digital') NOT NULL,
  `uso` int DEFAULT '0',
  `listado` tinyint(1) DEFAULT '1',
  `capaUrl` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `formato_material` (`formato_material`),
  KEY `area_conhecimento` (`area_conhecimento`),
  KEY `fk_material_pessoa` (`cadastrado_por`),
  CONSTRAINT `fk_material_pessoa` FOREIGN KEY (`cadastrado_por`) REFERENCES `Pessoa` (`id`),
  CONSTRAINT `Material_ibfk_1` FOREIGN KEY (`formato_material`) REFERENCES `Formato_material` (`id`),
  CONSTRAINT `Material_ibfk_2` FOREIGN KEY (`area_conhecimento`) REFERENCES `Area_conhecimento` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Material`
--

LOCK TABLES `Material` WRITE;
/*!40000 ALTER TABLE `Material` DISABLE KEYS */;
INSERT INTO `Material` VALUES (45,'The Essential Galileo','Galileu Galilei',1,1,'Básico',4.0,'2025-05-24 15:53:14',5,'Testando 123',2,'Fisico',0,1,NULL),(46,'The Essential Galileo','Galileu Galilei',1,1,'Básico',NULL,'2025-05-24 16:10:20',NULL,'Testando 123',2,'Fisico',1,0,NULL),(48,'Banco de Dados Relacionais: A Bíblia ','Eber Chagas',6,8,'Básico',NULL,'2025-05-27 21:07:08',NULL,'Material completo de banco de dados relacionais e sql.',2,'Fisico',0,0,NULL),(49,'Materia testeeeeeeee','dsadsdasd',3,2,'Avançado',4.1,'2025-05-30 22:59:53',11,'sjksfkkjdsjfhjldk djhjdslsg',2,'Digital',1,1,NULL),(50,'teste de capa fisico','Eber Chagas',7,8,'Intermediário',NULL,'2025-06-03 10:30:41',NULL,'kjdks hufdsfg sgfydgfisad',2,'Fisico',0,1,'https://storage.googleapis.com/api-bibliotech.firebasestorage.app/145b0768-efea-4509-9b7c-0bf64c148e83-classe.jpg');
/*!40000 ALTER TABLE `Material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Material_digital`
--

DROP TABLE IF EXISTS `Material_digital`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Material_digital` (
  `material_id` int NOT NULL AUTO_INCREMENT,
  `link` varchar(200) NOT NULL,
  PRIMARY KEY (`material_id`),
  CONSTRAINT `Material_digital_ibfk_1` FOREIGN KEY (`material_id`) REFERENCES `Material` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Material_digital`
--

LOCK TABLES `Material_digital` WRITE;
/*!40000 ALTER TABLE `Material_digital` DISABLE KEYS */;
INSERT INTO `Material_digital` VALUES (49,'https://storage.googleapis.com/api-bibliotech.firebasestorage.app/bfc8c337-affb-42f4-b788-52b568a2fcd5-estrutura_composta.png');
/*!40000 ALTER TABLE `Material_digital` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Material_fisico`
--

DROP TABLE IF EXISTS `Material_fisico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Material_fisico` (
  `id` int NOT NULL AUTO_INCREMENT,
  `material_id` int NOT NULL,
  `disponibilidade` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `Material_fisico_ibfk_1` (`material_id`),
  CONSTRAINT `Material_fisico_ibfk_1` FOREIGN KEY (`material_id`) REFERENCES `Material` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Material_fisico`
--

LOCK TABLES `Material_fisico` WRITE;
/*!40000 ALTER TABLE `Material_fisico` DISABLE KEYS */;
INSERT INTO `Material_fisico` VALUES (33,45,0),(34,45,0),(35,45,0),(36,46,0),(37,46,0),(38,46,0),(39,48,0),(40,48,0),(41,48,0),(42,50,1);
/*!40000 ALTER TABLE `Material_fisico` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Necessidade`
--

DROP TABLE IF EXISTS `Necessidade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Necessidade` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Necessidade`
--

LOCK TABLES `Necessidade` WRITE;
/*!40000 ALTER TABLE `Necessidade` DISABLE KEYS */;
INSERT INTO `Necessidade` VALUES (1,'Deficiência Auditiva'),(2,'Deficiência Física'),(3,'Deficiência Intelectual'),(4,'Deficiência Múltipla'),(5,'Deficiência Visual'),(6,'Transtorno do Espectro Autista'),(7,'Transtorno de Déficit de Atenção e Hiperatividade');
/*!40000 ALTER TABLE `Necessidade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Pessoa`
--

DROP TABLE IF EXISTS `Pessoa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Pessoa` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `email` varchar(145) NOT NULL,
  `senha` char(60) NOT NULL,
  `aprovado` tinyint(1) DEFAULT '0',
  `solicitado` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Pessoa`
--

LOCK TABLES `Pessoa` WRITE;
/*!40000 ALTER TABLE `Pessoa` DISABLE KEYS */;
INSERT INTO `Pessoa` VALUES (2,'Emerson Neves','ana.souza@ifbaiano.edu.br','$2a$12$tPjidLdj9CAoh5lp5KlrEuryQ3AOmqhwD.OdoqpspFz3FPIu91Yta',1,'2025-05-25 15:42:08'),(5,'Emerson Neves','sjfhjhhsf@ifbaiano.edu.br','$2a$12$kMReh7jKBmk8eFDQUFBxxu4MkBXZXFz7HLltja0OWk/z.YN4mVGCW',1,'2025-05-25 15:42:08'),(9,'Teste Professor web','idenevessantos@hotmail.com','$2a$12$hFsNtF.mje0T2DjEBUEpOeffx/mUYhy8hrXBJ9r4otDbis30gCkSK',1,'2025-05-25 15:42:08'),(16,'Emerson Neves','colgatesorriso111@gmail.com','$2a$12$CMv7vSupxuv4JW7PrbqzSe84J5o.PS9yu08Y/vBEzLHcRwogBOXbe',1,'2025-05-27 21:34:56'),(17,'cadastro teste','emailteste@gmail.com','$2a$12$I.LBC2cI.rQdv3BmpFhKp.b0yDb/5oLbp76ftLA1zYK5tSqhuWQpS',0,'2025-05-28 17:05:32'),(19,'super admin','super@gmail.com','$2a$12$P9YkNjgQcnTh1zafFTPNauIUGeJhRN87p3VCsLBaccQhd1gzJs91i',1,'2025-05-28 17:10:05'),(20,'Teste Professor web','sadasdsdfd@gmail.com','$2a$12$eFW54MuNnYKLFRuB7w1D5.Gpgc70e1BOaMhKCIlIuXGi2WfPReOLG',0,'2025-05-28 22:21:13'),(21,'apagar','dsdsds@gmail.com','$2a$12$AvelvRb0nf1nEab296ptpuyaNjhxHkF4Kw1X9HCwBAjVQenc298ni',0,'2025-05-28 22:25:25'),(22,'fsdfsdfsfsd','oppopop@hotmail.com','$2a$12$SVKESNqYDMa9wUrcwHLcv.6TlaNEqcU3vyMhCf33MnojY1SkQ780m',0,'2025-05-28 22:39:54'),(23,'Vitor Sérgio Rodrigues','vitor.ser@gmail.com','$2a$12$9gL/UpDcvOk/8eCfsRu25OB6mObtCN5JXhZTjqttaHEMe9wIcck5q',0,'2025-06-02 14:58:22');
/*!40000 ALTER TABLE `Pessoa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Professor`
--

DROP TABLE IF EXISTS `Professor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Professor` (
  `pessoa_id` int NOT NULL,
  `siap` varchar(10) NOT NULL,
  PRIMARY KEY (`pessoa_id`),
  UNIQUE KEY `siap` (`siap`),
  CONSTRAINT `Professor_ibfk_1` FOREIGN KEY (`pessoa_id`) REFERENCES `Pessoa` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Professor`
--

LOCK TABLES `Professor` WRITE;
/*!40000 ALTER TABLE `Professor` DISABLE KEYS */;
INSERT INTO `Professor` VALUES (22,'12222'),(17,'123'),(23,'123456'),(9,'424224'),(16,'7654321'),(21,'787887');
/*!40000 ALTER TABLE `Professor` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-03 11:19:55
