<!DOCTYPE html>
<html lang="en">
<?php include 'functions.php';
session_start();
if(!isset($_SESSION['username']))
{
	header('Location: login.php');
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
    <title>Homepage</title>

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
		<?php echo mobile_header("questionario"); ?>
        <!-- END HEADER MOBILE-->

        <!-- MENU SIDEBAR-->
		<?php echo menu_sidebar("questionario"); ?>
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
                            <div class="col-lg-9">
                                <h2 class="title-1 m-b-25">Questionario <?php /*echo calltype($_GET['type']);*/ ?></h2>
                                <div class="table-responsive table--no-card m-b-40">
                                    <table class="table table-borderless table-striped table-earning">
                                        <thead>
                                            <tr>
                                                <th>Anziano</th>
                                                <th>Letto</th>
						                        <th>Effettuato</th>
                                                <th>Riposato</th>
                                                <th>Di buon umore</th>
                                                <th>Dolori fisici</th>
                                                <th>Triste</th>
                                                <th>Nervoso</th>
                                                <th>Felice</th>
                                                <th>Annoiato</th>
                                                <th>Tranquillo</th>
                                                <th>Teso</th>
                                                <th>Energico</th>
                                            </tr>
                                        </thead>
                                        <tbody>
											<?php

                                $answers = getQuestionarioAnswers();
                               /* if(empty($aswers))
                                {
                                    echo '<tr><td>Nessuna risposta</td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td></tr>';
                                } 
                                else{*/
                                    foreach($answers as $ans)
                                    {
                                        echo '<tr>';
                                        echo '<td>'.$ans['ename'].' '.$ans['esurname'].'</td>';
                                        echo '<td>'.$ans['eroom'].'</td>';
                                        if($ans['effettuato'] == 1){
                                            echo '<td>'.'Sì'.'</td>';
                                            echo'<td>'.$ans['domanda1'].'</td>';
                                            echo'<td>'.$ans['domanda2'].'</td>';
                                            echo'<td>'.$ans['domanda3'].'</td>';
                                            echo'<td>'.$ans['domanda4'].'</td>';
                                            echo'<td>'.$ans['domanda5'].'</td>';
                                            echo'<td>'.$ans['domanda6'].'</td>';
                                            echo'<td>'.$ans['domanda7'].'</td>';
                                            echo'<td>'.$ans['domanda8'].'</td>';
                                            echo'<td>'.$ans['domanda9'].'</td>';
                                            echo'<td>'.$ans['domanda10'].'</td>';
                                        }
                                        else{   
                                                echo '<td>'.'No'.'</td>';
                                                echo '<td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td>';
                                        }
                                        echo '</tr>';

                                    }

                                //}

								?>
											
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12">
                                <div class="copyright">
                                    <p>Copyright © <script>document.write(new Date().getFullYear());</script> Better Call Pepper Developers. Tutti i diritti riservati.</p>
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
	
	<div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="deleteModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-md" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="deleteModalLabel">Elimina Chiamata</h5>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<form action="action.php?action=delcall" method="post" class="form-horizontal">
				<input type="hidden" id="id" name="id">
				<input type="hidden" value="<?= $_GET['type']?>" name="type">
					<div class="modal-body">
						<p>Sei sicuro di voler eliminare questa chiamata?</p>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary" data-dismiss="modal">Annulla</button>
						<button type="submit" class="btn btn-primary">Elimina</button>
					</div>
				</form>
			</div>
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
	$('#deleteModal').on('show.bs.modal', function (event) {
	  let button = $(event.relatedTarget); // Button that triggered the modal
	  $('#id').val(button.attr('data-id'));
	  });
	</script>

</body>

</html>
<!-- end document-->
