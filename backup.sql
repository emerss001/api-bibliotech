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
INSERT INTO `Aluno` VALUES (25,'2024005',9,0),(30,'11111111',9,0);
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
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Area_conhecimento`
--

LOCK TABLES `Area_conhecimento` WRITE;
/*!40000 ALTER TABLE `Area_conhecimento` DISABLE KEYS */;
INSERT INTO `Area_conhecimento` VALUES (10,'Matemática'),(11,'Língua Portuguesa'),(12,'Inglês'),(13,'Espanhol'),(14,'Informática'),(15,'Educação Financeira'),(16,'Ciências da Natureza'),(17,'Ciências Sociais'),(18,'Filosofia'),(19,'Sociologia');
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Avaliacao`
--

LOCK TABLES `Avaliacao` WRITE;
/*!40000 ALTER TABLE `Avaliacao` DISABLE KEYS */;
INSERT INTO `Avaliacao` VALUES (3,53,25,5,'Ótimo material, com fontes que ajudam na leitura','2025-06-03 22:54:51'),(4,55,25,5,'Material super explicativo','2025-06-04 00:40:03'),(5,55,25,2,'teste de apagar','2025-06-12 18:47:23'),(6,55,25,2,'teste de apagar','2025-06-12 18:47:48');
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
INSERT INTO `Bibliotecario` VALUES (24,'852'),(29,'123456');
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
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Emprestimo`
--

LOCK TABLES `Emprestimo` WRITE;
/*!40000 ALTER TABLE `Emprestimo` DISABLE KEYS */;
INSERT INTO `Emprestimo` VALUES (1,43,25,'2025-06-04 00:44:56','2025-06-07 00:00:00','2025-06-04 10:34:17','Devolvido','2025-06-03 23:46:12',NULL,NULL),(2,44,25,NULL,NULL,NULL,'Rejeitado','2025-06-04 00:13:52',NULL,'teste de rejeição'),(3,45,25,NULL,NULL,NULL,'Rejeitado','2025-06-04 00:30:56','teste de mensagem','rejeitado'),(4,44,25,'2025-06-04 00:49:18','2025-06-12 00:00:00',NULL,'Aprovado','2025-06-04 00:48:14','Trabalho de poo',NULL),(5,45,25,'2025-06-04 10:31:29','2025-06-11 00:00:00','2025-06-10 20:42:55','Devolvido','2025-06-04 10:29:27','Trabalho de poo',NULL),(20,61,25,NULL,NULL,NULL,'Pendente','2025-06-11 00:19:34',NULL,NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Formato_material`
--

LOCK TABLES `Formato_material` WRITE;
/*!40000 ALTER TABLE `Formato_material` DISABLE KEYS */;
INSERT INTO `Formato_material` VALUES (8,'Texto com Leitura fácil','Fisico'),(9,'Textos ampliados','Fisico'),(10,'Vídeos com Libras','Fisico'),(11,'Vídeos com Áudio Descritivo','Fisico'),(12,'Materiais em Braille','Fisico'),(13,'Materiais em Audiodescrição','Fisico'),(14,'Materiais em Língua de Sinais','Fisico');
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
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Material`
--

LOCK TABLES `Material` WRITE;
/*!40000 ALTER TABLE `Material` DISABLE KEYS */;
INSERT INTO `Material` VALUES (53,'Banco de Dados Relacionais: A Bíblia ','Eber Chagas',9,14,'Intermediário',5.0,'2025-06-03 22:52:06',1,'Apostila sobre banco de dados relacionais com texto ampliado focado para pessoas com baixa visão',24,'Digital',11,1,'https://storage.googleapis.com/api-bibliotech.firebasestorage.app/3fa1b6be-c604-4ead-82d9-858dce3b9370-1.webp'),(54,'Biologia Celular: Um mundo microscópico  ','Galileu Galiei',8,16,'Avançado',NULL,'2025-06-03 23:36:42',NULL,'Texto com leitura fácil sobre microbiologia celular, apostia avançada ',29,'Fisico',1,1,'https://storage.googleapis.com/api-bibliotech.firebasestorage.app/780d9f9d-ebf3-4818-a901-dde696db434e-capa_baixa_5-204x300.jpg'),(55,'POO com Java avançado ','Woquiton',12,14,'Intermediário',3.0,'2025-06-04 00:12:14',3,'Programação orientada a objetos com java, o mundo do horror ',29,'Fisico',6,1,'https://storage.googleapis.com/api-bibliotech.firebasestorage.app/c5a5d160-aede-4e70-9f6a-4aa702360b5f-LAB-POO-IME-USP.jpg'),(62,'teste emprestimo','dsadsdasd',13,19,'Intermediário',NULL,'2025-06-06 19:22:42',NULL,'jkdd uwqegw wgehkwq',24,'Fisico',5,1,'');
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
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Material_digital`
--

LOCK TABLES `Material_digital` WRITE;
/*!40000 ALTER TABLE `Material_digital` DISABLE KEYS */;
INSERT INTO `Material_digital` VALUES (53,'https://storage.googleapis.com/api-bibliotech.firebasestorage.app/6f32faff-8480-48ad-a1f8-9138aa99f872-ATIVIDADE_SOBRE_TRABALHO_EM_EQUIPE_ENVOLVENDO_OS_PILARES_DO_PENSAMENTO_COMPTACIONAL.pdf');
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
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Material_fisico`
--

LOCK TABLES `Material_fisico` WRITE;
/*!40000 ALTER TABLE `Material_fisico` DISABLE KEYS */;
INSERT INTO `Material_fisico` VALUES (43,54,1),(44,55,0),(45,55,1),(46,55,0),(47,55,0),(60,62,1),(61,62,1),(62,62,1),(63,62,1),(64,62,1),(65,62,1),(66,62,1),(67,62,1),(68,62,1),(69,62,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Necessidade`
--

LOCK TABLES `Necessidade` WRITE;
/*!40000 ALTER TABLE `Necessidade` DISABLE KEYS */;
INSERT INTO `Necessidade` VALUES (7,''),(8,'Deficiência Auditiva'),(9,'Deficiência Visual'),(10,'Deficiência Intelectual'),(11,'Transtorno do Espectro Autista'),(12,'Transtorno de Déficit de Atenção e Hiperatividade'),(13,'Deficiência Múltipla');
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
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Pessoa`
--

LOCK TABLES `Pessoa` WRITE;
/*!40000 ALTER TABLE `Pessoa` DISABLE KEYS */;
INSERT INTO `Pessoa` VALUES (24,'ADMIN','admin.admin@gmail.com','$2a$12$IHmMMFWyP8jpIFzscUEjROWnN.RpucRG9UDidVOl6aybBsIj6EDYW',1,'2025-06-03 17:00:27'),(25,'João Pereira Carvalho','carvalhojoao@gmail.com','$2a$12$bkumlxqsOumTNknpRfGTFukVYaqsb4Jz.XxElFcyr6HzkeZUL/3z6',1,'2025-06-03 22:11:42'),(27,'Carlos Vianna','vaianacarlos@hotmail.com','$2a$12$ZJBXgOMdaoqm4a/XeQZuBeRAq2BE5ByuSB775SoK.21pHvFSgd9Ci',1,'2025-06-03 23:17:52'),(29,'Maria Santana','sadasdsdfd@gmail.com','$2a$12$yJjxOla9LnbSyg0CdX9t..4eg1R6k14TzhQiF7ZaZsvq5Jd4NB6mm',1,'2025-06-03 23:33:12'),(30,'Maria Santana','maria123@gmail.com','$2a$12$N3.CzYxMP3bG3QZDrrrLX.oS3FX2.HCE01dHhHh5SHJZWoZ3IB2Ru',1,'2025-06-04 10:33:00'),(31,'Woquiton Fernandes','emersonneves08@gmail.com','$2a$12$D.KSOABMtMG03b/09KYciO89iIoLICgKP1VnczlGuG1o6ZZ66lzbW',1,'2025-06-04 10:37:17'),(32,'Santana Maria','santana.maria@ifbaiano.edu.br','$2a$12$rh1VWXSwg2Rpcfmy6QV7U..X6roK41likS9CX9CwXXlJ/adJP9H3a',1,'2025-06-11 08:17:07');
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
INSERT INTO `Professor` VALUES (27,'11111111'),(31,'1234567'),(32,'741852');
/*!40000 ALTER TABLE `Professor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'Bibliotech'
--
/*!50003 DROP PROCEDURE IF EXISTS `GetMaterialCompleto` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE PROCEDURE `GetMaterialCompleto`(IN mat_id int)
BEGIN
    DECLARE mat_tipo ENUM('Fisico', 'Digital');

    -- Buscar o tipo do material
    SELECT tipo INTO mat_tipo
    FROM Material
    WHERE id = mat_id;

    -- Verifica e executa o SELECT apropriado
    IF mat_tipo = 'Fisico' THEN
        SELECT
            m.autor,
            m.tipo,
            m.titulo,
            f.nome as formato_material,
            a.nome as area_conhecimento,
            m.nivel_conhecimento,
            m.descricao,
            m.cadastrado_por,
            m.adicionado_em,
            m.nota,
            m.quantidade_avaliacao,
            MAX(mf.disponibilidade) AS disponibilidade,
            m.capaUrl
        FROM Material m
                 JOIN Material_fisico mf ON m.id = mf.material_id
                join Formato_material AS f on f.id = m.formato_material
                join Area_conhecimento AS a on a.id = m.area_conhecimento
        WHERE m.id = mat_id
        GROUP BY m.id;

    ELSEIF mat_tipo = 'Digital' THEN
        SELECT
            m.autor,
            m.tipo,
            m.titulo,
            f.nome as formato_material,
            a.nome as area_conhecimento,
            m.nivel_conhecimento,
            m.descricao,
            m.cadastrado_por,
            m.adicionado_em,
            m.nota,
            m.quantidade_avaliacao,
            md.link,
            m.capaUrl
        FROM Material m
                 JOIN Material_digital md ON m.id = md.material_id
                 join Formato_material AS f on f.id = m.formato_material
                 join Area_conhecimento AS a on a.id = m.area_conhecimento
        WHERE m.id = mat_id;

    ELSE
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Tipo de material desconhecido ou material não encontrado';
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-12 18:49:30
