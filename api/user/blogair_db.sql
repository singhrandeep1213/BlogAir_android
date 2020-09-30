-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 30, 2020 at 09:17 AM
-- Server version: 10.4.14-MariaDB
-- PHP Version: 7.4.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `blogair_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `blocked_users`
--

CREATE TABLE `blocked_users` (
  `block_id` varchar(255) NOT NULL,
  `blocker_uid` varchar(255) NOT NULL,
  `blocked_uid` varchar(255) NOT NULL,
  `blocked_on` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `blocked_users`
--


-- --------------------------------------------------------

--
-- Table structure for table `bookmarks`
--

CREATE TABLE `bookmarks` (
  `bid` varchar(255) NOT NULL,
  `pid` varchar(255) DEFAULT NULL,
  `uid` varchar(255) DEFAULT NULL,
  `created_on` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `bookmarks`
--

INSERT INTO `bookmarks` (`bid`, `pid`, `uid`, `created_on`) VALUES
('1', '3', '1', '2020-09-21 08:56:44'),
('2', '1', '1', '2020-09-21 08:56:44'),
('3', '4', '1', '2020-09-21 08:56:44');

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
('2', '1', '3'),
('2361e2a1-c98a-4cb3-9745-9876cca5a8b7', '856ab421-6178-40e0-bee0-d533bd7a63a7', '1'),
('3', '1', '1'),
('5d6cbeb9-392f-408b-8304-3709e4da23e2', '7', '1'),
('6e46f4b5-fc01-49cf-9a06-bb01e6052698', '8afd25c9-686c-45dc-9706-74b372c1150c', '1'),
('73ab7e8a-998f-4b59-871b-3aca2560fd83', '7', '1'),
('aa6dba3e-94e3-4cf1-ac1a-27ab4627f3b4', 'ebb36262-9c2a-4e33-9a0f-4bb077efb81a', '1'),
('f63d0354-e189-49f6-ad72-139a5fe2bfae', '7', '1');

-- --------------------------------------------------------

--
-- Table structure for table `likes`
--

CREATE TABLE `likes` (
  `lid` varchar(255) CHARACTER SET utf8 NOT NULL,
  `pid` varchar(255) NOT NULL,
  `uid` varchar(255) NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `likes`
--

INSERT INTO `likes` (`lid`, `pid`, `uid`, `created_on`) VALUES
('1', '1', '2', '2020-09-28 18:54:28'),
('2', '1', '3', '2020-09-28 19:28:04'),
('3', '1', '4', '2020-09-28 19:30:58'),
('4', '1', '1', '2020-09-28 19:35:32'),
('5', '3', '1', '2020-09-28 19:40:12');

-- --------------------------------------------------------

--
-- Table structure for table `post`
--

CREATE TABLE `post` (
  `pid` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `desc` text CHARACTER SET utf8mb4 DEFAULT NULL,
  `post_image` text NOT NULL,
  `time_stamp` datetime NOT NULL DEFAULT current_timestamp(),
  `uid` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `post_heading` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `post`
--

INSERT INTO `post` (`pid`, `desc`, `post_image`, `time_stamp`, `uid`, `post_heading`) VALUES
('1', 'This is a first sample post description', 'post_image1043ca4e-8ddc-48c9-97b7-abe280983f73.jpg', '2020-01-07 17:17:36', '1', '1st post heading'),
('169f697d-27ef-44d6-8', '🤩🤩', 'post_image0e1da9f3-36f5-4638-a1e5-04e948bca30d.jpg', '2020-09-18 01:44:53', '1', 'hello😛'),
('3', 'This is a third sample post description', 'post_image73c08054-c5e7-4ad4-935f-4f822b18cb93.png', '2020-01-07 17:17:36', '2', '3rd post h eading'),
('3a1cf611-3b85-4d28-a64d-2f6ec1ea1235', '', 'post_imagee5fdf82b-0015-417b-b1d2-8c3db9a6ebf6.jpg', '2020-09-18 22:56:38', '1', ''),
('4', 'This is a forth sample post description', 'post_image262a9f72-681b-488c-a7c6-255e2d9c1e83.jpg', '2020-01-07 17:17:36', '3', '4th post heading'),
('5', 'This is a fifth sample post description', 'post_imageb7741dd7-058b-4f63-b40b-d152c82601de.jpg', '2020-01-07 17:17:36', '3', '5th post heading'),
('a5835a34-3635-4ed5-8525-c9146ccafedf', 'babbu mann 🤩', 'post_image2247f9cc-025f-476a-bda5-048213c76271.jpg', '2020-09-23 00:52:23', '1', 'ustaad'),
('c3584455-a17a-40af-aa1e-e06bdf630d9b', '', 'post_image_930ed5c2-3248-47bf-873a-9de216d78bf3.jpg', '2020-09-23 00:55:08', '1', 'cypher'),
('e1060624-b434-4677-8', '', 'post_imageb4394312-2dd9-46c9-a25e-81ef89a9ac06.jpg', '2020-09-18 21:50:14', '1', '');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `uid` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `email_id` varchar(50) NOT NULL,
  `thumb_image` text NOT NULL,
  `full_name` varchar(50) DEFAULT NULL,
  `country` varchar(20) DEFAULT NULL,
  `dob` datetime NOT NULL,
  `time_stamp` datetime NOT NULL DEFAULT current_timestamp(),
  `PASSWORD` varchar(255) DEFAULT NULL,
  `bio` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `is_public` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`uid`, `email_id`, `thumb_image`, `full_name`, `country`, `dob`, `time_stamp`, `PASSWORD`, `bio`, `is_public`) VALUES
('1', 'rsrsingh420@gmail.com', 'profile_image_d3fbc47d-f981-401e-b8dc-602b22a3e5f2.jpg', 'Randeep Singh', 'india', '1997-12-13 00:00:00', '2020-01-05 17:46:36', '1234', 'Hello there', 1),
('2', 'divesh@gmail.com', 'profile_image_643e6e86-d700-40c4-901b-84672a259b06.jpg', 'Divesh Thareja', 'india', '1996-12-12 18:30:00', '2019-01-05 12:16:36', '1234', 'hello I\'m divesh thareja.\nI am an artist.\nI am randeep\'s friend\nnice to meet you guys\n♥️♥️♥️', 1),
('3', 'rahul.gaur152@gmail.com', 'profile_image_5586f177-5b5d-4c8b-bbe0-1ea7b7f006b7.jpg', 'Rahul Gaur', 'india', '1996-12-12 18:30:00', '2019-01-05 12:16:36', '1234', 'Hello this is rahul gaur', 1),
('4', 'singh40@gmail.com', 'https://www.laptopmag.com/images/uploads/4427/g/apple-macbook-air-13inch-w-g03.jpg', 'Naman Singh', 'india', '1997-12-12 18:30:00', '2020-01-05 12:16:36', '1234', NULL, 1),
('5', 'abc@example.com', 'https://www.laptopmag.com/images/uploads/4427/g/apple-macbook-air-13inch-w-g03.jpg', 'abc def', 'India', '1996-12-12 13:00:00', '2019-01-05 06:46:36', '1234', NULL, 0),
('7', 'singhaz@gmail.com', '', 'Singhaz a', NULL, '0000-00-00 00:00:00', '2020-09-27 04:27:05', '1234', NULL, 1),
('856ab421-6178-40e0-bee0-d533bd7a63a7', 'singhagaming@gmail.com', '', 'singha gaming', NULL, '0000-00-00 00:00:00', '2020-09-23 02:45:19', '1234', NULL, 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `blocked_users`
--
ALTER TABLE `blocked_users`
  ADD PRIMARY KEY (`block_id`),
  ADD KEY `blocked_users_ibfk_1` (`blocked_uid`);

--
-- Indexes for table `bookmarks`
--
ALTER TABLE `bookmarks`
  ADD PRIMARY KEY (`bid`),
  ADD UNIQUE KEY `arbitrary_index_name` (`pid`,`uid`);

--
-- Indexes for table `follow`
--
ALTER TABLE `follow`
  ADD PRIMARY KEY (`fid`);

--
-- Indexes for table `likes`
--
ALTER TABLE `likes`
  ADD PRIMARY KEY (`lid`),
  ADD KEY `likes_ibfk_1` (`pid`);

--
-- Indexes for table `post`
--
ALTER TABLE `post`
  ADD PRIMARY KEY (`pid`),
  ADD KEY `post_ibfk_1` (`uid`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`uid`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `blocked_users`
--
ALTER TABLE `blocked_users`
  ADD CONSTRAINT `blocked_users_ibfk_1` FOREIGN KEY (`blocked_uid`) REFERENCES `user` (`uid`);

--
-- Constraints for table `likes`
--
ALTER TABLE `likes`
  ADD CONSTRAINT `likes_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `post` (`pid`);

--
-- Constraints for table `post`
--
ALTER TABLE `post`
  ADD CONSTRAINT `post_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
