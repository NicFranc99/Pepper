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
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

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
							if($_SESSION['popup'] == "addelder" && $_SESSION['popupok'] == true)
							{
								alert("success", "Anziano", "Anziano aggiunto con successo");
							}
							if($_SESSION['popup'] == "addelder" && $_SESSION['popupok'] == false)
							{
								alert("danger", "Anziano", "Errore nell'aggiunta dell'anziano");
							}
							if($_SESSION['popup'] == "editelder" && $_SESSION['popupok'] == true)
							{
								alert("success", "Anziano", "Anziano modificato con successo");
							}
							if($_SESSION['popup'] == "editelder" && $_SESSION['popupok'] == false)
							{
								alert("danger", "Anziano", "Errore nella modifica dell'anziano");
							}								
						    unset($_SESSION['popup']);
                            unset($_SESSION['popupok']);
						}
					?>
                        <div class="row">
                            <div class="col-lg-9">
                                <h2 class="title-1 m-b-25">Anziani</h2>
                                <div class="table-responsive table--no-card m-b-40">
                                    <table class="table table-borderless table-striped table-earning">
                                        <thead>
                                            <tr>
                                                <th></th>
                                                <th>Anziano</th>
                                                <th>Status</th>
                                                <th>Sesso</th>
												<th></th>
                                            </tr>
                                        </thead>
                                        <tbody>
											<?php
											$eld = getElderliesList();
											if(empty($eld))
											{
												echo '<tr><td>Nessun anziano</td><td></td><td></td><td></td></tr>';
											} 
											else
											{
											foreach($eld as $elder)
											{
												echo '<tr>';
                                                echo '<td><img class="rounded-circle mx-auto d-block imgminsize" width="120" height="120" src="'.$elder['propic'].'"></td>';												
												echo '<td><b>' . $elder['name'] . " " . $elder['surname'] . '</b><br> nato il '.$elder['birth'].'<br>a '.$elder['place'].' ('.$elder['prov'].')<br>';
												echo '<a class="btn btn-sm btn-info" href="contacts.php?id='.$elder['id'].'"><i class="fas fa-address-book"></i> Contatti</a>';
												echo '<a class="btn btn-sm btn-info m-l-5" href="viewElderGames.php?id='.$elder['id'].'&eldName='.$elder['name'].'"><i class="bi bi-joystick"></i> Giochi</a>';
												echo '</td>';
												if($elder['status'] == 1)
													echo '<td>Active';
												else
													echo '<td>Inactive';
												echo '<br>Numero letto: ' . $elder['room'] . '</td>';
												if($elder['gender'] == 0)
													echo '<td>?</td>';
												if($elder['gender'] == 1)
													echo '<td>M</td>';
												if($elder['gender'] == 2)
													echo '<td>F</td>';
												echo '<td>
														<button class="btn btn-sm btn-danger" 
															data-toggle="modal" 
															data-id="'.$elder['id'].'" 
															data-name="'.$elder['name'].'" 
															data-surname="'.$elder['surname'].'" 
															data-target="#deleteModal"><i class="fas fa-trash-alt"></i> 
														<button class="btn btn-sm btn-warning" 
															data-toggle="modal" 
															data-id="'.$elder['id'].'" 
															data-name="'.$elder['name'].'" 
															data-img="'.$elder['propic'].'" 
															data-surname="'.$elder['surname'].'" 
															data-ddn="'.$elder['birth2'].'" 
															data-city="'.$elder['place'].'" 
															data-prov="'.$elder['prov'].'" 
															data-sex="'.$elder['gender'].'" 
															data-id="'.$elder['id'].'" 
															data-filename="'.$elder['filename'].'" 
															data-room="'.$elder['room'].'" 
															data-target="#editModal"><i class="fas fa-edit"></i>
														</td>';
												echo '</tr>';
											}
											}
											?>
											
                                        </tbody>
										<tfoot>
                                            <tr>
                                                <td><button data-toggle="modal" data-target="#elderModal"><i class="fas fa-user-plus"></i> Aggiungi</button></td>
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
	
	<div class="modal fade" id="elderModal" tabindex="-1" role="dialog" aria-labelledby="elderModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="elderModalLabel">Aggiungi Anziano</h5>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<form action="action.php?action=addelder" method="post" class="form-horizontal" enctype="multipart/form-data">
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
									<label for="ddn" class="form-control-label">Data di nascita:</label>
								</div>
								<div class="col col-sm-8">
									<input type="date" id="ddn" name="ddn" class="input-sm form-control-sm form-control" required>
								</div>
							</div>
							<div class="row form-group">					
								<div class="col col-sm-3">
									<label for="city" class="form-control-label">Citta:</label>
								</div>
								<div class="col col-sm-8">
									<input type="text" id="city" name="city" class="input-sm form-control-sm form-control" required>
								</div>
							</div>   
							<div class="row form-group">					
								<div class="col col-sm-3">
									<label for="prov" class="form-control-label">Provincia:</label>
								</div>
								<div class="col col-sm-8">
									<input type="text" id="prov" name="prov" class="input-sm form-control-sm form-control" required>
								</div>
							</div>
							<div class="row form-group">					
								<div class="col col-sm-3">
									<label for="room" class="form-control-label">Numero letto:</label>
								</div>
								<div class="col col-sm-8">
									<input type="text" id="room" name="room" class="input-sm form-control-sm form-control" required>
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
					<h5 class="modal-title" id="editModalLabel">Modifica Anziano</h5>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<form action="action.php?action=editelder" method="post" class="form-horizontal" enctype="multipart/form-data">
				<input type="hidden" id="efilename" name="filename">
				<input type="hidden" id="eid" name="id">
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
									<label for="ddn" class="form-control-label">Data di nascita:</label>
								</div>
								<div class="col col-sm-8">
									<input type="date" id="eddn" name="ddn" class="input-sm form-control-sm form-control" required>
								</div>
							</div>
							<div class="row form-group">					
								<div class="col col-sm-3">
									<label for="city" class="form-control-label">Citta:</label>
								</div>
								<div class="col col-sm-8">
									<input type="text" id="ecity" name="city" class="input-sm form-control-sm form-control" required>
								</div>
							</div>   
							<div class="row form-group">					
								<div class="col col-sm-3">
									<label for="prov" class="form-control-label">Provincia:</label>
								</div>
								<div class="col col-sm-8">
									<input type="text" id="eprov" name="prov" class="input-sm form-control-sm form-control" required>
								</div>
							</div>
							<div class="row form-group">					
								<div class="col col-sm-3">
									<label for="room" class="form-control-label">Numero letto:</label>
								</div>
								<div class="col col-sm-8">
									<input type="text" id="eroom" name="room" class="input-sm form-control-sm form-control" required>
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
					<h5 class="modal-title" id="deleteModalLabel">Elimina Anziano</h5>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<form action="action.php?action=delelder" method="post" class="form-horizontal" enctype="multipart/form-data">
				<input type="hidden" id="id" name="id">
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
	  let ddn = button.attr('data-ddn');
	  let city = button.attr('data-city');
	  let prov = button.attr('data-prov');
	  let sex = button.attr('data-sex');
	  let room = button.attr('data-room');
	  let filename = button.attr('data-filename');
	  let img = button.attr('data-img');
	  $('#eid').val(id);
	  $('#eimg').attr("src", img);
	  $('#efilename').val(filename);
	  $('#ename').val(name);
	  $('#esurname').val(surname);
	  $('#eddn').val(ddn);
	  $('#ecity').val(city);
	  $('#eprov').val(prov);
	  $('#esex').val(sex);
	  $('#eroom').val(room);
	  });	  
	</script>

</body>

</html>
<!-- end document-->
