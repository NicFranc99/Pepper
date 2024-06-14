<?php
session_start();
require 'functions.php';

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

if($_REQUEST['action'] == "changeusername")
{
	$username = $_POST['username'];
	setUsername($_SESSION['username'], $_SESSION['password'], $username);
	$_SESSION['username'] = $username;
	$_SESSION['popup'] = "usernamechange";
	$_SESSION['popupok'] = true;
	header('Location: settings.php');
	exit();
}

if($_REQUEST['action'] == "approve_pepper")
{
	$room = $_REQUEST['id'];
	approvePepperGo($room);
	header('Location: pepperCalls.php?type=toapp');
	//header('Location: pepperCalls.php?type=toapp');
	exit();
}

if($_REQUEST['action'] == "deny_pepper")
{
	$room = $_REQUEST['id'];
	denyPepperGo($room);
	header('Location: pepperCalls.php?type=toapp');
	//header('Location: pepperCalls.php?type=toapp');
	exit();
}

if($_REQUEST['action'] == "changepassword")
{
	$oldpsw = $_POST['oldpsw'];
	$newpsw = $_POST['newpsw'];
	$confpsw = $_POST['confpsw'];
	$_SESSION['popup'] = "pswchange";
	$_SESSION['popupok'] = false;
	if(checkvalidpasswordchange($_SESSION['username'], $_SESSION['password'], $oldpsw, $newpsw, $confpsw) == 1)
    {
		$_SESSION['popupok'] = true;
		setPassword($_SESSION['username'], $_SESSION['password'], $newpsw);
    	$_SESSION['password'] = $newpsw;
    }
	header('Location: settings.php');
	exit();
}

if($_REQUEST['action'] == "approve")
{
	$id = $_REQUEST['id'];
	approve($id);
	header('Location: calls.php?type=toapp');
	exit();
}

if($_REQUEST['action'] == "deny")
{
	$id = $_REQUEST['id'];
	deny($id);
	header('Location: calls.php?type=toapp');
	exit();
}

if($_REQUEST['action'] == "addelder")
{
	$name = $_POST['name'];
	$surname = $_POST['surname'];
	$ddn = $_POST['ddn'];
	$city = $_POST['city'];
	$prov = $_POST['prov'];
	$sex = $_POST['sex'];
	$room = $_POST['room'];
	
	$_SESSION['popupok'] = false;
	$_SESSION['popup'] = "addelder";
	
	$target_dir = "img/elderlies/";
	$imageFileType = strtolower(pathinfo($_FILES["avatar"]["name"],PATHINFO_EXTENSION));
	$target_file = $target_dir . str_replace(" ", "_", strtolower($name) . "_" . strtolower($surname) . ".$imageFileType");
	$uploadOk = 1;

	// Check file size
	if ($_FILES["avatar"]["size"] > 5000000)
	{
  		$uploadOk = 0;
		$_SESSION['popupdesc'] = "File troppo grande!";
	}

	 //Allow certain file formats     
	if($imageFileType != "jpg" && $imageFileType != "png" && $imageFileType != "jpeg" && $imageFileType != "gif" )
	{
  		$uploadOk = 0;
		$_SESSION['popupdesc'] = "Formato $imageFileType non valido!";
	}

	// Check if $uploadOk is set to 0 by an error
	if ($uploadOk != 0) 
  		if (move_uploaded_file($_FILES["avatar"]["tmp_name"], $target_file))
        	$uploadOk = 1;
		else
			$_SESSION['popupdesc'] = "Errore nello spostamento del file!";
	
	if($uploadOk == 1)
    {	
    	$filename = str_replace(" ", "_", strtolower($name) . "_" . strtolower($surname) . ".$imageFileType");
		addElder($name, $surname, $ddn, $city, $prov, $sex, $filename, $room);
		$_SESSION['popupok'] = true;
		header('Location: index.php');
		exit();
    }
	else
    {
    	header('Location: index.php');
		exit();
    }
}

if($_REQUEST['action'] == "editelder")
{
	$name = $_POST['name'];
	$surname = $_POST['surname'];
	$ddn = $_POST['ddn'];
	$city = $_POST['city'];
	$prov = $_POST['prov'];
	$sex = $_POST['sex'];
	$id = $_POST['id'];
	$room = $_POST['room'];
	
	$_SESSION['popupok'] = false;
	$_SESSION['popup'] = "editelder";
	
	if(empty($_FILES))
	{
		$target_dir = "img/elderlies/";
		$imageFileType = strtolower(pathinfo($_FILES["avatar"]["name"],PATHINFO_EXTENSION));
		$target_file = $target_dir . str_replace(" ", "_", strtolower($name) . "_" . strtolower($surname) . ".$imageFileType");
		$uploadOk = 1;

		// Check file size
		if ($_FILES["avatar"]["size"] > 5000000)
		{
			$uploadOk = 0;
			$_SESSION['popupdesc'] = "File troppo grande!";
		}

		 //Allow certain file formats     
		if($imageFileType != "jpg" && $imageFileType != "png" && $imageFileType != "jpeg" && $imageFileType != "gif" )
		{
			$uploadOk = 0;
			$_SESSION['popupdesc'] = "Formato $imageFileType non valido!";
		}

		// Check if $uploadOk is set to 0 by an error
		if ($uploadOk != 0) 
			if (move_uploaded_file($_FILES["avatar"]["tmp_name"], $target_file))
				$uploadOk = 1;
			else
				$_SESSION['popupdesc'] = "Errore nello spostamento del file!";

		if($uploadOk == 1)
		{	
			$filename = str_replace(" ", "_", strtolower($name) . "_" . strtolower($surname) . ".$imageFileType");
		}
	}
	else
	{
		$filename = $_POST['filename'];
	}
	editElder($name, $surname, $ddn, $city, $prov, $sex, $filename, $id, $room);
	$_SESSION['popupok'] = true;
	header('Location: index.php');
	exit();
}

if($_REQUEST['action'] == "editcontact")
{
	$name = $_POST['name'];
	$surname = $_POST['surname'];
	$email = $_POST['mail'];
	$sex = $_POST['sex'];
	$id = $_POST['id'];
	$eldid = $_POST['eldid'];
	
	$_SESSION['popupok'] = false;
	$_SESSION['popup'] = "editcontact";
	
	if(empty($_FILES))
	{
		$target_dir = "img/contacts/";
		$imageFileType = strtolower(pathinfo($_FILES["avatar"]["name"],PATHINFO_EXTENSION));
		$target_file = $target_dir . str_replace(" ", "_", strtolower($name) . "_" . strtolower($surname) . ".$imageFileType");
		$uploadOk = 1;

		// Check file size
		if ($_FILES["avatar"]["size"] > 5000000)
		{
			$uploadOk = 0;
			$_SESSION['popupdesc'] = "File troppo grande!";
		}

		 //Allow certain file formats     
		if($imageFileType != "jpg" && $imageFileType != "png" && $imageFileType != "jpeg" && $imageFileType != "gif" )
		{
			$uploadOk = 0;
			$_SESSION['popupdesc'] = "Formato $imageFileType non valido!";
		}

		// Check if $uploadOk is set to 0 by an error
		if ($uploadOk != 0) 
			if (move_uploaded_file($_FILES["avatar"]["tmp_name"], $target_file))
				$uploadOk = 1;
			else
				$_SESSION['popupdesc'] = "Errore nello spostamento del file!";

		if($uploadOk == 1)
		{	
			$filename = str_replace(" ", "_", strtolower($name) . "_" . strtolower($surname) . ".$imageFileType");
		}
	}
	else
	{
		$filename = $_POST['filename'];
	}
	editContact($name, $surname, $email, $sex, $filename, $id);
	$_SESSION['popupok'] = true;
	header('Location: contacts.php?id=' . $eldid);
	exit();
}

if($_REQUEST['action'] == "delelder")
{
	$id = $_POST['id'];
	delElderliesFromID($id);
	header('Location: index.php');
	exit();
}

if($_REQUEST['action'] == "delcall")
{
	$id = $_POST['id'];
	$type = $_POST['type'];
	delCallsFromID($id);
	header('Location: calls.php?type=' . $type);
	exit();
}

if($_REQUEST['action'] == "delete_failed")
{
	$room = $_REQUEST['room'];
	$orario = $_REQUEST['orario'];
	delFailedRoomOrario($room,$orario);
	header('Location: pepperCalls.php?type=failed');
	exit();
}

if($_REQUEST['action'] == "delete_oprequest")
{
	$room = $_REQUEST['room'];
	$orario = $_REQUEST['richiesta_operatore'];
	delOperatoreRequest($room,$orario);
	header('Location: pepperCalls.php?type=operatore');
	exit();
}


if($_REQUEST['action'] == "delcontact")
{
	$eldid = $_POST['eldid'];
	$id = $_POST['id'];
	delContactsFromID($id);
	header('Location: contacts.php?id=' . $eldid);
	exit();
}

if($_REQUEST['action'] == "addcontact")
{
	$name = $_POST['name'];
	$surname = $_POST['surname'];
	$sex = $_POST['sex'];
	$eldid = $_POST['eldid'];
	$mail = $_POST['mail'];
	$password = $_POST['password'];
	
	$_SESSION['popupok'] = false;
	$_SESSION['popup'] = "addcontact";
	
	$target_dir = "img/contacts/";
	$imageFileType = strtolower(pathinfo($_FILES["avatar"]["name"],PATHINFO_EXTENSION));
	$target_file = $target_dir . str_replace(" ", "_", strtolower($name) . "_" . strtolower($surname) . ".$imageFileType");
	$uploadOk = 1;

	// Check file size
	if ($_FILES["avatar"]["size"] > 5000000)
	{
  		$uploadOk = 0;
		$_SESSION['popupdesc'] = "File troppo grande!";
	}

	 //Allow certain file formats     
	if($imageFileType != "jpg" && $imageFileType != "png" && $imageFileType != "jpeg" && $imageFileType != "gif" )
	{
  		$uploadOk = 0;
		$_SESSION['popupdesc'] = "Formato $imageFileType non valido!";
	}

	// Check if $uploadOk is set to 0 by an error
	if ($uploadOk != 0) 
  		if (move_uploaded_file($_FILES["avatar"]["tmp_name"], $target_file))
        	$uploadOk = 1;
		else
			$_SESSION['popupdesc'] = "Errore nello spostamento del file!";
	
	if($uploadOk == 1)
    {	
    	$filename = str_replace(" ", "_", strtolower($name) . "_" . strtolower($surname) . ".$imageFileType");
		addContact($name, $surname, $sex, $eldid, $mail, $password, $filename);
		$_SESSION['popupok'] = true;
		header('Location: contacts.php?id=' . $eldid);
		exit();
    }
	else
    {
    	header('Location: contacts.php?id=' . $eldid);
		exit();
    }
}

if($_REQUEST['action'] == "changeavatar")
{
	$_SESSION['popupok'] = false;
	$_SESSION['popup'] = "avatarchange";
   	$target_dir = "img/users/";

	$imageFileType = strtolower(pathinfo($_FILES["avatar"]["name"],PATHINFO_EXTENSION));
	$target_file = $target_dir . $_SESSION['username'] . ".$imageFileType";
	$uploadOk = 1;

	// Check file size
	if ($_FILES["avatar"]["size"] > 5000000)
	{
  		$uploadOk = 0;
		$_SESSION['popupdesc'] = "File troppo grande!";
	}

	 //Allow certain file formats     
	if($imageFileType != "jpg" && $imageFileType != "png" && $imageFileType != "jpeg" && $imageFileType != "gif" )
	{
  		$uploadOk = 0;
		$_SESSION['popupdesc'] = "Formato $imageFileType non valido!";
	}

	// Check if $uploadOk is set to 0 by an error
	if ($uploadOk != 0) 
  		if (move_uploaded_file($_FILES["avatar"]["tmp_name"], $target_file))
        	$uploadOk = 1;
		else
			$_SESSION['popupdesc'] = "Errore nello spostamento del file!";
	
	if($uploadOk == 1)
    {	
    	$filename = $_SESSION['username'].".$imageFileType";
		setAvatar($_SESSION['username'], $_SESSION['password'], $filename);
		$_SESSION['popupok'] = true;
		header('Location: settings.php');
		exit();
    }
	else
    {
    	header('Location: settings.php');
		exit();
    }
	exit();
}
