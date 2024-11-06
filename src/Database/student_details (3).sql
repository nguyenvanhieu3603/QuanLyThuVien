-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: May 10, 2024 at 04:01 PM
-- Server version: 8.2.0
-- PHP Version: 8.2.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `quanlythuvien`
--

-- --------------------------------------------------------

--
-- Table structure for table `student_details`
--

DROP TABLE IF EXISTS `student_details`;
CREATE TABLE IF NOT EXISTS `student_details` (
  `serialID` int NOT NULL AUTO_INCREMENT,
  `studentID` varchar(8) COLLATE utf8mb3_unicode_ci NOT NULL,
  `studentName` varchar(50) COLLATE utf8mb3_unicode_ci NOT NULL,
  `gender` varchar(5) COLLATE utf8mb3_unicode_ci NOT NULL,
  `birthday` date NOT NULL,
  `studentEmail` varchar(20) COLLATE utf8mb3_unicode_ci NOT NULL,
  `major` varchar(10) COLLATE utf8mb3_unicode_ci NOT NULL,
  PRIMARY KEY (`studentID`),
  UNIQUE KEY `STT` (`serialID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

--
-- Dumping data for table `student_details`
--

INSERT INTO `student_details` (`serialID`, `studentID`, `studentName`, `gender`, `birthday`, `studentEmail`, `major`) VALUES
(1, 'ID1010', 'Nguyễn Huy A', 'Nam', '2024-05-06', 'hoang@gm.com', 'CNTT'),
(2, 'ID1011', 'Phạm Ngọc Anh', 'Nữ', '2024-05-04', 'anhngoc@gm.com', 'KHMT'),
(7, 'ID1012', 'Trần Thị B', 'Nữ', '2024-05-03', 'thib@gm.com', 'HTTT'),
(3, 'ID1013', 'Trần Như Quỳnh', 'Nữ', '2024-05-01', 'nhuquynh@gm.com', 'HTTT'),
(4, 'ID1014', 'Nguyễn Đăng Khoa', 'Nam', '2024-05-02', 'dangkh@gm.com', 'CNTT'),
(5, 'ID1016', 'Nguyễn Thị Lan', 'Nữ', '2024-05-03', 'lan456@gm.com', 'KHMT'),
(6, 'ID1017', 'Lê Hoàng Kiên', 'Nam', '2024-04-13', 'kienle@gm.com', 'HTTT');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
