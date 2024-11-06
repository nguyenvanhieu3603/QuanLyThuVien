-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 12, 2024 at 04:44 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

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
-- Table structure for table `issue_book_details`
--

CREATE TABLE `issue_book_details` (
  `id` int(11) NOT NULL,
  `bookID` varchar(10) DEFAULT NULL,
  `bookName` varchar(150) DEFAULT NULL,
  `studentID` varchar(8) DEFAULT NULL,
  `studentName` varchar(50) DEFAULT NULL,
  `issueDate` date DEFAULT NULL,
  `dueDate` date DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `issue_book_details`
--

INSERT INTO `issue_book_details` (`id`, `bookID`, `bookName`, `studentID`, `studentName`, `issueDate`, `dueDate`, `status`) VALUES
(3, 'B1010', 'Báo cáo chỉ số thương mại điện tử 2023', 'ID1010', 'Nguyễn Huy A', '2024-05-12', '2024-05-16', 'Đang mượn');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `issue_book_details`
--
ALTER TABLE `issue_book_details`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `issue_book_details`
--
ALTER TABLE `issue_book_details`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
