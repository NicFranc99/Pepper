<?php
include '../functions.php';
$appID = $_REQUEST['appid'];
echo json_encode(getElderliesListContact($appID));
?>