<?php
include '../functions.php';
$room = $_REQUEST['room'];
$pepper_impegnato = $_REQUEST['motivation'];

//controllare validità valore passato
if($pepper_impegnato != 1 && $pepper_impegnato != 2 && $pepper_impegnato != 3 )
	$pepper_impegnato = 0; 

setPepperImpegnato($room, $pepper_impegnato);
?>