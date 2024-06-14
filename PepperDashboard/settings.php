<!DOCTYPE html>
<html lang="en">
<?php
   	include 'functions.php';
	error_reporting(E_ALL);
	ini_set("display_errors", 1);
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
    <title>Impostazioni</title>

    <!-- Fontfaces CSS-->
    <link href="../css/font-face.css" rel="stylesheet" media="all">
	<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet" media="all">	
    <link href="../vendor/font-awesome-5/css/fontawesome-all.min.css" rel="stylesheet" media="all">
    <link href="../vendor/font-awesome-4.7/css/font-awesome.min.css" rel="stylesheet" media="all">
    <link href="../vendor/mdi-font/css/material-design-iconic-font.min.css" rel="stylesheet" media="all">

    <!-- Bootstrap CSS-->
    <link href="../vendor/bootstrap-4.1/bootstrap.min.css" rel="stylesheet" media="all">

    <!-- Vendor CSS-->
    <link href="../vendor/animsition/animsition.min.css" rel="stylesheet" media="all">
    <link href="../vendor/bootstrap-progressbar/bootstrap-progressbar-3.3.4.min.css" rel="stylesheet" media="all">
    <link href="../vendor/wow/animate.css" rel="stylesheet" media="all">
    <link href="../vendor/css-hamburgers/hamburgers.min.css" rel="stylesheet" media="all">
    <link href="../vendor/slick/slick.css" rel="stylesheet" media="all">
    <link href="../vendor/select2/select2.min.css" rel="stylesheet" media="all">
    <link href="../vendor/perfect-scrollbar/perfect-scrollbar.css" rel="stylesheet" media="all">

    <!-- Main CSS-->
    <link href="../css/theme.css" rel="stylesheet" media="all">

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
                            <div class="col-md-8">
                           		<?php
								if(isset($_SESSION['popup']))
                                {
									if($_SESSION['popup'] == "usernamechange" && $_SESSION['popupok'] == true)
                                    {
                                    	alert("success", "Username", "Username cambiato con successo");
                                    }
									if($_SESSION['popup'] == "usernamechange" && $_SESSION['popupok'] == false)
                                    {
                                    	alert("danger", "Username", "Errore nel cambio dell'username");
                                    }
									if($_SESSION['popup'] == "pswchange" && $_SESSION['popupok'] == true)
                                    {
                                    	alert("success", "Password", "Password cambiata con successo");
                                    }
									if($_SESSION['popup'] == "pswchange" && $_SESSION['popupok'] == false)
                                    {
                                    	alert("danger", "Password", "Errore nel cambio della password");
                                    }    
									if($_SESSION['popup'] == "avatarchange" && $_SESSION['popupok'] == true)
                                    {
                                    	alert("success", "Avatar", "Avatar cambiata con successo");
                                    }       
									if($_SESSION['popup'] == "avatarchange" && $_SESSION['popupok'] == false)
                                    {
                                    	alert("danger", "Avatar", "Errore nel cambio dell'avatar" . $_SESSION['popupdesc']);
                                    }       
                                    unset($_SESSION['popup']);
                                    unset($_SESSION['popupok']);                                
                                }
								?>
                            
                            	<form action="action.php?action=changeavatar" method="post" class="form-horizontal" enctype="multipart/form-data">
								<div class="card">
                                    <div class="card-header">
                                        Cambio Avatar
                                    </div>
                                    <div class="card-body card-block">
                                            <div class="row form-group">
                                                <div class="col col-md-5">
                                                    <label for="hf-file" class=" form-control-label">Avatar</label>
                                                </div>
                                                <div class="col-12 col-md-7">
                                                    <input type="file" accept=".gif,.jpg,.jpeg,.png" id="hf-file" name="avatar" class="form-control-file">
                                                </div>
                                            </div>
                                       </div>
                                    <div class="card-footer">
                                        <button type="submit" class="btn btn-primary btn-sm">
                                            <i class="fa fa-dot-circle-o"></i> Invia
                                        </button>
                                    </div>
                                </div>
                                </form>
                            	<form action="../action.php?action=changepassword" method="post" class="form-horizontal">
								<div class="card">
                                    <div class="card-header">
                                        Cambio Password
                                    </div>
                                    <div class="card-body card-block">
                                            <div class="row form-group">
                                                <div class="col col-md-5">
                                                    <label for="hf-password" class=" form-control-label">Vecchia password</label>
                                                </div>
                                                <div class="col-12 col-md-7">
                                                    <input type="password" id="hf-password" name="oldpsw" required placeholder="Vecchia password" class="form-control">
                                                    <span class="help-block">Inserisci la tua vecchia password</span>
                                                </div>
                                            </div>
                                            <div class="row form-group">
                                                <div class="col col-md-5">
                                                    <label for="psw" class=" form-control-label">Nuova password (minimo 8 caratteri)</label>
                                                </div>
                                                <div class="col-12 col-md-7">
                                                    <input type="password" id="psw" required name="newpsw" placeholder="Nuova password" class="form-control">
                                                    <span class="help-block">Inserisci la tua nuova password</span>
                                                </div>
                                            </div>
                                            <div class="row form-group">
                                                <div class="col col-md-5">
                                                    <label for="confirm_psw" class=" form-control-label">Conferma la nuova password (minimo 8 caratteri)</label>
                                                </div>
                                                <div class="col-12 col-md-7">
                                                    <input type="password" id="confirm_psw" name="confpsw" required placeholder="Conferma la nuova password" class="form-control">
                                                    <span class="help-block">Conferma la nuova password</span>
                                                </div>
                                            </div>
                                    		Il cambio password fallisce se la nuova password non rispetta i requisiti o se la vecchia password non corrisponde a quella salvata!
                                    </div>
                                    <div class="card-footer">
                                        <button type="submit" class="btn btn-primary btn-sm">
                                            <i class="fa fa-dot-circle-o"></i> Invia
                                        </button>
                                    </div>
                                </div>
                                </form>
                            	<form action="../action.php?action=changeusername" method="post" class="form-horizontal">
                            	<div class="card">
                                    <div class="card-header">
                                        Cambio Username
                                    </div>
                                    <div class="card-body card-block">
                                            <div class="row form-group">
                                                <div class="col col-md-5">
                                                    <label for="hf-user" class=" form-control-label">Username Corrente</label>
                                                </div>
                                                <div class="col-12 col-md-7">
                                                    <input type="email" autocomplete="off" id="hf-user" name="hf-user" disabled value="<?= $_SESSION['username']?>" class="form-control">
                                            	</div>
                                            </div>
                                            <div class="row form-group">
                                                <div class="col col-md-5">
                                                    <label for="hf-user" autocomplete="off" class=" form-control-label">Nuovo Username</label>
                                                </div>
                                                <div class="col-12 col-md-7">
                                                    <input type="text" autocomplete="off" id="hf-user" class="form-control" name="username" placeholder="Nuovo Username">
                                                	<span class="help-block">Inserisci il nuovo username</span>                                                
                                                </div>
                                            </div>
                                    </div>
                                    <div class="card-footer">
                                        <button type="submit" class="btn btn-primary btn-sm">
                                            <i class="fa fa-dot-circle-o"></i> Invia
                                        </button>
                                    </div>
                                </div>
                            	</form>
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
        </div>
        <!-- END PAGE CONTAINER-->

    <!-- Jquery JS-->
    <script src="../vendor/jquery-3.2.1.min.js"></script>
    <!-- Bootstrap JS-->
    <script src="../vendor/bootstrap-4.1/popper.min.js"></script>
    <script src="../vendor/bootstrap-4.1/bootstrap.min.js"></script>
    <!-- Vendor JS       -->
    <script src="../vendor/slick/slick.min.js">
    </script>
    <script src="../vendor/wow/wow.min.js"></script>
    <script src="../vendor/animsition/animsition.min.js"></script>
    <script src="../vendor/bootstrap-progressbar/bootstrap-progressbar.min.js">
    </script>
    <script src="../vendor/counter-up/jquery.waypoints.min.js"></script>
    <script src="../vendor/counter-up/jquery.counterup.min.js">
    </script>
    <script src="../vendor/circle-progress/circle-progress.min.js"></script>
    <script src="../vendor/perfect-scrollbar/perfect-scrollbar.js"></script>
    <script src="../vendor/chartjs/Chart.bundle.min.js"></script>
    <script src="../vendor/select2/select2.min.js">
    </script>

    <!-- Main JS-->
    <script src="../js/main.js"></script>

	<script src="../js/passvalidation.js"></script>

</body>

</html>
<!-- end document-->
