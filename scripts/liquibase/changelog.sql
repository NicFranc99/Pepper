-- liquibase formatted sql

-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Creato il: Giu 14, 2024 alle 21:05
-- Versione del server: 8.0.26
-- Versione PHP: 8.0.22

-- changeset n.francavilla:start_up


--
-- Database: `my_bettercallpepper`
--
CREATE DATABASE IF NOT EXISTS `my_bettercallpepper`;
-- --------------------------------------------------------

--
-- Struttura della tabella `Calls`
--

CREATE TABLE `my_bettercallpepper`.`Calls` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parID` int(11) NOT NULL,
  `eldID` int(11) NOT NULL,
  `type` char(1) NOT NULL,
  `isActive` char(1) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ended_at` timestamp NULL DEFAULT NULL,
  `isApproved` char(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=354 DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `Calls`
--

INSERT INTO `my_bettercallpepper`.`Calls` (`id`, `parID`, `eldID`, `type`, `isActive`, `created_at`, `ended_at`, `isApproved`) VALUES
(160, 1, 1, '0', '0', '2021-05-13 13:07:10', '2021-05-13 13:07:57', '1'),
(159, 1, 1, '0', '0', '2021-05-13 13:01:02', '2021-05-13 13:02:21', '1'),
(158, 1, 1, '0', '0', '2021-05-13 13:00:11', '2021-05-13 13:00:39', '1'),
(157, 1, 1, '0', '0', '2021-05-13 10:41:24', '2021-05-13 10:42:13', '1'),
(156, 1, 1, '0', '0', '2021-05-13 10:29:45', '2021-05-13 10:30:47', '1'),
(155, 1, 1, '0', '0', '2021-05-13 10:07:58', '2021-05-13 10:08:31', '1'),
(154, 1, 1, '0', '0', '2021-05-13 10:06:02', '2021-05-13 10:06:39', '1'),
(153, 1, 1, '0', '0', '2021-05-13 10:04:42', '2021-05-13 10:05:43', '1'),
(152, 1, 1, '0', '0', '2021-05-13 10:02:28', '2021-05-13 10:03:01', '1'),
(151, 1, 1, '0', '0', '2021-05-13 10:00:38', '2021-05-13 10:01:20', '1'),
(150, 1, 1, '0', '0', '2021-05-13 09:49:38', '2021-05-13 09:51:17', '1'),
(147, 1, 1, '0', '0', '2021-05-13 08:40:02', '2021-05-13 08:41:38', '1'),
(146, 1, 1, '0', '0', '2021-05-13 08:34:52', '2021-05-13 08:35:50', '1'),
(145, 1, 1, '0', '0', '2021-05-13 08:31:49', '2021-05-13 08:33:52', '-'),
(144, 1, 1, '0', '0', '2021-05-13 08:21:03', '2021-05-13 08:21:55', '1'),
(143, 1, 1, '0', '0', '2021-05-13 08:19:44', '2021-05-13 08:20:59', '1'),
(142, 1, 1, '0', '0', '2021-05-13 08:09:08', '2021-05-13 08:10:26', '1'),
(141, 1, 1, '0', '0', '2021-05-13 07:53:27', '2021-05-13 07:53:42', '1'),
(140, 1, 1, '0', '0', '2021-05-13 07:51:20', '2021-05-13 07:53:23', '1'),
(139, 1, 1, '0', '0', '2021-05-13 07:46:37', '2021-05-13 07:47:52', '1'),
(138, 1, 1, '0', '0', '2021-05-13 07:45:27', '2021-05-13 07:45:51', '1'),
(137, 1, 1, '0', '0', '2021-05-13 07:43:13', '2021-05-13 07:45:18', '1'),
(149, 1, 1, '0', '0', '2021-05-13 09:42:14', '2021-05-13 09:43:43', '1'),
(132, 1, 1, '0', '0', '2021-05-07 09:27:22', '2021-05-07 09:27:45', '-'),
(148, 1, 1, '0', '0', '2021-05-13 08:42:53', '2021-05-13 08:46:56', '1'),
(135, 1, 1, '0', '0', '2021-05-11 08:53:16', '2021-05-11 08:53:43', '1'),
(134, 1, 1, '1', '0', '2021-05-11 08:52:40', '2021-05-11 08:52:57', '1'),
(133, 1, 1, '1', '0', '2021-05-11 08:50:32', '2021-05-11 08:51:34', '1'),
(161, 1, 1, '0', '0', '2021-05-13 13:08:20', '2021-05-13 13:08:39', '1'),
(162, 1, 1, '0', '0', '2021-05-13 13:08:48', '2021-05-13 13:10:18', '1'),
(163, 1, 1, '0', '0', '2021-05-13 13:23:45', '2021-05-13 13:27:34', '1'),
(164, 1, 1, '0', '0', '2021-05-13 13:41:59', '2021-05-13 13:42:28', '1'),
(165, 1, 1, '0', '0', '2021-05-13 13:53:03', '2021-05-13 13:53:55', '1'),
(166, 1, 1, '0', '0', '2021-05-13 13:54:06', '2021-05-13 13:54:35', '1'),
(167, 1, 1, '0', '0', '2021-05-18 17:08:04', '2021-05-18 17:09:08', '1'),
(168, 1, 1, '0', '0', '2021-05-21 12:52:02', '2021-05-21 12:52:49', '1'),
(169, 1, 1, '0', '0', '2021-05-22 07:49:00', '2021-05-22 07:50:37', '1'),
(170, 1, 1, '0', '0', '2021-05-22 07:51:36', '2021-05-22 07:56:10', '1'),
(171, 1, 1, '0', '0', '2021-05-22 07:56:44', '2021-05-22 07:57:47', '1'),
(172, 1, 1, '0', '0', '2021-05-22 08:10:38', '2021-05-22 08:11:21', '1'),
(173, 1, 1, '0', '0', '2021-05-22 08:17:36', '2021-05-22 08:18:33', '1'),
(174, 13, 3, '1', '1', '2021-06-01 10:41:14', NULL, '-'),
(175, 13, 3, '1', '1', '2021-06-01 10:49:05', NULL, '1'),
(176, 1, 1, '1', '0', '2021-06-01 11:07:19', NULL, '1'),
(177, 1, 1, '1', '0', '2021-06-01 11:15:14', NULL, '1'),
(178, 1, 1, '1', '0', '2021-06-01 12:36:00', NULL, '1'),
(179, 1, 1, '1', '0', '2021-06-01 12:42:48', NULL, '1'),
(180, 1, 1, '1', '0', '2021-06-01 12:47:28', NULL, '1'),
(181, 1, 1, '1', '0', '2021-06-01 12:51:50', NULL, '1'),
(182, 1, 1, '1', '0', '2021-06-01 13:07:56', NULL, '1'),
(183, 1, 1, '1', '0', '2021-06-01 13:15:53', NULL, '1'),
(184, 1, 1, '1', '0', '2021-06-01 13:31:26', NULL, '1'),
(185, 1, 1, '1', '0', '2021-06-01 13:34:39', NULL, '1'),
(186, 1, 1, '1', '0', '2021-06-01 13:35:27', '2021-06-03 10:49:53', '1'),
(187, 1, 1, '0', '0', '2021-06-03 10:49:55', '2021-06-03 10:51:21', '1'),
(188, 1, 1, '0', '0', '2021-06-03 11:00:45', '2021-06-03 11:04:07', '1'),
(189, 1, 1, '0', '0', '2021-06-07 10:18:20', '2021-06-07 10:19:12', '1'),
(190, 1, 1, '0', '0', '2021-06-07 10:28:43', '2021-06-07 10:30:49', '1'),
(191, 1, 1, '0', '0', '2021-06-07 11:06:28', '2021-06-07 11:09:03', '1'),
(192, 1, 1, '0', '0', '2021-06-07 13:53:39', '2021-06-07 13:55:49', '1'),
(193, 1, 1, '0', '0', '2021-06-07 13:57:48', '2021-06-07 13:58:18', '1'),
(194, 1, 1, '0', '0', '2021-06-07 14:09:26', '2021-06-07 14:09:38', '1'),
(195, 1, 1, '1', '0', '2021-06-08 08:52:27', NULL, '-'),
(196, 1, 1, '0', '0', '2021-06-08 12:03:52', '2021-06-08 12:06:46', '1'),
(197, 1, 1, '0', '0', '2021-06-08 12:07:06', '2021-06-08 12:08:01', '1'),
(198, 1, 1, '0', '0', '2021-06-08 12:10:37', NULL, '-'),
(199, 1, 1, '0', '0', '2021-06-08 12:11:14', '2021-06-08 12:12:56', '1'),
(200, 1, 1, '0', '0', '2021-06-08 12:14:17', '2021-06-08 12:15:21', '1'),
(201, 1, 1, '0', '0', '2021-06-08 12:15:34', '2021-06-08 12:17:13', '1'),
(202, 1, 1, '0', '0', '2021-06-08 12:17:32', '2021-06-08 12:19:19', '1'),
(203, 1, 1, '0', '0', '2021-06-08 12:19:29', '2021-06-08 12:20:43', '1'),
(204, 1, 1, '0', '0', '2021-06-08 12:24:05', '2021-06-08 12:25:34', '1'),
(205, 1, 1, '0', '0', '2021-06-08 12:25:48', '2021-06-08 12:36:09', '1'),
(206, 1, 1, '0', '0', '2021-06-08 12:36:31', '2021-06-08 12:37:57', '1'),
(207, 1, 1, '0', '0', '2021-06-08 12:43:37', '2021-06-08 12:44:46', '1'),
(208, 1, 1, '0', '0', '2021-06-08 12:45:08', '2021-06-08 12:46:33', '1'),
(209, 1, 1, '0', '0', '2021-06-08 12:46:58', '2021-06-08 12:48:20', '1'),
(210, 1, 1, '0', '0', '2021-06-08 12:48:34', '2021-06-08 12:48:50', '1'),
(211, 1, 1, '1', '0', '2021-06-08 14:29:14', '2021-06-08 14:30:08', '1'),
(212, 1, 1, '1', '0', '2021-06-08 14:33:35', NULL, '1'),
(213, 1, 1, '0', '0', '2021-06-09 12:20:37', '2021-06-09 12:22:01', '1'),
(214, 1, 1, '0', '0', '2021-06-09 12:22:24', '2021-06-09 12:24:24', '1'),
(215, 1, 1, '0', '0', '2021-06-09 12:24:28', '2021-06-09 12:25:11', '1'),
(216, 1, 1, '0', '0', '2021-06-09 12:32:34', '2021-06-09 12:33:37', '1'),
(217, 1, 1, '0', '0', '2021-06-09 12:33:55', '2021-06-09 12:34:45', '1'),
(218, 1, 1, '0', '0', '2021-06-09 12:35:06', '2021-06-09 12:35:12', '1'),
(219, 1, 1, '0', '0', '2021-06-09 12:35:37', '2021-06-09 12:36:33', '-'),
(220, 1, 1, '0', '0', '2021-06-09 12:36:41', '2021-06-09 12:37:33', '1'),
(221, 1, 1, '0', '0', '2021-06-09 12:37:37', '2021-06-09 12:38:47', '1'),
(222, 1, 1, '0', '0', '2021-06-09 12:39:13', '2021-06-09 12:40:01', '1'),
(223, 1, 1, '0', '0', '2021-06-09 12:40:05', '2021-06-09 12:41:48', '1'),
(224, 1, 1, '0', '0', '2021-06-09 12:41:52', '2021-06-09 12:43:06', '1'),
(225, 1, 1, '0', '0', '2021-06-09 12:44:05', '2021-06-09 12:44:35', '1'),
(226, 1, 1, '0', '0', '2021-06-09 12:44:44', '2021-06-09 12:46:22', '1'),
(227, 1, 1, '0', '0', '2021-06-09 12:46:31', '2021-06-09 12:46:58', '1'),
(228, 1, 1, '0', '0', '2021-06-09 12:47:00', '2021-06-09 12:47:37', '1'),
(229, 1, 1, '0', '0', '2021-06-09 12:47:42', '2021-06-09 12:53:53', '1'),
(230, 1, 1, '0', '0', '2021-06-09 12:54:07', '2021-06-09 12:54:33', '1'),
(231, 1, 1, '0', '0', '2021-06-09 12:54:38', '2021-06-09 12:54:53', '1'),
(232, 1, 1, '0', '0', '2021-06-09 12:54:58', '2021-06-09 12:55:13', '1'),
(233, 1, 1, '0', '0', '2021-06-09 12:56:15', '2021-06-09 12:56:35', '1'),
(234, 1, 1, '0', '0', '2021-06-09 12:56:38', '2021-06-09 12:57:01', '1'),
(235, 1, 1, '0', '0', '2021-06-09 12:57:04', '2021-06-09 12:57:23', '1'),
(236, 1, 1, '0', '0', '2021-06-11 13:58:27', '2021-06-11 13:59:17', '-'),
(237, 1, 1, '0', '0', '2021-06-12 08:30:36', '2021-06-12 08:31:43', '1'),
(238, 1, 1, '0', '0', '2021-06-12 13:45:29', '2021-06-12 13:46:32', '1'),
(239, 1, 1, '0', '0', '2021-06-12 14:05:36', '2021-06-12 14:06:44', '1'),
(240, 1, 1, '0', '0', '2021-06-12 14:07:31', '2021-06-12 14:11:18', '1'),
(241, 1, 1, '0', '0', '2021-06-12 14:23:17', '2021-06-12 14:24:47', '1'),
(242, 1, 1, '0', '0', '2021-06-12 14:25:34', '2021-06-12 14:28:51', '1'),
(243, 1, 1, '0', '0', '2021-06-12 14:29:23', '2021-06-12 14:29:59', '1'),
(244, 1, 1, '0', '0', '2021-06-12 14:30:02', '2021-06-12 14:30:39', '1'),
(245, 1, 1, '0', '0', '2021-06-12 14:30:51', '2021-06-12 14:32:38', '1'),
(246, 1, 1, '0', '0', '2021-06-12 14:39:50', '2021-06-12 14:41:41', '1'),
(247, 1, 1, '0', '0', '2021-06-12 14:41:42', '2021-06-12 14:44:54', '1'),
(248, 1, 1, '0', '0', '2021-06-12 14:44:56', '2021-06-12 14:49:12', '1'),
(249, 1, 1, '0', '0', '2021-06-12 14:49:39', '2021-06-12 14:52:44', '1'),
(250, 1, 1, '0', '0', '2021-06-12 14:52:51', '2021-06-12 14:53:09', '1'),
(251, 1, 1, '0', '0', '2021-06-12 14:53:12', '2021-06-12 14:56:19', '1'),
(252, 1, 1, '0', '0', '2021-06-12 14:56:20', '2021-06-12 14:57:07', '1'),
(253, 1, 1, '0', '0', '2021-06-12 14:57:37', '2021-06-12 14:58:39', '1'),
(254, 1, 1, '0', '0', '2021-06-12 14:58:40', '2021-06-12 14:59:29', '1'),
(255, 1, 1, '0', '0', '2021-06-12 14:59:35', '2021-06-12 15:00:46', '1'),
(256, 1, 1, '0', '0', '2021-06-12 15:05:13', '2021-06-12 15:05:32', '-'),
(257, 1, 1, '0', '0', '2021-06-12 15:05:37', '2021-06-12 15:05:49', '-'),
(258, 1, 1, '0', '0', '2021-06-12 15:05:52', '2021-06-12 15:06:43', '1'),
(259, 1, 1, '0', '0', '2021-06-12 15:07:01', NULL, '-'),
(260, 1, 1, '0', '0', '2021-06-12 15:07:22', '2021-06-12 15:09:41', '-'),
(261, 1, 1, '0', '0', '2021-06-12 15:16:33', '2021-06-12 15:16:59', '-'),
(262, 1, 1, '0', '0', '2021-06-12 15:25:44', '2021-06-12 15:27:57', '1'),
(263, 1, 1, '1', '0', '2021-06-12 15:29:30', '2021-06-12 15:30:11', '1'),
(264, 1, 1, '0', '0', '2021-06-12 15:30:30', '2021-06-12 15:31:04', '1'),
(265, 1, 1, '0', '0', '2021-06-12 15:32:20', '2021-06-12 15:33:31', '1'),
(266, 1, 1, '0', '0', '2021-06-12 15:35:10', '2021-06-12 15:35:44', '1'),
(267, 1, 1, '1', '0', '2021-06-12 17:16:24', NULL, '-'),
(268, 1, 1, '1', '0', '2021-06-12 17:25:32', NULL, '-'),
(269, 1, 1, '0', '0', '2021-06-15 14:16:33', NULL, '-'),
(270, 1, 1, '0', '0', '2021-06-15 14:16:40', NULL, '-'),
(271, 1, 1, '0', '0', '2021-06-15 14:16:43', NULL, '-'),
(272, 1, 1, '0', '0', '2021-06-15 14:16:51', NULL, '-'),
(273, 1, 1, '0', '0', '2021-06-15 14:16:54', '2021-06-15 14:16:56', '-'),
(274, 1, 1, '0', '0', '2021-06-15 14:18:05', '2021-06-15 14:18:52', '-'),
(275, 1, 1, '0', '0', '2021-06-15 14:19:03', '2021-06-15 14:20:23', '-'),
(276, 1, 1, '1', '0', '2021-06-15 14:21:16', '2021-06-15 14:22:52', '1'),
(277, 1, 1, '1', '0', '2021-06-17 09:24:47', NULL, '-'),
(278, 14, 2, '1', '1', '2021-06-17 09:26:31', NULL, '-'),
(279, 1, 1, '0', '0', '2021-06-18 09:31:03', '2021-06-18 09:32:11', '1'),
(280, 1, 1, '1', '0', '2021-06-18 09:45:01', '2021-06-18 09:45:34', '1'),
(281, 1, 1, '1', '0', '2021-06-18 09:47:26', '2021-06-18 09:47:49', '1'),
(282, 1, 1, '1', '0', '2021-06-18 10:59:05', NULL, '1'),
(283, 1, 1, '1', '0', '2021-06-18 11:10:24', NULL, '-'),
(284, 1, 1, '0', '0', '2021-06-18 11:15:30', '2021-06-18 11:16:02', '1'),
(285, 1, 1, '0', '0', '2021-06-18 11:16:05', '2021-06-18 11:22:43', '1'),
(286, 1, 1, '0', '0', '2021-06-18 12:17:29', '2021-06-18 12:19:48', '1'),
(287, 1, 1, '0', '0', '2021-06-18 13:14:35', '2021-06-18 13:15:18', '1'),
(288, 1, 1, '0', '0', '2021-06-18 13:15:23', '2021-06-18 13:17:33', '1'),
(289, 1, 1, '0', '0', '2021-06-18 13:32:26', '2021-06-18 13:34:40', '1'),
(290, 1, 1, '0', '0', '2021-06-18 13:44:00', '2021-06-18 13:44:37', '1'),
(291, 1, 1, '0', '0', '2021-06-18 13:44:59', '2021-06-18 13:48:25', '1'),
(292, 1, 1, '0', '0', '2021-06-18 13:51:36', '2021-06-18 13:52:46', '1'),
(293, 1, 1, '0', '0', '2021-06-18 13:52:51', '2021-06-18 13:55:56', '1'),
(294, 1, 1, '0', '0', '2021-06-18 13:59:43', '2021-06-18 14:00:11', '1'),
(295, 1, 1, '0', '0', '2021-06-18 14:10:26', '2021-06-18 14:11:09', '1'),
(296, 1, 1, '0', '0', '2021-06-18 14:16:09', '2021-06-18 14:18:09', '1'),
(297, 1, 1, '0', '0', '2021-06-18 14:22:36', '2021-06-18 14:23:22', '1'),
(298, 1, 1, '1', '0', '2021-06-18 14:31:42', '2021-06-18 14:31:55', '1'),
(299, 1, 1, '1', '0', '2021-06-22 10:32:36', NULL, '-'),
(300, 1, 1, '1', '0', '2021-09-07 10:15:33', NULL, '-'),
(301, 1, 1, '1', '0', '2021-10-08 14:58:25', NULL, '-'),
(302, 1, 1, '1', '0', '2022-05-23 09:22:08', NULL, '1'),
(303, 1, 1, '1', '0', '2022-05-23 09:22:53', NULL, '1'),
(304, 1, 1, '0', '0', '2022-05-23 09:34:36', NULL, '1'),
(305, 1, 1, '1', '0', '2022-05-23 09:35:21', '2022-05-23 09:35:51', '1'),
(306, 1, 1, '0', '0', '2022-05-23 09:35:54', NULL, '1'),
(307, 1, 1, '0', '0', '2022-05-23 09:37:45', NULL, '1'),
(308, 1, 1, '1', '0', '2022-05-23 09:37:57', '2022-05-23 09:39:00', '1'),
(309, 1, 1, '1', '0', '2022-05-23 09:39:32', NULL, '1'),
(310, 1, 1, '0', '0', '2022-05-23 09:40:07', '2022-05-23 09:41:17', '1'),
(311, 1, 1, '1', '0', '2022-05-23 09:41:40', '2022-05-23 09:42:00', '1'),
(312, 1, 1, '0', '0', '2022-05-23 09:43:26', '2022-05-23 09:44:13', '1'),
(313, 1, 1, '0', '0', '2022-05-23 09:44:16', '2022-05-23 09:44:30', '1'),
(314, 1, 1, '1', '0', '2022-05-23 09:44:16', NULL, '1'),
(315, 1, 1, '0', '0', '2022-05-23 09:44:36', '2022-05-23 09:45:08', '1'),
(316, 1, 1, '0', '0', '2022-05-23 09:45:14', '2022-05-23 09:45:30', '-'),
(317, 1, 1, '0', '0', '2022-05-23 09:45:33', '2022-05-23 09:45:47', '-'),
(318, 1, 1, '0', '0', '2022-05-23 09:45:50', '2022-05-23 09:46:40', '-'),
(319, 1, 1, '0', '0', '2022-05-23 09:46:43', '2022-05-23 09:47:02', '-'),
(320, 1, 1, '0', '0', '2022-05-23 09:47:06', '2022-05-23 09:47:35', '1'),
(321, 1, 1, '1', '0', '2022-05-23 09:47:46', '2022-05-23 09:49:11', '1'),
(322, 1, 1, '0', '0', '2022-05-23 09:49:41', NULL, '-'),
(323, 1, 1, '0', '0', '2022-05-23 09:50:04', '2022-05-23 09:52:54', '1'),
(324, 1, 1, '1', '0', '2022-05-23 09:56:22', NULL, '-'),
(325, 1, 1, '0', '0', '2022-06-12 10:57:14', '2022-06-12 10:58:47', '1'),
(326, 1, 1, '0', '0', '2022-06-12 10:59:11', '2022-06-12 10:59:22', '1'),
(327, 1, 1, '0', '0', '2022-06-12 10:59:24', '2022-06-12 10:59:47', '1'),
(328, 1, 1, '0', '0', '2022-06-12 10:59:49', NULL, '1'),
(329, 1, 1, '1', '0', '2022-06-12 11:00:12', '2022-06-12 11:00:37', '1'),
(330, 1, 1, '0', '0', '2022-06-12 11:00:51', '2022-06-12 11:01:30', '1'),
(331, 1, 1, '0', '0', '2022-06-12 11:01:33', '2022-06-12 11:02:16', '1'),
(332, 1, 1, '0', '0', '2022-06-12 11:02:23', '2022-06-12 11:04:40', '1'),
(333, 1, 1, '1', '1', '2022-06-14 10:14:52', NULL, '-'),
(334, 1, 1, '1', '1', '2022-06-14 10:20:29', NULL, '-'),
(335, 1, 1, '1', '1', '2022-06-14 10:29:21', NULL, '1'),
(336, 13, 3, '1', '1', '2022-06-26 00:03:15', NULL, '-'),
(337, 13, 3, '1', '1', '2022-06-26 00:06:56', NULL, '-'),
(338, 13, 0, '1', '1', '2022-06-26 00:09:02', NULL, '0'),
(339, 1, 1, '1', '1', '2022-06-26 21:01:53', NULL, '-'),
(340, 1, 1, '1', '1', '2022-06-26 21:02:20', NULL, '-'),
(341, 13, 3, '1', '1', '2022-06-26 21:04:45', NULL, '-'),
(342, 1, 1, '1', '1', '2022-06-26 21:06:59', NULL, '-'),
(343, 1, 1, '1', '1', '2022-06-26 21:43:14', NULL, '-'),
(344, 1, 1, '1', '1', '2022-06-26 21:52:17', NULL, '-'),
(345, 1, 1, '1', '1', '2022-07-02 13:30:34', NULL, '-'),
(346, 13, 3, '1', '1', '2022-07-02 13:31:23', NULL, '-'),
(347, 1, 1, '1', '1', '2022-07-09 17:14:18', NULL, '-'),
(348, 1, 1, '1', '1', '2022-07-09 17:53:50', NULL, '-'),
(349, 14, 2, '1', '1', '2022-07-09 17:54:33', NULL, '-'),
(350, 1, 1, '1', '1', '2022-07-17 18:33:44', NULL, '-'),
(351, 1, 1, '1', '1', '2022-09-17 11:01:59', NULL, '0'),
(352, 1, 1, '1', '1', '2022-10-09 15:49:17', NULL, '0'),
(353, 3, 1, '1', '1', '2022-11-07 11:15:20', NULL, '0');

-- --------------------------------------------------------

--
-- Struttura della tabella `Categories`
--

CREATE TABLE `my_bettercallpepper`.`Categories` (
  `id_category` int(11) NOT NULL,
  `name_category` varchar(35) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `description_explaitation_category` text NOT NULL,
  `has_vocal_input` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id_category`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `Categories`
--

INSERT INTO `Categories` (`id_category`, `name_category`, `description_explaitation_category`, `has_vocal_input`) VALUES
(1, 'appartenenza', 'In questo esercizio ti mostreremo di volta in volta una parola, dovrai indicare se la parola appartiene o meno alla categoria appartenenza.', 0),
(2, 'categorizzazione', 'In questo esercizio ti mostreremo  un nome di una persona o un oggetto, e 4 opzioni che indicano diverse categorie, dovrai indicare a quale categoria appartiene la parola.', 0),
(3, 'combinazionilettere', "Durante l'esercizio di combinazioni lettere  dovrai cercare di formare il maggior numero di parole con le lettere presentate.", 1),
(4, 'esistenzaparole', "Durante questo esercizio ti sará mostrata una parola, dovrai dirci se quella parola ESISTE oppure NON ESISTE.", 0),
(5, 'finaliparole', "L'esercizio di finali parole consiste nel presentarti l'inizio di una parola, e tu dovrai dirmi come termina ad esempio “diva” sarà l'inizio della parola “divano” oppure “divario”.", 1),
(6, 'fluenzefonologiche', 'In questo gioco ti sará mostrata una lettera, tu dovrai indicare quante piú parole che ti vengono in mente che iniziano con quella lettera.', 1),
(7, 'fluenzesemantiche', 'In questo gioco ti mostreremo una categoria e dovrai indicare quante piú parole possibili che ti vengono in mente inerente alla categoria mostrata.', 1),
(8, 'fluenzeverbali', 'In questo esercizio ti verra mostrata di volta in volta una categoria, dovrai indicare quante piú parole ti vengono in mente inerente a quella categoria.', 1),
(9, 'letteremancanti', 'In questo gioco ti verrano mostrate delle parole con delle lettere mancanti ed una categoria di riferimento, dovrai indicare quali sono le lettere mancanti.', 0),
(10, 'mesi', "In questo gioco ti verranno fatte delle domande sui diversi mesi dell'anno, il tuo compito sará quello di rispondere correttamente alle domande.", 0),
(11, 'musica', "Con il gioco musica ti faremo ascoltare 30 secondi di una canzone, il tuo compito sará riconoscere chi é l'autore di quella canzone tra le opzioni mostrate.", 0),
(12, 'racconti', 'In questo esercizio ti racconteremo una breve storia, e una volta terminata ti faremo delle domande inerenti al racconto e dovrai rispondere.', 0),
(13, 'volti', 'Ti mostreremo dei volti, il tuo compito sará riconoscere il personaggio e selezionare il nome corretto tra le varie risposte.', 0);

-- --------------------------------------------------------

--
-- Struttura della tabella `Contacts`
--

CREATE TABLE `my_bettercallpepper`.`Contacts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(25) NOT NULL,
  `surname` varchar(25) NOT NULL,
  `propic` varchar(100) NOT NULL,
  `gender` char(1) NOT NULL,
  `elderID` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `Contacts`
--

INSERT INTO `my_bettercallpepper`.`Contacts` (`id`, `name`, `surname`, `propic`, `gender`, `elderID`) VALUES
(1, 'Mario', 'Balotelli', 'mario_balotelli.jpeg', '1', 1),
(2, 'Nicola', 'Di Liddo', 'nicola_diliddo.png', '1', 2),
(3, 'Nicola', 'Di Liddo', 'nicola_diliddo.png', '1', 1),
(4, 'Andrea', 'De Gennaro', 'andrea_de_gennaro.png', '1', 1),
(16, 'Michele', 'Salvemini', 'michele_salvemini.jpg', '1', 5),
(15, 'Tonio', 'Decaro', 'tonio_decaro.jpg', '1', 4),
(14, 'Beppe', 'Conte', 'beppe_conte.jpg', '1', 2),
(13, 'Antonello', 'Venditti', 'antonello_venditti.jpg', '1', 3),
(17, 'Michele', 'Salvemini', 'michele_salvemini.jpg', '1', 7);

-- --------------------------------------------------------

--
-- Struttura della tabella `Elderlies`
--

CREATE TABLE `my_bettercallpepper`.`Elderlies` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(25) NOT NULL,
  `surname` varchar(25) NOT NULL,
  `birth` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `place` varchar(30) NOT NULL,
  `prov` varchar(5) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '1',
  `propic` varchar(100) NOT NULL,
  `gender` char(1) NOT NULL,
  `room` int(11) NOT NULL,
  `pulsante` char(1) NOT NULL DEFAULT '0',
  `pepper_impegnato` int(11) NOT NULL DEFAULT '0',
  `pepper_go` int(11) NOT NULL DEFAULT '0',
  `richiesta_operatore` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `Elderlies`
--

INSERT INTO `my_bettercallpepper`.`Elderlies` (`id`, `name`, `surname`, `birth`, `place`, `prov`, `status`, `propic`, `gender`, `room`, `pulsante`, `pepper_impegnato`, `pepper_go`, `richiesta_operatore`) VALUES
(1, 'Tina', 'De Gennaro', '1960-04-25 00:00:00', 'Molfetta', 'BA', 1, 'tina_degennaro.png', '2', 1, '0', 0, 0, NULL),
(2, 'Michele', 'Annese', '1961-01-11 00:00:00', 'Giovinazzo', 'BA', 1, 'michele_annese.png', '1', 2, '0', 0, 0, NULL),
(3, 'Maurizio', 'De Biase', '1972-05-22 00:00:00', 'Napoli', 'NA', 1, 'maurizio_de_biase.png', '1', 3, '0', 0, 0, NULL);

-- --------------------------------------------------------

--
-- Struttura della tabella `Games`
--

CREATE TABLE `my_bettercallpepper`.`Games` (
  `id_game` int NOT NULL,
  `id_category` int NOT NULL,
  `id_elderly` int NOT NULL,
  `name_game` varchar(32) NOT NULL,
  `risposte` json DEFAULT NULL COMMENT 'struttura {["pippo","pluto"]}. Json che rappresenta array di tutte le risposte possibili del gioco',
  `domanda` text,
  `esercizi` json NOT NULL COMMENT 'Json array cosi'' composto [{parola: null, rispostaCorretta: null, domanda:null}]. Ogni nodo di questo json array rappresenta un esercizio di questo gioco',
  `freeText` text,
  `mediaUrl` json DEFAULT NULL,
  PRIMARY KEY (`id_game`),
  UNIQUE KEY unique_id_game_to_name_game (`id_game`, `name_game`),
   FOREIGN KEY (`id_category`) REFERENCES Categories(`id_category`),
   FOREIGN KEY (`id_elderly`) REFERENCES Elderlies(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Dump dei dati per la tabella `Games`
--

INSERT INTO `Games` (`id_game`, `id_category`, `id_elderly`, `name_game`, `risposte`, `domanda`, `esercizi`, `freeText`, `mediaUrl`) VALUES
(1, 1, 1, 'Frutti Estivi', '[\"Si\", \"No\"]', NULL, '[{\"parola\": \"albicocche\", \"rispostaCorretta\": \"Si\"}, {\"parola\": \"alchechengi\", \"rispostaCorretta\": \"No\"}]', NULL, NULL),
(2, 2, 1, 'Categorizziamo', '[\"Cantanti\", \"Frutta\", \"Verdura\", \"Mestieri\"]', 'A quale categoria appartiene la parola: ', '[{\"parola\": \"andriano celentano\", \"rispostaCorretta\": \"cantanti\"}, {\"parola\": \"mandarino\", \"rispostaCorretta\": \"frutta\"}, {\"parola\": \"bietole\", \"rispostaCorretta\": \"verdura\"}, {\"parola\": \"carrozziere\", \"rispostaCorretta\": \"mestieri\"}, {\"parola\": \"cavolfiore\", \"rispostaCorretta\": \"verdura\"}]', NULL, NULL),
(3, 3, 1, 'Combiniamo le lettere', NULL, 'Forma quante piú parole possibili con queste lettere', '[{\"parola\": \"e\"}, {\"parola\": \"m\"}]', NULL, NULL),
(4, 4, 1, 'Esistenza Parole', '[\"Esiste\", \"Non Esiste\"]', 'Esiste o non esiste', '[{\"parola\": \"vaso\"}, {\"parola\": \"cane\"}, {\"parola\": \"casco\"}, {\"parola\": \"castaro\"}, {\"parola\": \"nettare\"}, {\"parola\": \"minto\"}, {\"parola\": \"lonfo\"}, {\"parola\": \"bambino\"}, {\"parola\": \"gluisce\"}, {\"parola\": \"trono\"}]', NULL, NULL),
(5, 5, 1, 'Finali parole', NULL, 'Completa la parola che inizia con:', '[{\"parola\": \"diva\"}, {\"parola\": \"acqua\"}, {\"parola\": \"ali\"}, {\"parola\": \"dolo\"}, {\"parola\": \"tav\"}]', NULL, NULL),
(6, 6, 1, 'Fluenze Fonologiche', NULL, 'Forma quante piú parole possibili che iniziano con la lettera: ', '[{\"parola\": \"E\"}]', NULL, NULL),
(7, 7, 1, 'Vestiario', NULL, 'Indica piú parole possibili relative alla categoria: ', '[{\"parola\": \"Vestiti\"}]', NULL, NULL),
(8, 8, 1, 'Fluenze Verbali', NULL, 'Indica la prima cosa che ti viene in mente quanto senti la parola: ', '[{\"parola\": \"genitori\"}, {\"parola\": \"scuola\"}, {\"parola\": \"sport\"}, {\"parola\": \"cibo\"}]', NULL, NULL),
(9, 9, 1, 'Lettere Mancanti', NULL, 'Inserisci le lettere mancanti della parola appartenente alla categoria NATALE', '[{\"parola\": \"VIGILIA\"}, {\"parola\": \"VISCHIO\"}, {\"parola\": \"ASINELLO\"}, {\"parola\": \"PENDOLO\"}, {\"parola\": \"CAVATAPPI\"}]', NULL, NULL),
(10, 10, 1, 'Mesi', NULL, NULL, '[{\"domanda\": \"Quando inizia la primavera?\", \"risposte\": [\"Aprile\", \"Giugno\"], \"rispostaCorretta\": \"Marzo\"}, {\"domanda\": \"Quando inizia la l\'inverno?\", \"risposte\": [\"Novembre\", \"Gennario\"], \"rispostaCorretta\": \"Dicembre\"}, {\"domanda\": \"Quando inizia l\'autunno?\", \"risposte\": [\"Ottobre\", \"Agosto\"], \"rispostaCorretta\": \"Settembre\"}]', NULL, NULL),
(11, 11, 1, 'Musica', NULL, 'Chi canta questa canzone?', '[{\"risposte\": [\"Fausto Leali\", \"Adriano Celentano\"], \"rispostaCorretta\": \"Massimo Ranieri\"}, {\"risposte\": [\"Mina\", \"Anna Oxa\"], \"rispostaCorretta\": \"Rita Pavone\"}, {\"risposte\": [\"Massimo Ranieri\", \"Claudio Baglioni\"], \"rispostaCorretta\": \"Edoardo Vianello\"}, {\"risposte\": [\"Franco Califano\", \"Albano Carrisi\"], \"rispostaCorretta\": \"Adriano Celentano\"}, {\"risposte\": [\"Max Pezzali\", \"Renato Zero\"], \"rispostaCorretta\": \"Mina\"}]', NULL, '[\"https://firebasestorage.googleapis.com/v0/b/amicoreminders-nsfphc.appspot.com/o/songMusicQuiz%2Frose%20rosse%20-%20massimo%20ranieri.mp3?alt=media&token=07b71179-c357-4192-b12f-2ccdbb4c7fc3\", \"https://firebasestorage.googleapis.com/v0/b/amicoreminders-nsfphc.appspot.com/o/songMusicQuiz%2FDatemi%20un%20martello.mp3?alt=media&token=dd9d7735-2dd9-4b14-af48-274c864977e9\", \"https://firebasestorage.googleapis.com/v0/b/amicoreminders-nsfphc.appspot.com/o/songMusicQuiz%2FAbbronzatissima.mp3?alt=media&token=6640bd72-7c27-4994-a059-df6036c9a069\", \"https://firebasestorage.googleapis.com/v0/b/amicoreminders-nsfphc.appspot.com/o/songMusicQuiz%2F24%20mila%20baci%20(Remastered).mp3?alt=media&token=f89a53f4-81a5-42ca-891f-409768c11d9e\", \"https://firebasestorage.googleapis.com/v0/b/amicoreminders-nsfphc.appspot.com/o/songMusicQuiz%2FMINA%20-%20Stessa%20spiaggia%20stesso%20mare.mp3?alt=media&token=f47d04cb-ebea-4a7e-b967-724e63934729\"]'),
(12, 13, 1, 'Volti', NULL, NULL, '[{\"domanda\": \"Quale e\' il suo nome?\", \"risposte\": [\"Carlo\", \"Francesco\"], \"rispostaCorretta\": \"Antonio\"}, {\"domanda\": \"Chi e\' per te questo signore? \", \"risposte\": [\"Zio\", \"Nonno\"], \"rispostaCorretta\": \"Fratello\"}, {\"domanda\": \"Quale e\' il suo lavoro?\", \"risposte\": [\"Muratore\", \"Elettricista\"], \"rispostaCorretta\": \"Meccanico\"}]', NULL, '[\"https://firebasestorage.googleapis.com/v0/b/amicoreminders-nsfphc.appspot.com/o/imagesQuiz%2FvoltiImage1.jpg?alt=media&token=7e2f23bb-7013-4217-bef3-303095179cec\"]'),
(13, 1, 2, 'Frutti Estivi', '[\"Si\", \"No\"]', NULL, '[{\"parola\": \"albicocche\", \"rispostaCorretta\": \"Si\"}, {\"parola\": \"alchechengi\", \"rispostaCorretta\": \"No\"}]', NULL, NULL),
(14, 2, 2, 'Categorizziamo', '[\"Cantanti\", \"Frutta\", \"Verdura\", \"Mestieri\"]', NULL, '[{\"parola\": \"andriano celentano\", \"domanda\": \"A quale categoria appartiene la parola: \", \"rispostaCorretta\": \"cantanti\"}, {\"parola\": \"mandarino\", \"domanda\": \"A quale categoria appartiene la parola: \", \"rispostaCorretta\": \"frutta\"}, {\"parola\": \"bietole\", \"domanda\": \"A quale categoria appartiene la parola: \", \"rispostaCorretta\": \"verdura\"}, {\"parola\": \"carrozziere\", \"domanda\": \"A quale categoria appartiene la parola: \", \"rispostaCorretta\": \"mestieri\"}, {\"parola\": \"cavolfiore\", \"domanda\": \"A quale categoria appartiene la parola: \", \"rispostaCorretta\": \"verdura\"}]', NULL, NULL),
(15, 12, 2, 'Elisabetta', NULL, NULL, '[{\"domanda\": \"Quanti erano i fratelli?\", \"risposte\": [\"Uno\", \"Due\", \"Cinque\"], \"rispostaCorretta\": \"Tre\"}, {\"domanda\": \"Cosa capitò a Lorenzo?\", \"risposte\": [\"Si sposò con Elisabetta\", \"Partì con Elisabetta\"], \"rispostaCorretta\": \"Fu ucciso dai fratelli\"}, {\"domanda\": \"Come si chiama la sorella minore?\", \"risposte\": [\"Maria\", \"Pina\", \"Addolorata\"], \"rispostaCorretta\": \"Elisabetta\"}]', 'Nella città di Messina vi abitavano tre fratelli, ricchi mercanti, con la sorella minore Elisabetta, fanciulla molto bella non ancora maritata. Questa si innamorò di un giovane di nome Lorenzo che lavorava presso il fondaco dei tre fratelli. Anche Lorenzo si innamorò di Elisabetta e i due incominciarono a frequentarsi segretamente. Il fratello maggiore accortosi della relazione né parlò agli altri due fratelli e tutti e tre, dopo aver portato Lorenzo in luogo solitario lo uccisero e lo seppellirono. Una notte comparve in sogno a Elisabetta Lorenzo che le dice di essere stato ucciso dai suoi fratelli e le rivelò dove era seppellito. La fanciulla vi si recò, scavò e tagliò la testa dal corpo che dopo averla fasciata mise in un vaso e ricoprì di terra e vi piantò delle piante. Spesso la fanciulla riversava lacrime sul vaso e i fratelli avvertiti dai vicini, le tolsero il vaso e scoperta la testa la sotterrarono. Dopo i tre fratelli partirono per Napoli affinché non si sapesse la storia e la sorella continuando a versare lacrime morì.  ', '[\"https://firebasestorage.googleapis.com/v0/b/amicoreminders-nsfphc.appspot.com/o/storyImagesQuiz%2FElisabetta%2FLisabetta0.jpg?alt=media&token=c80cc643-9a09-499b-996b-fcc2186c5091\", \"https://firebasestorage.googleapis.com/v0/b/amicoreminders-nsfphc.appspot.com/o/storyImagesQuiz%2FElisabetta%2FLisabetta1.jpg?alt=media&token=85c7b808-bf57-4e0f-bf13-236d35727732\", \"https://firebasestorage.googleapis.com/v0/b/amicoreminders-nsfphc.appspot.com/o/storyImagesQuiz%2FElisabetta%2FLisabetta2.png?alt=media&token=e5c7a954-0d4d-4a5c-b351-ef5ecb10f936\", \"https://firebasestorage.googleapis.com/v0/b/amicoreminders-nsfphc.appspot.com/o/storyImagesQuiz%2FElisabetta%2FLisabetta3.jpg?alt=media&token=726820f7-5c42-4ed2-8a0e-a654ae47e049\", \"https://firebasestorage.googleapis.com/v0/b/amicoreminders-nsfphc.appspot.com/o/storyImagesQuiz%2FElisabetta%2FLisabetta4.jpg?alt=media&token=53bb036e-cc24-4e20-b841-4eab0639fa5d\", \"https://firebasestorage.googleapis.com/v0/b/amicoreminders-nsfphc.appspot.com/o/storyImagesQuiz%2FElisabetta%2FLisabetta5.jpg?alt=media&token=2db1dd5e-086e-40b3-8ce9-bcb0c9275772\"]');

-- --------------------------------------------------------


--
-- Struttura della tabella `GameResults`
--

CREATE TABLE `my_bettercallpepper`.`GameResults` (
  `id_result` int NOT NULL,
  `id_elderly` int NOT NULL,
  `id_game` int NOT NULL,
  `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `score` int NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
   PRIMARY KEY (`id_result`),
   FOREIGN KEY (`id_elderly`) REFERENCES Elderlies(`id`),
   FOREIGN KEY (`id_game`) REFERENCES Games(`id_game`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `GameResults`
--

INSERT INTO `GameResults` (`id_result`, `id_elderly`, `id_game`, `creation_date`, `score`, `is_active`) VALUES
(67, 1, 1, '2024-06-27 14:56:45', 3, 0),
(66, 2, 13, '2024-06-27 13:22:40', 2, 1),
(65, 1, 10, '2024-06-27 13:20:32', 4, 1),
(64, 1, 1, '2024-06-27 13:20:14', 2, 0),
(63, 1, 1, '2024-06-27 10:37:33', 2, 0),
(62, 1, 1, '2024-06-27 10:31:05', 2, 0),
(61, 1, 11, '2024-06-22 17:51:50', 9, 1),
(60, 1, 12, '2024-06-22 17:44:46', 7, 1),
(59, 1, 12, '2024-06-22 17:43:53', 4, 0),
(58, 2, 15, '2024-06-22 17:18:13', 4, 1),
(57, 2, 14, '2024-06-22 17:17:23', 5, 1),
(56, 2, 13, '2024-06-22 17:17:12', 2, 0),
(55, 1, 2, '2024-06-22 17:16:56', 6, 1),
(54, 1, 1, '2024-06-22 17:12:43', 3, 0),
(68, 1, 1, '2024-06-29 18:27:31', 4, 1);


-- --------------------------------------------------------

--
-- Struttura della tabella `Pepper_call`
--

CREATE TABLE `my_bettercallpepper`.`Pepper_call` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(25) NOT NULL,
  `surname` varchar(25) NOT NULL,
  `filename` varchar(100) NOT NULL,
  `room` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `Pepper_call`
--

INSERT INTO `my_bettercallpepper`.`Pepper_call` (`id`, `name`, `surname`, `filename`, `room`, `type`, `created_at`) VALUES
(47, 'Tina', 'De Gennaro', 'tina_degennaro.png', 1, 0, '2021-06-17 14:18:27'),
(46, 'Tina', 'De Gennaro', 'tina_degennaro.png', 1, 0, '2021-06-17 14:18:00');

-- --------------------------------------------------------

--
-- Struttura della tabella `Questionario`
--

CREATE TABLE `my_bettercallpepper`.`Questionario` (
  `eroom` int(11) NOT NULL,
  `effettuato` int(11) NOT NULL DEFAULT '1',
  `ename` varchar(25) NOT NULL,
  `esurname` varchar(25) NOT NULL,
  `domanda1` varchar(30) NOT NULL,
  `domanda2` varchar(30) NOT NULL,
  `domanda3` varchar(30) NOT NULL,
  `domanda4` varchar(30) NOT NULL,
  `domanda5` varchar(30) NOT NULL,
  `domanda6` varchar(30) NOT NULL,
  `domanda7` varchar(30) NOT NULL,
  `domanda8` varchar(30) NOT NULL,
  `domanda9` varchar(30) NOT NULL,
  `domanda10` varchar(30) NOT NULL,
  PRIMARY KEY (`eroom`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `Questionario`
--

INSERT INTO `my_bettercallpepper`.`Questionario` (`eroom`, `effettuato`, `ename`, `esurname`, `domanda1`, `domanda2`, `domanda3`, `domanda4`, `domanda5`, `domanda6`, `domanda7`, `domanda8`, `domanda9`, `domanda10`) VALUES
(1, 0, 'Tina', 'De_Gennaro', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_'),
(3, 0, 'Maurizio', 'De_Biase', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_'),
(2, 0, 'Michele', 'Annese', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_');

-- --------------------------------------------------------

--
-- Struttura della tabella `Users`
--

CREATE TABLE `my_bettercallpepper`.`Users` (
  `mail` varchar(50) NOT NULL,
  `fullname` varchar(20) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `propic` varchar(50) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `isAdmin` char(1) NOT NULL,
  `actualid` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

--
-- Dump dei dati per la tabella `Users`
--

INSERT INTO `my_bettercallpepper`.`Users` (`mail`, `fullname`, `username`, `password`, `propic`, `id`, `isAdmin`, `actualid`) VALUES
('adminmail@mail.com', 'System Administrator', 'admin', 'adminadmin', 'admin.jpg', 1, '1', 0),
('mario@balo.it', 'Mario Balotelli', 'mario', 'mario', 'mario_balotelli.png', 2, '0', 1),
('andreadegennaro@gmail.com', 'Andrea De Gennaro', 'Andrea', 'ciaomondo', 'andrea_de gennaro.png', 3, '0', 4),
('beppecontepresidente@gmail.com', 'Beppe Conte', 'Beppe', 'conte', 'beppe_conte.jpg', 15, '0', 14),
('nicoladiliddo@gmail.com', 'Nicola Di Liddo', 'nicola', 'ciaonicola', 'nicola_diliddo.png', 14, '0', 3),
('nicoladiliddo@gmail.com', 'Nicola Di Liddo', 'nicola', 'ciaonicola', 'nicola_diliddo.png', 13, '0', 2),
('antovendi@gmail.com', 'Antonello Venditti', 'Antonello', 'bellodellozio', 'antonello_venditti.jpg', 12, '0', 13),
('mm@mail.com', 'Mario Merola', 'Mario', 'ciaomondo', 'mario_merola.jpg', 11, '0', 12),
('toniodecaro@gmail.com', 'Tonio Decaro', 'Tonio', 'decaro', 'tonio_decaro.jpg', 16, '0', 15),
('caparezza@gmail.com', 'Michele Salvemini', 'Michele', 'salvemini', 'michele_salvemini.jpg', 17, '0', 16),
('mic@gmail.com', 'Michele Salvemini', 'Michele', 'salvemini', 'michele_salvemini.jpg', 18, '0', 17),
('ciao@mondo.it', 'Ciao Mondo', 'Ciao', 'adasfas', 'ciao_mondo.jpg', 19, '0', 18);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `Calls`
--
ALTER TABLE `my_bettercallpepper`.`Calls`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=354;

--
-- AUTO_INCREMENT per la tabella `Contacts`
--
ALTER TABLE `my_bettercallpepper`.`Contacts`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT per la tabella `Elderlies`
--
ALTER TABLE `my_bettercallpepper`.`Elderlies`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT per la tabella `Pepper_call`
--
ALTER TABLE `my_bettercallpepper`.`Pepper_call`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=50;

--
-- AUTO_INCREMENT per la tabella `Users`
--
ALTER TABLE `my_bettercallpepper`.`Users`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;
