<?php
include '../functions.php';
$eldId = $_REQUEST['eldid'];
echo json_encode(getElderById($eldId));
?>