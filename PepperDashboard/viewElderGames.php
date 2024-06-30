<!DOCTYPE html>
<html lang="en">
<?php include 'functions.php';
session_start();
if (!isset($_SESSION['username'])) {
    header('Location: login.php');
    header('Content-Type: text/html; charset=utf-8');
    exit();
}
?>

<head>
    <!-- Required meta tags-->
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="au theme template">
    <meta name="author" content="Hau Nguyen">
    <meta name="keywords" content="au theme template">

    <!-- Title Page-->
    <title>Contatti</title>

    <!-- Fontfaces CSS-->
    <link href="css/font-face.css" rel="stylesheet" media="all">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet" media="all">
    <link href="vendor/font-awesome-4.7/css/font-awesome.min.css" rel="stylesheet" media="all">
    <link href="vendor/font-awesome-5/css/fontawesome-all.min.css" rel="stylesheet" media="all">
    <link href="vendor/mdi-font/css/material-design-iconic-font.min.css" rel="stylesheet" media="all">

    <!-- Bootstrap CSS-->
    <link href="vendor/bootstrap-4.1/bootstrap.min.css" rel="stylesheet" media="all">

    <!-- Vendor CSS-->
    <link href="vendor/animsition/animsition.min.css" rel="stylesheet" media="all">
    <link href="vendor/bootstrap-progressbar/bootstrap-progressbar-3.3.4.min.css" rel="stylesheet" media="all">
    <link href="vendor/wow/animate.css" rel="stylesheet" media="all">
    <link href="vendor/css-hamburgers/hamburgers.min.css" rel="stylesheet" media="all">
    <link href="vendor/slick/slick.css" rel="stylesheet" media="all">
    <link href="vendor/select2/select2.min.css" rel="stylesheet" media="all">
    <link href="vendor/perfect-scrollbar/perfect-scrollbar.css" rel="stylesheet" media="all">

    <!-- Main CSS-->
    <link href="css/theme.css" rel="stylesheet" media="all">

</head>

<body class="animsition">
    <div class="page-wrapper">
        <!-- HEADER MOBILE-->
        <?php echo mobile_header("index"); ?>
        <!-- END HEADER MOBILE-->

        <!-- MENU SIDEBAR-->
        <?php echo menu_sidebar("index"); ?>
        <!-- END MENU SIDEBAR-->

        <!-- PAGE CONTAINER-->
        <div class="page-container">
            <!-- HEADER DESKTOP-->
            <?php echo header_desktop(); ?>
            <!-- HEADER DESKTOP-->

            <!-- MAIN CONTENT-->
            <div class="main-content">
                <div class="section__content section__content--p30">
                    <div class="container-fluid">
                        <div class="row">
                            <div class="col-lg-12">
                                <h2 class="title-1 m-b-25">Giochi Paziente  <?php echo '<strong>';echo $_GET['eldName']; echo'</strong>'; ?>  </h2>
                                <div class="table-responsive table--no-card m-b-40">
                                    <table class="table table-borderless table-striped table-earning">
                                        <thead>
                                            <tr>
                                                <th>Nome Gioco</th>
                                                <th>Categoria</th>
                                                <th>Punteggio</th>
                                                <th>Data Punteggio</th>
                                                <th>Risposte</th>
                                                <th>Domanda</th>
                                                <th>Esercizi</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <?php
                                            $games = getGamesByElderId($_GET['id'], false);
                                            if (empty($games)) {
                                                echo '<tr><td>Nessun Gioco Assegnato al Paziente</td><td>-</td><td>-</td><td>-</td><td>-</td><td>-</td></tr>';
                                            } else {
                                                foreach ($games as $game) {
                                                    echo '<tr>';
                                                    echo '<td>' . $game['titleGame'] . '</td>';
                                                    echo '<td>' . $game['nameCategory'] . '</td>';
                                                    echo '<td>';
                                                    if (empty($game['score']))
                                                        echo '-';
                                                    else echo $game['score'];
                                                    '</td>';
                                                    echo '<td>';
                                                    if (empty($game['creationDateResult']))
                                                        echo '-';
                                                    else echo $game['creationDateResult'];
                                                    '</td>';
                                                    echo '<td>';
                                                    if (empty($game['risposte']))
                                                        echo '-';
                                                    else echo $game['risposte'];
                                                    '</td>';
                                                    echo '<td>';
                                                    if (empty($game['domanda']))
                                                        echo '-';
                                                    else  echo $game['domanda'];
                                                    '</td>';
                                                    echo '<td>' . $game['esercizi'] . '</td>';
                                                }
                                            }
                                            ?>

                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12">
                                <div class="copyright">
                                    <p>Copyright © <script>
                                            document.write(new Date().getFullYear());
                                        </script> Better Call Pepper Developers. Tutti i diritti riservati.</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- END MAIN CONTENT-->
            <!-- END PAGE CONTAINER-->
        </div>

    </div>


    <!-- Jquery JS-->
    <script src="vendor/jquery-3.2.1.min.js"></script>
    <!-- Bootstrap JS-->
    <script src="vendor/bootstrap-4.1/popper.min.js"></script>
    <script src="vendor/bootstrap-4.1/bootstrap.min.js"></script>
    <!-- Vendor JS       -->
    <script src="vendor/slick/slick.min.js">
    </script>
    <script src="vendor/wow/wow.min.js"></script>
    <script src="vendor/animsition/animsition.min.js"></script>
    <script src="vendor/bootstrap-progressbar/bootstrap-progressbar.min.js">
    </script>
    <script src="vendor/counter-up/jquery.waypoints.min.js"></script>
    <script src="vendor/counter-up/jquery.counterup.min.js">
    </script>
    <script src="vendor/circle-progress/circle-progress.min.js"></script>
    <script src="vendor/perfect-scrollbar/perfect-scrollbar.js"></script>
    <script src="vendor/chartjs/Chart.bundle.min.js"></script>
    <script src="vendor/select2/select2.min.js">
    </script>

    <!-- Main JS-->
    <script src="js/main.js"></script>
    <script>
    </script>

</body>

</html>
<!-- end document-->