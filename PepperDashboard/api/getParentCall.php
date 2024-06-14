<?php
include '../functions.php';
echo json_encode(getParentCall($_GET['parid']));
?>