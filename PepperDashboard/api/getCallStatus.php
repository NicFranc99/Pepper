<?php
include '../functions.php';
$parID = $_REQUEST['parid'];
$eldID = $_REQUEST['eldid'];
echo json_encode(getCallStatus($parID, $eldID));
?>