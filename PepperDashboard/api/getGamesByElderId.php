<?php
include '../functions.php';
$eldId = $_REQUEST['eldid'];

// Verificare se il parametro 'orderByGamesWithoutResults' esiste, altrimenti impostarlo su false
$orderByGamesWithoutResults = isset($_REQUEST['orderByGamesWithoutResults']) ? filter_var($_REQUEST['orderByGamesWithoutResults'], FILTER_VALIDATE_BOOLEAN) : false;

echo json_encode(getGamesByElderId($eldId,$orderByGamesWithoutResults));
?>
