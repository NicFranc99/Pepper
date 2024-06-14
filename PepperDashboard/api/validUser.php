<?php
include '../functions.php';
$password = $_REQUEST['password'];
$username = $_REQUEST['username'];
echo json_encode(validUser($username, $password));
?>