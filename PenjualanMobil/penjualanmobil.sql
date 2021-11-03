-- phpMyAdmin SQL Dump
-- version 4.1.12
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Nov 20, 2016 at 05:49 PM
-- Server version: 5.6.16
-- PHP Version: 5.5.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `penjualanmobil`
--

-- --------------------------------------------------------

--
-- Table structure for table `faktur`
--

CREATE TABLE IF NOT EXISTS `faktur` (
  `no_faktur` int(10) NOT NULL,
  `kode_pembeli` int(10) NOT NULL,
  `kode_mobil` int(10) NOT NULL,
  `tgl_fktur` varchar(10) NOT NULL,
  `jam_fktur` varchar(10) NOT NULL,
  PRIMARY KEY (`no_faktur`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `faktur`
--

INSERT INTO `faktur` (`no_faktur`, `kode_pembeli`, `kode_mobil`, `tgl_fktur`, `jam_fktur`) VALUES
(1, 1001, 1, '15/11/2016', '10:20:21'),
(1996, 1001, 1, '14/11/2016', '00:25:44');

-- --------------------------------------------------------

--
-- Table structure for table `mobil`
--

CREATE TABLE IF NOT EXISTS `mobil` (
  `kode_mobil` int(10) NOT NULL,
  `nama_mobil` varchar(20) NOT NULL,
  `merk` varchar(20) NOT NULL,
  `warna` varchar(20) NOT NULL,
  `tahun` int(10) NOT NULL,
  `harga` int(20) NOT NULL,
  PRIMARY KEY (`kode_mobil`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `mobil`
--

INSERT INTO `mobil` (`kode_mobil`, `nama_mobil`, `merk`, `warna`, `tahun`, `harga`) VALUES
(1, 'AVANSA', 'AE1903SH', 'HITAM', 2016, 250000000),
(2, 'ALPAT', 'D110PS', 'PINK', 2011, 900000000),
(3, 'FERARI', 'ST4324PK', 'SILVER', 2019, 150000000),
(4, 'minti', 'ug1323', 'biru', 1998, 89000189);

-- --------------------------------------------------------

--
-- Table structure for table `pembeli`
--

CREATE TABLE IF NOT EXISTS `pembeli` (
  `kode_pembeli` int(10) NOT NULL,
  `nama` varchar(20) NOT NULL,
  `pekerjaan` varchar(20) NOT NULL,
  `alamat` varchar(30) NOT NULL,
  `umur` int(10) NOT NULL,
  PRIMARY KEY (`kode_pembeli`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pembeli`
--

INSERT INTO `pembeli` (`kode_pembeli`, `nama`, `pekerjaan`, `alamat`, `umur`) VALUES
(1, 'gembel', 'dokter', 'jl mastrip', 34),
(2, 'sumidi', 'kuli', 'kolong jembatan', 99),
(1001, 'fery', 'pengusaha', 'JL kita masih panjang', 20);

-- --------------------------------------------------------

--
-- Table structure for table `transaksi`
--

CREATE TABLE IF NOT EXISTS `transaksi` (
  `no_transaksi` int(10) NOT NULL,
  `no_faktur` int(10) NOT NULL,
  `tgl_fktur` varchar(10) NOT NULL,
  `jam_fktur` varchar(10) NOT NULL,
  `kode_pembeli` int(10) NOT NULL,
  `nama` varchar(20) NOT NULL,
  `kode_mobil` int(10) NOT NULL,
  `nama_mobil` varchar(20) NOT NULL,
  `merk` varchar(20) NOT NULL,
  `warna` varchar(20) NOT NULL,
  `tahun` int(10) NOT NULL,
  `harga` int(20) NOT NULL,
  PRIMARY KEY (`no_transaksi`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `transaksi`
--

INSERT INTO `transaksi` (`no_transaksi`, `no_faktur`, `tgl_fktur`, `jam_fktur`, `kode_pembeli`, `nama`, `kode_mobil`, `nama_mobil`, `merk`, `warna`, `tahun`, `harga`) VALUES
(1, 1, '15/11/2016', '10:20:21', 1001, 'sumanto', 2, 'ALPAT', 'D110PS', 'PINK', 2011, 900000000),
(2, 1, '15/11/2016', '10:20:21', 2, 'sumidi', 3, 'FERARI', 'ST4324PK', 'SILVER', 2019, 150000000);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
