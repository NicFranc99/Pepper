<?php
include '../functions.php';
$room = $_REQUEST['room'];
$pepper_failed = $_REQUEST['failed'];

//controllare validità valore passato
if($pepper_failed != 1 )
	$pepper_failed = 0; 

setPepperFailed($room, $pepper_failed);
?>
