-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 08, 2020 at 02:19 PM
-- Server version: 10.3.16-MariaDB
-- PHP Version: 7.3.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `blogair_db`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `PostAddOrEdit` (IN `_pid` VARCHAR(20), IN `_desc` TEXT, IN `_postImage` TEXT, IN `_likesCount` INT(255), IN `timeStamp` DATETIME, IN `_uid` VARCHAR(20))  MODIFIES SQL DATA
SELECT * from post$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `UserAddOrEdit` (IN `_uid` VARCHAR(20), IN `_emailId` VARCHAR(50), IN `_thumbIMage` TEXT, IN `_fullName` VARCHAR(50), IN `_country` VARCHAR(20), IN `_dob` DATETIME, IN `_timeStamp` DATETIME)  MODIFIES SQL DATA
if EXISTS(select * from user where uid= _uid ) then update user set email_id = _emailID , thumb_image = _thumbImage , full_name= _fullName, country = _country, dob = _dob, time_stamp = _timeStamp where uid = _uid ; ELSE INSERT INTO user (uid,email_id,thumb_image,full_name,country,dob,time_stamp) VALUES (_uid,_emailId,_thumbImage,_fullName,_country,_dob,_timeStamp); end if$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `follow`
--

CREATE TABLE `follow` (
  `fid` varchar(50) NOT NULL,
  `follower_uid` varchar(50) DEFAULT NULL,
  `following_uid` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `follow`
--

INSERT INTO `follow` (`fid`, `follower_uid`, `following_uid`) VALUES
('1', '1', '2'),
('2', '1', '3');

-- --------------------------------------------------------

--
-- Table structure for table `post`
--

CREATE TABLE `post` (
  `pid` varchar(20) NOT NULL,
  `desc` text DEFAULT NULL,
  `post_image` text NOT NULL,
  `likes_count` int(255) DEFAULT NULL,
  `time_stamp` datetime NOT NULL DEFAULT current_timestamp(),
  `uid` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `post`
--

INSERT INTO `post` (`pid`, `desc`, `post_image`, `likes_count`, `time_stamp`, `uid`) VALUES
('1', 'This is a first sample post description', 'https://images-na.ssl-images-amazon.com/images/I/41JOpEMJsDL.jpg', 2510, '2020-01-07 17:17:36', '1'),
('10', 'This is a tenth sample post description', 'https://images-na.ssl-images-amazon.com/images/I/41JOpEMJsDL.jpg', 25610, '2020-01-07 17:17:36', '3'),
('2', 'This is a second sample post description', 'https://images-na.ssl-images-amazon.com/images/I/41JOpEMJsDL.jpg', 250, '2020-01-07 17:17:36', '1'),
('3', 'This is a third sample post description', 'https://images-na.ssl-images-amazon.com/images/I/41JOpEMJsDL.jpg', 25170, '2020-01-07 17:17:36', '2'),
('4', 'This is a forth sample post description', 'https://images-na.ssl-images-amazon.com/images/I/41JOpEMJsDL.jpg', 25170, '2020-01-07 17:17:36', '3'),
('5', 'This is a fifth sample post description', 'https://images-na.ssl-images-amazon.com/images/I/41JOpEMJsDL.jpg', 25610, '2020-01-07 17:17:36', '3'),
('6', 'This is a sixth sample post description', 'https://images-na.ssl-images-amazon.com/images/I/41JOpEMJsDL.jpg', 2510, '2020-01-07 17:17:36', '1'),
('7', 'This is a seventh sample post description', 'https://images-na.ssl-images-amazon.com/images/I/41JOpEMJsDL.jpg', 250, '2020-01-07 17:17:36', '1'),
('8', 'This is a eight sample post description', 'https://images-na.ssl-images-amazon.com/images/I/41JOpEMJsDL.jpg', 25170, '2020-01-07 17:17:36', '2'),
('9', 'This is a ninth sample post description', 'https://images-na.ssl-images-amazon.com/images/I/41JOpEMJsDL.jpg', 25170, '2020-01-07 17:17:36', '3');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `uid` varchar(20) NOT NULL,
  `email_id` varchar(50) NOT NULL,
  `thumb_image` text NOT NULL,
  `full_name` varchar(50) DEFAULT NULL,
  `country` varchar(20) DEFAULT NULL,
  `dob` datetime NOT NULL,
  `time_stamp` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`uid`, `email_id`, `thumb_image`, `full_name`, `country`, `dob`, `time_stamp`) VALUES
('1', 'rsrsingh420@gmail.com', 'https://www.laptopmag.com/images/uploads/4427/g/apple-macbook-air-13inch-w-g03.jpg', 'Randeep Singh', 'india', '1997-12-13 00:00:00', '2020-01-05 17:46:36'),
('2', 'divesh@gmail.com', 'https://www.laptopmag.com/images/uploads/4427/g/apple-macbook-air-13inch-w-g03.jpg', 'Divesh Thareja', 'india', '1996-12-12 18:30:00', '2019-01-05 12:16:36'),
('3', 'rahul.gaur152@gmail.com', 'https://www.laptopmag.com/images/uploads/4427/g/apple-macbook-air-13inch-w-g03.jpg', 'Rahul Gaur', 'india', '1996-12-12 18:30:00', '2019-01-05 12:16:36'),
('4', 'singh40@gmail.com', 'https://www.laptopmag.com/images/uploads/4427/g/apple-macbook-air-13inch-w-g03.jpg', 'Naman Singh', 'india', '1997-12-12 18:30:00', '2020-01-05 12:16:36'),
('5', 'abc@example.com', 'https://www.laptopmag.com/images/uploads/4427/g/apple-macbook-air-13inch-w-g03.jpg', 'abc def', 'India', '1996-12-12 13:00:00', '2019-01-05 06:46:36');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `follow`
--
ALTER TABLE `follow`
  ADD PRIMARY KEY (`fid`);

--
-- Indexes for table `post`
--
ALTER TABLE `post`
  ADD PRIMARY KEY (`pid`),
  ADD KEY `uid` (`uid`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`uid`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `post`
--
ALTER TABLE `post`
  ADD CONSTRAINT `post_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
