<?php
include '../functions.php';
$room = $_REQUEST['room'];
$go = $_REQUEST['go'];

//controllare validità valore passato
if($go != 1 && $go != 2 )
	$pepper_impegnato = 0; 

setPepperGo($room, $go);
?>