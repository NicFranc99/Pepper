<?php
include '../functions.php';
$room = $_REQUEST['room'];
$button = $_REQUEST['button'];

if($button == "true")
	$button = 1;
else
	$button = 0;

callPepper($room, $button);
?>