<?php
include '../functions.php';
$room = $_REQUEST['room'];
$name = $_REQUEST['name'];
$surname = $_REQUEST['surname'];
$effettuato = $_REQUEST['effettuato'];
$domanda1 = $_REQUEST['domanda1'];
$domanda2 = $_REQUEST['domanda2'];
$domanda3 = $_REQUEST['domanda3'];
$domanda4 = $_REQUEST['domanda4'];
$domanda5 = $_REQUEST['domanda5'];
$domanda6 = $_REQUEST['domanda6'];
$domanda7 = $_REQUEST['domanda7'];
$domanda8 = $_REQUEST['domanda8'];
$domanda9 = $_REQUEST['domanda9'];
$domanda10 = $_REQUEST['domanda10'];

setQuestionario($room, $name, $surname,$effettuato,
					$domanda1,
					$domanda2,
					$domanda3,
					$domanda4,
					$domanda5,
					$domanda6,
					$domanda7,
					$domanda8,
					$domanda9,
					$domanda10);
?>
