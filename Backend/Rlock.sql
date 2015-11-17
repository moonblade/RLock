-- phpMyAdmin SQL Dump
-- version 4.4.3
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 17, 2015 at 10:12 AM
-- Server version: 5.6.24
-- PHP Version: 5.6.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `Rlock`
--

-- --------------------------------------------------------

--
-- Table structure for table `passkeys`
--

CREATE TABLE IF NOT EXISTS `passkeys` (
  `kid` int(11) NOT NULL,
  `passkey` int(4) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `id` varchar(30) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `passkeys`
--

INSERT INTO `passkeys` (`kid`, `passkey`, `date`, `id`) VALUES
(16, 6523, '2015-11-16 12:31:52', '101130645015448847110'),
(17, 6523, '2015-11-16 12:32:06', '1'),
(18, 5809, '2015-11-16 14:52:49', '101130645015448847110'),
(19, 5555, '2015-11-16 14:53:07', '101130645015448847110'),
(20, 5280, '2015-11-17 04:41:50', '108564481030705994196');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` varchar(30) NOT NULL,
  `name` varchar(30) NOT NULL,
  `level` int(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `level`) VALUES
('1', 'a', 0),
('101130645015448847110', 'nisham mohammed', 3),
('108564481030705994196', 'Kevin Joseph', 2),
('3', 'b', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `passkeys`
--
ALTER TABLE `passkeys`
  ADD PRIMARY KEY (`kid`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `passkeys`
--
ALTER TABLE `passkeys`
  MODIFY `kid` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=21;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
