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
		<?php echo mobile_header("peppercalls"); ?>
        <!-- END HEADER MOBILE-->

        <!-- MENU SIDEBAR-->
		<?php echo menu_sidebar("peppercalls"); ?>
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
                                <h2 class="title-1 m-b-25">Richieste Pepper <?php /*echo calltype($_GET['type']);*/ ?></h2>
                                <div class="table-responsive table--no-card m-b-40">
                                    <table class="table table-borderless table-striped table-earning">
                                        <thead>
                                            <tr>
                                                <th>Anziano</th>
                                                <th>Orario</th>
						                        <th>Motivo</th>
                                            </tr>
                                        </thead>
                                        <tbody>
											<?php
											
												
											
								if($_GET['type'] == "toapp"){
									$calls = getNotApprovedPepperCalls();
														
											
                                              //  $calls = getNotApprovedPepperCalls();
                                                if(empty($calls))
											    {
												echo '<tr><td>Nessuna richiesta di Pepper da approvare</td><td></td><td></td></tr>';
											    } 
                                                else
                                                {
                                                foreach($calls as $call)
                                                {
                                                    

                                                    echo '<tr>';
                                                    echo '<td><img class="rounded-circle mx-auto" width="120" height="120" src="'.$call['epropic'].'"> '.$call['ename'].' '.$call['esurname'].'</td>';
                                                    /*if($call['type'] == 0)
                                                        echo '<td><img height="120" width="120" src="img/icon/in.png"></td>';
                                                    else
                                                        echo '<td><img height="120" width="120" src="img/icon/out.png"></td>';*/


                                                    //nel prossimo rigo occorre inserire l'immagine di pepper (o togliere prorpio questa colonna perché inutile)
                                                    //echo '<td><img class="rounded-circle mx-auto" width="120" height="120" src="'.$call['cpropic'].'"> '.$call['cname'].' '.$call['csurname'].'</td>';
                                                    
                                                    //$calltime = 
                                                    //getFancyCalltime($call['calltime']); //durata della chiamata, nel mio caso sostituire con orario di arrivo della richiesta
                                                    //echo '<td>'.$calltime . '<br>';
                                                    echo '</td>';
                                                    echo '<td>';
                                                    /*if($_GET['type'] == "toapp")
                                                    {*/
                                                        echo '<a class="btn btn-block btn-sm btn-success" href="action.php?action=approve_pepper&id='.$call['room'].'"><i class="fas fa-thumbs-up"></i> Consenti</a>
                                                            <a class="btn btn-block btn-sm btn-danger" href="action.php?action=deny_pepper&id='.$call['room'].'"><i class="fas fa-trash-alt"></i> Nega</a>';
                                                    //}
                                                    echo '</td>';

                                                    $impegno = $call['pepper_impegnato'];
                                                    
                                                    
                                                    if($impegno == '2')
                                                    {
                                                        echo '<td>Il check mattutino è in corso</td>';
                                                    } 
                                                    else if($impegno == '1')
                                                    {
                                                        echo '<td>Una call è in corso</td>';
                                                    }
                                                    else {  echo '<td>Sta facendo altro (e!) </td>'; }
                                            
                                                    echo '</tr>';
                                                }
                                                }
									
									
									
								}
											else
											if($_GET['type'] == "failed"){
												$calls = getFailedPepperGo();	
												
												
												
												
																	
											
                                              //  $calls = getNotApprovedPepperCalls();
                                                if(empty($calls))
											    {
												echo '<tr><td>Nessuna richiesta tramite pulsante è fallita</td><td></td><td></td></tr>';
											    } 
                                                else
                                                {
                                                foreach($calls as $call)
                                                {
                                                    

                                                    echo '<tr>';
                                                    echo '<td><img class="rounded-circle mx-auto" width="120" height="120" src="'.$call['epropic'].'"> '.$call['ename'].' '.$call['esurname'].'</td>';
                                                    /*if($call['type'] == 0)
                                                        echo '<td><img height="120" width="120" src="img/icon/in.png"></td>';
                                                    else
                                                        echo '<td><img height="120" width="120" src="img/icon/out.png"></td>';*/


                                                    //nel prossimo rigo occorre inserire l'immagine di pepper (o togliere prorpio questa colonna perché inutile)
                                                    //echo '<td><img class="rounded-circle mx-auto" width="120" height="120" src="'.$call['cpropic'].'"> '.$call['cname'].' '.$call['csurname'].'</td>';
                                                    
                                                    //$calltime = 
                                                    //getFancyCalltime($call['calltime']); //durata della chiamata, nel mio caso sostituire con orario di arrivo della richiesta
                                                    //echo '<td>'.$calltime . '<br>';
                                                    echo '</td>';
                                                    
                                                    /*if($_GET['type'] == "toapp")
                                                    {*/
                                                    
                                                    echo '<td>'.$call['orario'].'</td>';
                                                                                                                                                         
                                                                                                                                                        
                                                    $tipo = $call['type'];
                                                    
                                                    
                                                    if($tipo == '1')
                                                    {
                                                        echo '<td>Localizzazione fallita   ';
                                                    } 
                                                    else {  echo '<td> Altro (e!)   '; }
                                                    
                                                    
                                                    echo '<a class="btn btn-warning" href="action.php?action=delete_failed&room='.$call['room'].'&orario='.$call['orario'].'"><i class="far fa-minus-square"></i>';
                                                   echo '</td>';
                                            
                                                    echo '</tr>';
                                                }
                                                }
						
			} 	else 
            if($_GET['type'] == "operatore"){
                $calls = getOperatoreRequestList();	
                
                
                
                
                                    
            
              //  $calls = getNotApprovedPepperCalls();
                if(empty($calls))
                {
                echo '<tr><td>Nessuna richiesta operatore da notificare</td><td></td><td></td></tr>';
                } 
                else
                {
                foreach($calls as $call)
                {
                    

                    echo '<tr>';
                    echo '<td><img class="rounded-circle mx-auto" width="120" height="120" src="'.$call['epropic'].'"> '.$call['ename'].' '.$call['esurname'].'</td>';
                    /*if($call['type'] == 0)
                        echo '<td><img height="120" width="120" src="img/icon/in.png"></td>';
                    else
                        echo '<td><img height="120" width="120" src="img/icon/out.png"></td>';*/


                    //nel prossimo rigo occorre inserire l'immagine di pepper (o togliere prorpio questa colonna perché inutile)
                    //echo '<td><img class="rounded-circle mx-auto" width="120" height="120" src="'.$call['cpropic'].'"> '.$call['cname'].' '.$call['csurname'].'</td>';
                    
                    //$calltime = 
                    //getFancyCalltime($call['calltime']); //durata della chiamata, nel mio caso sostituire con orario di arrivo della richiesta
                    //echo '<td>'.$calltime . '<br>';
                    echo '</td>';
                    
                    /*if($_GET['type'] == "toapp")
                    {*/
                    
                    echo '<td>'.$call['richiesta_operatore'].'</td>';
                                                                                                                         
                                                                                                                        
                    echo '<td>Necessità operatore al letto '.$call['room'].'   ';                  
                    
                    echo '<a class="btn btn-success" href="action.php?action=delete_oprequest&room='.$call['room'].'&orario='.$call['richiesta_operatore'].'"><i class="fa fa-check-square"></i>';
                    echo '</td>';
                    echo '</tr>';
                }
                }

} 	else                  {
												echo '<tr><td>SELEZIONA UNA SOTTOSEZIONE!</td><td></td><td></td></tr>';
											    } 
											
											
						
										    /*if($_GET['type'] == "toapp")
												$calls = getNotApprovedCalls();
											else
											if($_GET['type'] == "app")
												$calls = getApprovedCalls();
											else
												$calls = getPanelCalls($_GET['type']);
											if(empty($calls))
											{
												echo '<tr><td>Nessuna chiamata</td><td></td><td></td><td></td><td></td></tr>';
											} */
											/*else
											{
											foreach($calls as $call)
											{
												$calltime = getFancyCalltime($call['calltime']);
												echo '<tr>';
                                                echo '<td><img class="rounded-circle mx-auto" width="120" height="120" src="'.$call['epropic'].'"> '.$call['ename'].' '.$call['esurname'].'</td>';
												if($call['type'] == 0)
													echo '<td><img height="120" width="120" src="img/icon/in.png"></td>';
												else
													echo '<td><img height="120" width="120" src="img/icon/out.png"></td>';
												echo '<td><img class="rounded-circle mx-auto" width="120" height="120" src="'.$call['cpropic'].'"> '.$call['cname'].' '.$call['csurname'].'</td>';		
												echo '<td>'.$calltime . '<br>';
												echo '</td>';
												echo '<td>';
												if($_GET['type'] == "toapp")
												{
													echo '<a class="btn btn-block btn-sm btn-success" href="action.php?action=approve&id='.$call['id'].'"><i class="fas fa-thumbs-up"></i> Consenti</a>
														<a class="btn btn-block btn-sm btn-danger" href="action.php?action=deny&id='.$call['id'].'"><i class="fas fa-trash-alt"></i> Nega</a>';
												}
												echo '</td>';	
												echo '</tr>';
											}*/
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
