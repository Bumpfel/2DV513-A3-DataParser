-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Jan 11, 2020 at 07:41 PM
-- Server version: 10.4.10-MariaDB
-- PHP Version: 7.3.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `imdb_data_test`
--

-- --------------------------------------------------------

--
-- Table structure for table `genres`
--

DROP TABLE IF EXISTS `genres`;
CREATE TABLE IF NOT EXISTS `genres` (
  `id` smallint(2) NOT NULL,
  `genreName` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `genreName` (`genreName`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `genretitlerelations`
--

DROP TABLE IF EXISTS `genretitlerelations`;
CREATE TABLE IF NOT EXISTS `genretitlerelations` (
  `titleId` varchar(10) NOT NULL,
  `genreId` smallint(2) NOT NULL,
  KEY `titleId` (`titleId`),
  KEY `genreId` (`genreId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `titles`
--

DROP TABLE IF EXISTS `titles`;
CREATE TABLE IF NOT EXISTS `titles` (
  `id` varchar(10) NOT NULL,
  `titleType` varchar(12) NOT NULL,
  `primaryTitle` varchar(410) NOT NULL,
  `originalTitle` varchar(410) NOT NULL,
  `isAdult` tinyint(1) NOT NULL,
  `startYear` mediumint(4) NOT NULL,
  `endYear` mediumint(4) NOT NULL,
  `runtimeMinutes` mediumint(3) NOT NULL,
  `averageRating` decimal(2,0) NOT NULL,
  `numVotes` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `titleType` (`titleType`),
  KEY `primaryTitle` (`primaryTitle`),
  KEY `primaryTitle` (`originalTitle`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `genretitlerelations`
--
ALTER TABLE `genretitlerelations`
  ADD CONSTRAINT `genretitlerelations_ibfk_1` FOREIGN KEY (`genreId`) REFERENCES `genres` (`id`),
  ADD CONSTRAINT `genretitlerelations_ibfk_2` FOREIGN KEY (`titleId`) REFERENCES `titles` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
