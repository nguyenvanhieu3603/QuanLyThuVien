CREATE DATABASE quanlythuvien;

USE quanlythuvien;

CREATE TABLE users(id int PRIMARY KEY NOT null AUTO_INCREMENT, name varchar(50), password varchar(50), email varchar(100), contact varchar(20));

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

create table issue_book_details(id int PRIMARY KEY not null AUTO_INCREMENT, book_id int, book_name varchar(150), student_id int, student_name varchar(50), issue_date date, due_date date, status varchar(50));