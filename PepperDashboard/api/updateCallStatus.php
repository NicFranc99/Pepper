<?php
include '../functions.php';
$parID = $_REQUEST['parid'];
$eldID = $_REQUEST['eldid'];
$status = $_REQUEST['status'];
updateCallStatus($parID, $eldID, $status);
?>