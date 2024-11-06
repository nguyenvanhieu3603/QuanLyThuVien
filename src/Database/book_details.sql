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
-- Table structure for table `book_details`
--

DROP TABLE IF EXISTS `book_details`;
CREATE TABLE IF NOT EXISTS `book_details` (
  `serialID` int NOT NULL AUTO_INCREMENT,
  `bookID` varchar(10) COLLATE utf8mb3_unicode_ci NOT NULL,
  `bookName` varchar(50) COLLATE utf8mb3_unicode_ci NOT NULL,
  `category` varchar(50) COLLATE utf8mb3_unicode_ci NOT NULL,
  `author` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `publisher` varchar(50) COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`bookID`),
  UNIQUE KEY `serialID` (`serialID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

--
-- Dumping data for table `book_details`
--

INSERT INTO `book_details` (`serialID`, `bookID`, `bookName`, `category`, `author`, `publisher`, `quantity`) VALUES
(1, 'B1010', 'Báo cáo chỉ số thương mại điện tử 2023', 'Sách tham khảo ', NULL, 'Hiệp hội Thương mại điện tử Việt Nam', 5),
(2, 'B1011', 'Toán cao cấp A1', 'Giáo trình', 'Đỗ Văn Nhơn', 'Đại học Quốc gia TP. HCM', 3),
(3, 'B1012', 'Quản lý dự án CNTT', 'Giáo trình', 'Ngô Việt Trung', 'Đại Học Quốc Gia TP.HCM', 5),
(4, 'B1013', 'Handbook of dynamic system modeling', 'Sách tham khảo', 'A. Fishwick, Paul', NULL, 5),
(5, 'B1014', 'Sách trắng Thương mại điện tử', 'Sách tham khảo', NULL, 'Cục Thương mại điện tử và Kinh tế số', 7),
(6, 'B1015', 'Hệ quản trị cơ sở dữ liệu Oracle', 'Giáo trình', 'Nguyễn Thị Trà Linh', 'Đại học Quốc gia TP. HCM', 3),
(7, 'B1016', 'Pro PHP XML and web services', 'Sách tham khảo', 'Richards, Robert', NULL, 2);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
