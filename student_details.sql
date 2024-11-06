-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: May 07, 2024 at 09:06 AM
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
  `studentID` varchar(8) COLLATE utf8mb3_unicode_ci NOT NULL,
  `studentName` varchar(50) COLLATE utf8mb3_unicode_ci NOT NULL,
  `gender` varchar(5) COLLATE utf8mb3_unicode_ci NOT NULL,
  `birthday` date NOT NULL,
  `studentEmail` varchar(20) COLLATE utf8mb3_unicode_ci NOT NULL,
  `major` varchar(10) COLLATE utf8mb3_unicode_ci NOT NULL,
  PRIMARY KEY (`studentID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

--
-- Dumping data for table `student_details`
--

INSERT INTO `student_details` (`studentID`, `studentName`, `gender`, `birthday`, `studentEmail`, `major`) VALUES
('ID1010', 'Nguyễn Huy A', 'Nam', '2024-05-06', 'hoang@gm.com', 'CNTT'),
('ID1011', 'Phạm Ngọc Anh', 'Nữ', '2024-05-04', 'anhngoc@gm.com', 'KHMT'),
('ID1012', 'Trần Như Quỳnh', 'Nữ', '2024-05-01', 'nhuquynh@gm.com', 'HTTT'),
('ID1013', 'Nguyễn Đăng Khoa', 'Nam', '2024-05-02', 'dangkh@gm.com', 'CNTT'),
('ID1014', 'Phan Tấn Hữu', 'Nam', '2024-05-07', 'tanhuu@gm.com', 'KHMT'),
('ID1017', 'Lê Hoàng Kiên', 'Nam', '2024-04-13', 'kienle@gm.com', 'HTTT'),
('ID1019', 'Lan', 'Nữ', '2024-12-24', 'lan123@gm.com', 'HTTT');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
