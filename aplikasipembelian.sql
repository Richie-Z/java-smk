-- phpMyAdmin SQL Dump
-- version 5.0.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 26, 2020 at 11:36 PM
-- Server version: 10.4.14-MariaDB
-- PHP Version: 7.4.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `aplikasipembelian`
--

-- --------------------------------------------------------

--
-- Table structure for table `tabelbarang`
--

CREATE TABLE `tabelbarang` (
  `kodebarang` varchar(5) NOT NULL,
  `namabarang` varchar(50) DEFAULT NULL,
  `hargabeli` int(11) DEFAULT NULL,
  `hargajual` int(11) DEFAULT NULL,
  `stok` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tabelbarang`
--

INSERT INTO `tabelbarang` (`kodebarang`, `namabarang`, `hargabeli`, `hargajual`, `stok`) VALUES
('001', 'Pensil', 1000, 1500, 11),
('002', 'Bulpen', 2000, 2500, 10);

-- --------------------------------------------------------

--
-- Table structure for table `tabelpembelian`
--

CREATE TABLE `tabelpembelian` (
  `notransaksi` varchar(20) NOT NULL,
  `tgl` date NOT NULL,
  `kodesup` varchar(5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tabelpembelian`
--

INSERT INTO `tabelpembelian` (`notransaksi`, `tgl`, `kodesup`) VALUES
('002', '2020-10-15', '001'),
('1', '2020-10-15', '001'),
('2', '2020-10-22', '001');

-- --------------------------------------------------------

--
-- Table structure for table `tabelpembeliandetail`
--

CREATE TABLE `tabelpembeliandetail` (
  `notransaksi` varchar(20) DEFAULT NULL,
  `kodebarang` varchar(10) DEFAULT NULL,
  `jumlah` int(11) DEFAULT NULL,
  `totalharga` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tabelpembeliandetail`
--

INSERT INTO `tabelpembeliandetail` (`notransaksi`, `kodebarang`, `jumlah`, `totalharga`) VALUES
('1', '001', 1, 1500),
('2', '002', 10, 25000),
('002', '002', 2, 5000);

-- --------------------------------------------------------

--
-- Table structure for table `tabelsupplier`
--

CREATE TABLE `tabelsupplier` (
  `kodesup` varchar(5) NOT NULL,
  `supplier` varchar(50) DEFAULT NULL,
  `kontak` varchar(255) DEFAULT NULL,
  `telepon` varchar(50) DEFAULT NULL,
  `fax` varchar(50) DEFAULT NULL,
  `alamat` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tabelsupplier`
--

INSERT INTO `tabelsupplier` (`kodesup`, `supplier`, `kontak`, `telepon`, `fax`, `alamat`) VALUES
('001', 'Richie', 'Wa', '0180381', 'sadas', '2jaljbald');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tabelbarang`
--
ALTER TABLE `tabelbarang`
  ADD PRIMARY KEY (`kodebarang`);

--
-- Indexes for table `tabelpembelian`
--
ALTER TABLE `tabelpembelian`
  ADD PRIMARY KEY (`notransaksi`),
  ADD KEY `kodesup` (`kodesup`);

--
-- Indexes for table `tabelpembeliandetail`
--
ALTER TABLE `tabelpembeliandetail`
  ADD KEY `notransaksi` (`notransaksi`),
  ADD KEY `kodebarang` (`kodebarang`);

--
-- Indexes for table `tabelsupplier`
--
ALTER TABLE `tabelsupplier`
  ADD PRIMARY KEY (`kodesup`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tabelpembelian`
--
ALTER TABLE `tabelpembelian`
  ADD CONSTRAINT `tabelpembelian_ibfk_1` FOREIGN KEY (`kodesup`) REFERENCES `tabelsupplier` (`KodeSup`);

--
-- Constraints for table `tabelpembeliandetail`
--
ALTER TABLE `tabelpembeliandetail`
  ADD CONSTRAINT `tabelpembeliandetail_ibfk_1` FOREIGN KEY (`notransaksi`) REFERENCES `tabelpembelian` (`notransaksi`),
  ADD CONSTRAINT `tabelpembeliandetail_ibfk_2` FOREIGN KEY (`kodebarang`) REFERENCES `tabelbarang` (`KodeBarang`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
