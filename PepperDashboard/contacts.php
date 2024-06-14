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
					<?php
					if(isset($_SESSION['popup']))
						{
							if($_SESSION['popup'] == "addcontact" && $_SESSION['popupok'] == true)
							{
								alert("success", "Contatto", "Contatto aggiunto con successo");
							}
							if($_SESSION['popup'] == "addcontact" && $_SESSION['popupok'] == false)
							{
								alert("danger", "Contatto", "Errore nell'aggiunta del contatto");
							}
							if($_SESSION['popup'] == "editcontact" && $_SESSION['popupok'] == true)
							{
								alert("success", "Contatto", "Contatto modificato con successo");
							}
							if($_SESSION['popup'] == "editcontact" && $_SESSION['popupok'] == false)
							{
								alert("danger", "Contatto", "Errore nella modifica del contatto");
							}
						    unset($_SESSION['popup']);
                            unset($_SESSION['popupok']);
						}
						?>
                        <div class="row">
                            <div class="col-lg-9">
                                <h2 class="title-1 m-b-25">Contatti</h2>
                                <div class="table-responsive table--no-card m-b-40">
                                    <table class="table table-borderless table-striped table-earning">
                                        <thead>
                                            <tr>
                                                <th></th>
                                                <th>Contatto</th>
                                                <th>Sesso</th>
												<th></th>
                                            </tr>
                                        </thead>
                                        <tbody>
											<?php
											$cont = getContactsFromID($_GET['id']);
											if(empty($cont))
											{
												echo '<tr><td>Nessun contatto</td><td></td><td></td><td></td></tr>';
											} 
											else
											{
											foreach($cont as $contact)
											{
												echo '<tr>';
                                                echo '<td><img width="120" height="120" class="rounded-circle mx-auto d-block" src="img/contacts/'.$contact['propic'].'"></td>';												
												echo '<td><b>' . $contact['name'] . " " . $contact['surname'] . '</b><br>'.$contact['mail'].'</td>';
												if($contact['gender'] == 0)
													echo '<td>?</td>';
												if($contact['gender'] == 1)
													echo '<td>M</td>';
												if($contact['gender'] == 2)
													echo '<td>F</td>';
												echo '<td><button class="btn btn-sm btn-danger" 
														data-toggle="modal" 
														data-id="'.$contact['id'].'" 
														data-name="'.$contact['name'].'" 
														data-surname="'.$contact['surname'].'" 
														data-target="#deleteModal"><i class="fas fa-trash-alt"></i>';	
												echo '<button class="btn btn-sm btn-warning" 
														data-toggle="modal" 
														data-id="'.$contact['id'].'" 
														data-name="'.$contact['name'].'" 
														data-img="'.$contact['epropic'].'" 
														data-sex="'.$contact['gender'].'" 
														data-surname="'.$contact['surname'].'" 
														data-filename="'.$contact['propic'].'" 
														data-mail="'.$contact['mail'].'" 
														data-target="#editModal"><i class="fas fa-edit"></i></td>';
												echo '</tr>';
											}
											}
											?>
											
                                        </tbody>
										<tfoot>
                                            <tr>
                                                <td><button data-toggle="modal" data-target="#contactsModal"><i class="fas fa-user-plus"></i> Aggiungi</button></td>
                                            </tr>
                                        </tfoot>
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
	
	<div class="modal fade" id="contactsModal" tabindex="-1" role="dialog" aria-labelledby="contactsModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="contactsModalLabel">Aggiungi Contatto</h5>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<form action="action.php?action=addcontact" method="post" class="form-horizontal" enctype="multipart/form-data">
				<input type="hidden" name="eldid" value="<?= $_GET['id']?>">
					<div class="modal-body">
						<div class="row">
						<div class="col-sm-3">
							<img id="img" src="img/icon/notfound.jpg" height="130" width="130">
							<br><br>
							<input type="file" accept=".gif,.jpg,.jpeg,.png" id="hf-file" name="avatar" class="form-control-file" required>
						</div>
						<div class="col-sm-9">
							<div class="row form-group">				
								<div class="col col-sm-3">
									<label for="name" class="form-control-label">Nome:</label>
								</div>
								<div class="col col-sm-8">
									<input type="text" id="name" name="name" class="input-sm form-control-sm form-control" required>
								</div>
							</div>
							<div class="row form-group">					
								<div class="col col-sm-3">
									<label for="surname" class="form-control-label">Cognome:</label>
								</div>
								<div class="col col-sm-8">
									<input type="text" id="surname" name="surname" class="input-sm form-control-sm form-control" required>
								</div>
							</div>
							<div class="row form-group">					
								<div class="col col-sm-3">
									<label for="mail" class="form-control-label">Email:</label>
								</div>
								<div class="col col-sm-8">
									<input type="email" id="mail" name="mail" class="input-sm form-control-sm form-control" required>
								</div>
							</div>
							<div class="row form-group">					
								<div class="col col-sm-3">
									<label for="password" class="form-control-label">Password:</label>
								</div>
								<div class="col col-sm-8">
									<input type="password" id="password" name="password" class="input-sm form-control-sm form-control" required>
								</div>
							</div>							
							<div class="row form-group">					
								<div class="col col-sm-3">
									<label for="sexselect" class="form-control-label">Sesso:</label>
								</div>
								<div class="col col-sm-8">
									<select name="sex" id="sex" class="form-control-sm form-control">
										<option value="0">Seleziona</option>
										<option value="1">M</option>
										<option value="2">F</option>
									</select>
								</div>
							</div>						
						</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary" data-dismiss="modal">Annulla</button>
						<button type="submit" class="btn btn-primary">Aggiungi</button>
					</div>
				</form>
			</div>
		</div>
	</div>
	
	<div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="editModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="editModalLabel">Modifica Contatto</h5>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<form action="action.php?action=editcontact" method="post" class="form-horizontal" enctype="multipart/form-data">
				<input type="hidden" id="efilename" name="filename">
				<input type="hidden" id="eid" name="id">
				<input type="hidden" name="eldid" value="<?= $_GET['id']?>">
					<div class="modal-body">
						<div class="row">
						<div class="col-sm-3">
							<img id="eimg" height="130" width="130">
							<br><br>
							<input type="file" accept=".gif,.jpg,.jpeg,.png" id="efile" name="avatar" class="form-control-file">
						</div>
						<div class="col-sm-9">
							<div class="row form-group">				
								<div class="col col-sm-3">
									<label for="name" class="form-control-label">Nome:</label>
								</div>
								<div class="col col-sm-8">
									<input type="text" id="ename" name="name" class="input-sm form-control-sm form-control" required>
								</div>
							</div>
							<div class="row form-group">					
								<div class="col col-sm-3">
									<label for="surname" class="form-control-label">Cognome:</label>
								</div>
								<div class="col col-sm-8">
									<input type="text" id="esurname" name="surname" class="input-sm form-control-sm form-control" required>
								</div>
							</div>
							<div class="row form-group">					
								<div class="col col-sm-3">
									<label for="mail" class="form-control-label">Email:</label>
								</div>
								<div class="col col-sm-8">
									<input type="email" id="email" name="mail" class="input-sm form-control-sm form-control" required>
								</div>
							</div>   
							<div class="row form-group">					
								<div class="col col-sm-3">
									<label for="sexselect" class="form-control-label">Sesso:</label>
								</div>
								<div class="col col-sm-8">
									<select name="sex" id="esex" class="form-control-sm form-control">
										<option value="0">Seleziona</option>
										<option value="1">M</option>
										<option value="2">F</option>
									</select>
								</div>
							</div>						
						</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary" data-dismiss="modal">Annulla</button>
						<button type="submit" class="btn btn-primary">Modifica</button>
					</div>
				</form>
			</div>
		</div>
	</div>
	
	<div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="deleteModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-md" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="deleteModalLabel">Elimina Contatto</h5>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<form action="action.php?action=delcontact" method="post" class="form-horizontal" enctype="multipart/form-data">
				<input type="hidden" id="id" name="id">
				<input type="hidden" value="<?= $_GET['id']?>" name="eldid">
					<div class="modal-body">
						<p id="data"></p>
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
	  let name = button.attr('data-name');
	  let surname = button.attr('data-surname');
	  $('#data').html("Sei sicuro di voler eliminare " + name + " " + surname + "?");
	  $('#id').val(button.attr('data-id'));
	  });
	  
	$('#editModal').on('show.bs.modal', function (event) {
	  let button = $(event.relatedTarget); // Button that triggered the modal
	  let id = button.attr('data-id');
	  let name = button.attr('data-name');
	  let surname = button.attr('data-surname');
	  let sex = button.attr('data-sex');
	  let img = button.attr('data-img');
	  let mail = button.attr('data-mail');
	  let filename = button.attr('data-filename');
	  $('#eid').val(id);
	  $('#eimg').attr("src", img);
	  $('#efilename').val(filename);
	  $('#ename').val(name);
	  $('#esurname').val(surname);
	  $('#esex').val(sex);
	  $('#email').val(mail);
	  });  
	</script>

</body>

</html>
<!-- end document-->
