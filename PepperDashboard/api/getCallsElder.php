<?php
include '../functions.php';
$appID = $_REQUEST['appid'];
echo json_encode(getCalls2($appID));
?>