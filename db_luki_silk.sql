-- phpMyAdmin SQL Dump
-- version 5.0.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 30, 2020 at 03:52 AM
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
-- Database: `db_luki_silk`
--

-- --------------------------------------------------------

--
-- Table structure for table `tb_barang`
--

CREATE TABLE `tb_barang` (
  `tanggal` varchar(100) NOT NULL,
  `kode_part` varchar(100) NOT NULL,
  `nama_part` varchar(250) NOT NULL,
  `kategori` varchar(100) NOT NULL,
  `jumlah` int(11) NOT NULL,
  `keterangan` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_barang`
--

INSERT INTO `tb_barang` (`tanggal`, `kode_part`, `nama_part`, `kategori`, `jumlah`, `keterangan`) VALUES
('01-05-2019', 'MTLAK01', 'Aki Kering', 'coba lagi', 45, ''),
('19-05-2019', 'MTLAK02', 'Aki Basah', 'coba2 ubah', 90, ''),
('22-05-2019', 'MTLAK03', 'Aki Cebong', 'coba2 ubah', 100, 'cebong anyut ke sungai');

-- --------------------------------------------------------

--
-- Table structure for table `tb_brg_keluar`
--

CREATE TABLE `tb_brg_keluar` (
  `tanggal` varchar(100) NOT NULL,
  `id_bk` varchar(100) NOT NULL,
  `gudang` varchar(250) NOT NULL,
  `keterangan` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_brg_keluar`
--

INSERT INTO `tb_brg_keluar` (`tanggal`, `id_bk`, `gudang`, `keterangan`) VALUES
('19-05-2019', 'BK-000001', 'Jakarta Pusat', ''),
('22-05-2019', 'BK-000002', 'Jakarta Timur', '');

-- --------------------------------------------------------

--
-- Table structure for table `tb_brg_masuk`
--

CREATE TABLE `tb_brg_masuk` (
  `tanggal` varchar(100) NOT NULL,
  `id_bm` varchar(100) NOT NULL,
  `supplier` varchar(250) NOT NULL,
  `keterangan` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_brg_masuk`
--

INSERT INTO `tb_brg_masuk` (`tanggal`, `id_bm`, `supplier`, `keterangan`) VALUES
('19-05-2019', 'BM-000001', 'Indo Narasi', ''),
('22-05-2019', 'BM-000002', 'Koin Jarang Kajun', 'Test'),
('22-05-2019', 'BM-000004', 'Indo Narasi', ''),
('22-05-2019', 'BM-000005', 'Indo Narasi', ''),
('22-05-2019', 'BM-000006', 'Koin Jarang Kajun', '');

-- --------------------------------------------------------

--
-- Table structure for table `tb_detail_brg_keluar`
--

CREATE TABLE `tb_detail_brg_keluar` (
  `tanggal` varchar(100) NOT NULL,
  `id_detail_bk` varchar(100) NOT NULL,
  `id_bk` varchar(100) NOT NULL,
  `gudang` varchar(250) NOT NULL,
  `kode_part` varchar(100) NOT NULL,
  `nama_part` varchar(250) NOT NULL,
  `jumlah` int(11) NOT NULL,
  `keterangan` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_detail_brg_keluar`
--

INSERT INTO `tb_detail_brg_keluar` (`tanggal`, `id_detail_bk`, `id_bk`, `gudang`, `kode_part`, `nama_part`, `jumlah`, `keterangan`) VALUES
('19-05-2019', 'DTBK-0001', 'BK-000001', 'Jakarta Pusat', 'MTLAK01', 'Aki Kering', 10, ''),
('22-05-2019', 'DTBK-0002', 'BK-000002', 'Jakarta Timur', 'MTLAK01', 'Aki Kering', 10, ''),
('22-05-2019', 'DTBK-0003', 'BK-000002', 'Jakarta Timur', 'MTLAK02', 'Aki Basah', 10, '');

--
-- Triggers `tb_detail_brg_keluar`
--
DELIMITER $$
CREATE TRIGGER `batal_keluar` AFTER DELETE ON `tb_detail_brg_keluar` FOR EACH ROW BEGIN
	UPDATE tb_barang SET jumlah = jumlah+OLD.jumlah
    WHERE kode_part = OLD.kode_part;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `keluar` AFTER INSERT ON `tb_detail_brg_keluar` FOR EACH ROW BEGIN
	UPDATE tb_barang SET jumlah = jumlah-NEW.jumlah
    WHERE kode_part = NEW.kode_part;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_detail_brg_masuk`
--

CREATE TABLE `tb_detail_brg_masuk` (
  `tanggal` varchar(100) NOT NULL,
  `id_detail_bm` varchar(100) NOT NULL,
  `id_bm` varchar(100) NOT NULL,
  `supplier` varchar(250) NOT NULL,
  `kode_part` varchar(100) NOT NULL,
  `nama_part` varchar(250) NOT NULL,
  `jumlah` int(11) NOT NULL,
  `keterangan` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_detail_brg_masuk`
--

INSERT INTO `tb_detail_brg_masuk` (`tanggal`, `id_detail_bm`, `id_bm`, `supplier`, `kode_part`, `nama_part`, `jumlah`, `keterangan`) VALUES
('19-05-2019', 'DTBM-0001', 'BM-000001', 'Indo Narasi', 'MTLAK01', 'Aki Kering', 10, ''),
('22-05-2019', 'DTBM-0002', 'BM-000002', 'Koin Jarang Kajun', 'MTLAK03', 'Aki Cebong', 50, 'Test'),
('22-05-2019', 'DTBM-0005', 'BM-000004', 'Indo Narasi', 'MTLAK01', 'Aki Kering', 50, ''),
('22-05-2019', 'DTBM-0006', 'BM-000005', 'Indo Narasi', 'MTLAK02', 'Aki Basah', 100, ''),
('22-05-2019', 'DTBM-0007', 'BM-000006', 'Koin Jarang Kajun', 'MTLAK03', 'Aki Cebong', 100, '');

--
-- Triggers `tb_detail_brg_masuk`
--
DELIMITER $$
CREATE TRIGGER `batal_tambah` AFTER DELETE ON `tb_detail_brg_masuk` FOR EACH ROW BEGIN
	UPDATE tb_barang SET jumlah = jumlah-OLD.jumlah
    WHERE kode_part = OLD.kode_part;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `tambah` AFTER INSERT ON `tb_detail_brg_masuk` FOR EACH ROW BEGIN
	UPDATE tb_barang SET jumlah = jumlah+NEW.jumlah
    WHERE kode_part = NEW.kode_part;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_gudang`
--

CREATE TABLE `tb_gudang` (
  `tanggal` varchar(100) NOT NULL,
  `kode_gudang` varchar(100) NOT NULL,
  `nama_gudang` varchar(250) NOT NULL,
  `alamat` varchar(500) NOT NULL,
  `keterangan` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_gudang`
--

INSERT INTO `tb_gudang` (`tanggal`, `kode_gudang`, `nama_gudang`, `alamat`, `keterangan`) VALUES
('22-05-2019', 'JKT01', 'Jakarta Barat', 'Jalan Meruya Selatan', 'Coba lagi'),
('01-05-2019', 'JKT02', 'Jakarta Pusat', 'Jala Wahid Hasyim', 'Keterangan aja kali ya\n'),
('22-05-2019', 'JKT03', 'Jakarta Timur', 'Condet Raya', 'Coba lagi ah');

-- --------------------------------------------------------

--
-- Table structure for table `tb_kategori`
--

CREATE TABLE `tb_kategori` (
  `tanggal` varchar(100) NOT NULL,
  `kode_kategori` varchar(100) NOT NULL,
  `nama_kategori` varchar(250) NOT NULL,
  `keterangan` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_kategori`
--

INSERT INTO `tb_kategori` (`tanggal`, `kode_kategori`, `nama_kategori`, `keterangan`) VALUES
('17-05-2019', '01', 'coba lagi', 'coba lagi iiii'),
('17-05-2019', '02', 'coba ubah', 'coba2 ubahhh lagi'),
('23-11-2020', '03', 'coba2asds', 'asdas');

-- --------------------------------------------------------

--
-- Table structure for table `tb_supplier`
--

CREATE TABLE `tb_supplier` (
  `tanggal` varchar(100) NOT NULL,
  `kode_supplier` varchar(100) NOT NULL,
  `nama_supplier` varchar(250) NOT NULL,
  `notelpon_supplier` varchar(20) NOT NULL,
  `alamat_supplier` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_supplier`
--

INSERT INTO `tb_supplier` (`tanggal`, `kode_supplier`, `nama_supplier`, `notelpon_supplier`, `alamat_supplier`) VALUES
('01-05-2019', 'PT IDN', 'Indo Narasi', '01 5555', 'Jalan Jalan fffffddd'),
('22-05-2019', 'PT KJK', 'Koin Jarang Kajun', '00112', 'kalla');

-- --------------------------------------------------------

--
-- Table structure for table `tb_user`
--

CREATE TABLE `tb_user` (
  `id` int(11) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(250) NOT NULL,
  `level` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_user`
--

INSERT INTO `tb_user` (`id`, `username`, `password`, `level`) VALUES
(1, 'admin', 'admin', 'admin');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tb_barang`
--
ALTER TABLE `tb_barang`
  ADD PRIMARY KEY (`kode_part`);

--
-- Indexes for table `tb_brg_keluar`
--
ALTER TABLE `tb_brg_keluar`
  ADD PRIMARY KEY (`id_bk`);

--
-- Indexes for table `tb_brg_masuk`
--
ALTER TABLE `tb_brg_masuk`
  ADD PRIMARY KEY (`id_bm`);

--
-- Indexes for table `tb_detail_brg_keluar`
--
ALTER TABLE `tb_detail_brg_keluar`
  ADD PRIMARY KEY (`id_detail_bk`);

--
-- Indexes for table `tb_detail_brg_masuk`
--
ALTER TABLE `tb_detail_brg_masuk`
  ADD PRIMARY KEY (`id_detail_bm`);

--
-- Indexes for table `tb_gudang`
--
ALTER TABLE `tb_gudang`
  ADD PRIMARY KEY (`kode_gudang`);

--
-- Indexes for table `tb_kategori`
--
ALTER TABLE `tb_kategori`
  ADD PRIMARY KEY (`kode_kategori`);

--
-- Indexes for table `tb_supplier`
--
ALTER TABLE `tb_supplier`
  ADD PRIMARY KEY (`kode_supplier`);

--
-- Indexes for table `tb_user`
--
ALTER TABLE `tb_user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tb_user`
--
ALTER TABLE `tb_user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
