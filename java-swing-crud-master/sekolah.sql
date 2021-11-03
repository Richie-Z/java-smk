--
-- Database: `sekolahx`
--
CREATE DATABASE IF NOT EXISTS `sekolah` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `sekolah`;

--
-- Table structure for table `siswa`
--

CREATE TABLE `siswa` (
  `no_induk` varchar(4) NOT NULL,
  `nama` varchar(30) NOT NULL,
  `nama_wali` varchar(30) NOT NULL,
  `tanggal_lahir` date NOT NULL,
  `telepon` varchar(20) NOT NULL,
  `alamat` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `siswa`
--

INSERT INTO `siswa` (`no_induk`, `nama`, `nama_wali`, `tanggal_lahir`, `telepon`, `alamat`) VALUES
('S001', 'Ahmad Yusuf', 'Yusuf', '2002-03-11', '081202834957', 'Jl. Perintis Kemerdekaan No.20'),
('S002', 'Intan Ali', 'Ali', '2002-01-23', '081143205836', 'Jl. Urip Sumoharjo No.12'),
('S003', 'Rahmad Syahrul', 'Syahrul', '2003-04-14', '081340673920', 'Jl. Tentara Pelajar No.3'),
('S004', 'Ridwan Rahman', 'Rahman', '2002-09-18', '081186746305', 'Jl. AP. Pettarani No.2'),
('S005', 'Putri Nur Hakim', 'Hakim', '2001-09-29', '081195826486', 'Jl. Sultan Alauddin No.5');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `siswa`
--
ALTER TABLE `siswa`
  ADD PRIMARY KEY (`no_induk`);
