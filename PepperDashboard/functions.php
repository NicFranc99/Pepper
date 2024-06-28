<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

function db_connect() {
	try 
	{
    	$hostname = "localhost";
    	$dbname = "my_bettercallpepper";
    	$user = 'root';
    	$pass = '';
    	$db = new PDO ("mysql:host=$hostname;dbname=$dbname", $user, $pass);
	} 
	catch (PDOException $e) {
    	echo "Errore: " . $e->getMessage();
    	die();
	}
	return $db;
}

function setQuestionario($room, $name, $surname,$effettuato, $domanda1, $domanda2,
						$domanda3, $domanda4, $domanda5, $domanda6,
						$domanda7, $domanda8, $domanda9, $domanda10){
	
	$conn = db_connect();	
	$sql = "DELETE FROM Questionario WHERE eroom = :room";	
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
		':room' => $room
		));

	$sql =  "INSERT INTO Questionario(eroom, effettuato, ename, esurname, domanda1, domanda2, domanda3, domanda4, domanda5, domanda6, domanda7, domanda8, domanda9, domanda10)
			VALUES (:eroom,:effettuato, :ename,:esurname,:d1, :d2, :d3, :d4, :d5, :d6, :d7, :d8, :d9, :d10)";	
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
		':eroom' => $room,
		':effettuato' => $effettuato,
		':ename' => $name,
		':esurname' => $surname,
		':d1' => $domanda1,
		':d2' => $domanda2,
		':d3' => $domanda3,
	        ':d4' => $domanda4,
		':d5' => $domanda5,
		':d6' => $domanda6,
		':d7' => $domanda7,
		':d8' => $domanda8,
		':d9' => $domanda9,
		':d10' => $domanda10
		));
}

function login($username, $password)
{
	$conn = db_connect();

	$sql = "SELECT id FROM Users WHERE username = :username AND password = :password AND isAdmin = '1'";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':username' => $username,
    ':password' => $password
	));
	if($stmt->rowCount() == 1)
    {
		$row = $stmt->fetch(PDO::FETCH_ASSOC);
    	return $row["id"];
    }
	else
    	return 0;
}

function validUser($username, $password)
{
	$conn = db_connect();

	$sql = "SELECT actualid as id, fullname FROM Users WHERE username = :username AND password = :password AND isAdmin = '0'";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':username' => $username,
    ':password' => $password
	));
	if($stmt->rowCount() == 1)
    {
		$row = $stmt->fetch(PDO::FETCH_ASSOC);
    	return $row;
    }
	else
    	return 0;
}

function getElderliesList()
{
	$conn = db_connect();

	$sql = "SELECT CONCAT('https://bettercallpepper.altervista.org/img/elderlies/', Elderlies.propic) as propic, 
	name, id, surname, place, prov, status, gender, DATE_FORMAT(birth, '%d/%m/%Y') as birth, 
	DATE_FORMAT(birth, '%Y-%m-%d') as birth2, propic as filename, room, pepper_impegnato, pepper_go, 
	CASE WHEN pulsante = '1' THEN 'true' ELSE 'false' END as pulsante FROM Elderlies";
	$stmt = $conn->prepare($sql);
	$stmt->execute();
	if($stmt->rowCount() > 0)
    {
		$list = $stmt->fetchAll(PDO::FETCH_ASSOC);
    	return $list;
    }
	else
    	return;
}

function updateCallStatus($parID, $eldID, $status)
{
	$parID = filter_var($parID, FILTER_SANITIZE_NUMBER_INT);
	$eldID = filter_var($eldID, FILTER_SANITIZE_NUMBER_INT);
	$status = filter_var($status, FILTER_SANITIZE_NUMBER_INT);
	
	$conn = db_connect();

	$sql = "UPDATE Calls SET isActive = :status WHERE parID = :parID AND eldID = :eldID AND isActive > 0";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':parID' => $parID,
    ':eldID' => $eldID,
	':status' => $status
	));
	
	if($status == 0)
	{
		$sql = "UPDATE Calls SET ended_at = NOW() WHERE parID = :parID AND eldID = :eldID AND isActive = 0 ORDER BY Calls.created_at DESC LIMIT 1";
		$stmt = $conn->prepare($sql);
		$stmt->execute(array(
		':parID' => $parID,
		':eldID' => $eldID
	));
	}
}

function getCallStatus($parID, $eldID)
{
	$parID = filter_var($parID, FILTER_SANITIZE_NUMBER_INT);
	$eldID = filter_var($eldID, FILTER_SANITIZE_NUMBER_INT);
	
	$conn = db_connect();

	$sql = "SELECT isActive FROM Calls WHERE parID = :parID AND eldID = :eldID AND isActive > 0";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':parID' => $parID,
    ':eldID' => $eldID
	));
	if($stmt->rowCount() > 0)
    {
		$list = $stmt->fetch(PDO::FETCH_ASSOC);
    	return $list;
    }
	else
    	return;
}

function getElderliesListContact($parID)
{
	$conn = db_connect();
	
	$parID = filter_var($parID, FILTER_SANITIZE_STRING);

	$sql = "SELECT Elderlies.name, Elderlies.id, Elderlies.surname, CONCAT('https://bettercallpepper.altervista.org/img/elderlies/', Elderlies.propic) as propic FROM Elderlies INNER JOIN Contacts ON elderID = Elderlies.id WHERE Contacts.id = :parID";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':parID' => $parID));
	if($stmt->rowCount() > 0)
    {
		$list = $stmt->fetchAll(PDO::FETCH_ASSOC);
    	return $list;
    }
	else
    	return;
}

function getParentsListContact($eldID)
{
	$conn = db_connect();
	
	$eldID = filter_var($eldID, FILTER_SANITIZE_STRING);

	$sql = "SELECT Contacts.name, Contacts.id, Contacts.surname, 
	CONCAT('https://bettercallpepper.altervista.org/img/contacts/', Contacts.propic) as propic 
	FROM Contacts INNER JOIN Elderlies ON Contacts.elderID = Elderlies.id WHERE Elderlies.id = :eldID";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':eldID' => $eldID));
	if($stmt->rowCount() > 0)
    {
		$list = $stmt->fetchAll(PDO::FETCH_ASSOC);
    	return $list;
    }
	else
    	return;
}

function getUserData()
{
	$conn = db_connect();

	$sql = "SELECT mail, fullname, propic FROM Users WHERE username = :username";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(':username' => $_SESSION['username']));
	if($stmt->rowCount() == 1)
    {
		$list = $stmt->fetch(PDO::FETCH_ASSOC);
    	return $list;
    }
	else
    	return;
}

function getContactsFromID($id)
{
	$conn = db_connect();

	$id = filter_var($id, FILTER_SANITIZE_NUMBER_INT);

	$sql = "SELECT Contacts.*, mail, CONCAT('https://bettercallpepper.altervista.org/img/contacts/', Contacts.propic) as epropic FROM Contacts RIGHT JOIN Users ON Contacts.id = Users.actualid WHERE elderID = :id";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(':id' => $id));
	if($stmt->rowCount() > 0)
    {
		$list = $stmt->fetchAll(PDO::FETCH_ASSOC);
    	return $list;
    }
	else
    	return;	
}

function delContactsFromID($id)
{
	$conn = db_connect();

	$id = filter_var($id, FILTER_SANITIZE_NUMBER_INT);

	$sql = "DELETE FROM Contacts WHERE id = :id";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(':id' => $id));
}

function delOperatoreRequest($room,$orario){
	$conn = db_connect();
	$sql = "UPDATE Elderlies SET richiesta_operatore = NULL WHERE id = :room";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(':room' => $room));
}

function delFailedRoomOrario($room,$orario)
{
	$conn = db_connect();

	$room = filter_var($room, FILTER_SANITIZE_NUMBER_INT);

	$sql = "DELETE FROM Pepper_call WHERE room = :room  and created_at = :orario";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(':room' => $room, ':orario' => $orario));
}

function delCallsFromID($id)
{
	$conn = db_connect();

	$id = filter_var($id, FILTER_SANITIZE_NUMBER_INT);

	$sql = "DELETE FROM Calls WHERE id = :id";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(':id' => $id));
}

function delElderliesFromID($id)
{
	$conn = db_connect();

	$id = filter_var($id, FILTER_SANITIZE_NUMBER_INT);

	$sql = "DELETE FROM Elderlies WHERE id = :id";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(':id' => $id));
}

function addParentCall($parID, $eldID)
{
	$conn = db_connect();

	$parID = filter_var($parID, FILTER_SANITIZE_NUMBER_INT);
	$eldID = filter_var($eldID, FILTER_SANITIZE_NUMBER_INT);

	$sql = "INSERT INTO Calls(parID, eldID, type, created_at, isActive) VALUES (:parID, :eldID, 1, NOW(), 1)";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':parID' => $parID,
    ':eldID' => $eldID
	));	
}

function getParentCall($parID)
{
	$conn = db_connect();

	$parID = filter_var($parID, FILTER_SANITIZE_NUMBER_INT);

	$sql = "SELECT Elderlies.name, Elderlies.surname, Elderlies.id FROM Elderlies INNER JOIN Calls ON eldID = Elderlies.id WHERE parID = :parID AND isActive = 1 AND isApproved = '1' AND type = 1";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':parID' => $parID
	));	
	if($stmt->rowCount() > 0)
    {
		$list = $stmt->fetchAll(PDO::FETCH_ASSOC);
    	return $list;
    }
	else
    	return;	
}

function addGameResult($idelder, $idgame,$score)
{
	$conn = db_connect();

	$idelder = filter_var($idelder, FILTER_SANITIZE_NUMBER_INT);
	$idgame = filter_var($idgame, FILTER_SANITIZE_NUMBER_INT);
	$score = filter_var($score, FILTER_SANITIZE_NUMBER_INT);
	
	    // Controlla se esiste già una riga per idgame e idElder con isActive = 1
    $sql_check = "SELECT id_result FROM GameResults WHERE id_elderly = :idelder AND id_game = :idgame AND is_active = 1;";
    $stmt_check = $conn->prepare($sql_check);
    $stmt_check->execute(array(':idelder' => $idelder, ':idgame' => $idgame));
    $row = $stmt_check->fetch(PDO::FETCH_ASSOC);

    if ($row) {
        // Esiste già una riga con isActive = 1, esegui un UPDATE per impostare isActive = 0
        $id_result = $row['id_result'];
        $sql_update = "UPDATE GameResults SET is_active = 0 WHERE id_result = :id_result";
        $stmt_update = $conn->prepare($sql_update);
        $stmt_update->execute(array(':id_result' => $id_result));
    }

    // Esegui un INSERT per aggiungere i nuovi dati
	$sql = "INSERT INTO GameResults(id_elderly,id_game,score) VALUES (:idelder, :idgame, :score)";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':idelder' => $idelder,
    ':idgame' => $idgame,
    ':score' => $score
	));
}

function addElderCall($parID, $eldID)
{
	$conn = db_connect();

	$parID = filter_var($parID, FILTER_SANITIZE_NUMBER_INT);
	$eldID = filter_var($eldID, FILTER_SANITIZE_NUMBER_INT);

	$sql = "INSERT INTO Calls(parID, eldID, type, created_at, isActive) VALUES (:parID, :eldID, 0, NOW(), 1)";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':parID' => $parID,
    ':eldID' => $eldID
	));
}

function getAllCategory()
{
	$conn = db_connect();

	$sql = "SELECT Categories.id_category,Categories.name_category,Categories.description_explaitation_category FROM Categories";
	$stmt = $conn->prepare($sql);
	$stmt->execute();

	$list = $stmt->fetchAll(PDO::FETCH_ASSOC);

    return $list;
}

function getGamesByElderId($eldID,$orderByGamesWithoutResults)
{

	$conn = db_connect();

	$eldID = htmlspecialchars($eldID);
	$orderByGamesWithoutResults = htmlspecialchars($orderByGamesWithoutResults);
	
	if($orderByGamesWithoutResults == false )
	{
				$sql = "SELECT 
    eld.id AS idElderly,
    eld.name AS nameElderly,
    gm.id_game AS idGame,
    gm.id_category AS idCategory,
    gm.name_game AS titleGame,
    gm.risposte AS risposte,
    gm.esercizi AS esercizi,
    gm.mediaUrl AS mediaUrl,
    gm.freeText,
    gm.domanda,
    c.name_category AS nameCategory,
    c.description_explaitation_category AS explaitationText,
    c.has_vocal_input AS hasVocalInput,
    gr.id_result AS idResult,
    gr.score AS score,
    gr.creation_date AS creationDateResult,
    gr.is_active AS isActive
FROM Elderlies eld 
INNER JOIN Games gm ON eld.id = gm.id_elderly
INNER JOIN Categories c ON gm.id_category = c.id_category
LEFT JOIN GameResults gr ON gr.id_elderly = eld.id AND gr.id_game = gm.id_game
WHERE eld.id = :eldID AND (gr.is_active = 1 OR gr.is_active IS NULL);";
	}
	else{
		$sql = "SELECT 
    eld.id AS idElderly,
    eld.name AS nameElderly,
    gm.id_game AS idGame,
    gm.id_category AS idCategory,
    gm.name_game AS titleGame,
    gm.risposte AS risposte,
    gm.esercizi AS esercizi,
    gm.mediaUrl AS mediaUrl,
    gm.domanda,
    gm.freeText,
    c.name_category AS nameCategory,
    c.description_explaitation_category AS explaitationText,
    c.has_vocal_input AS hasVocalInput,
    gr.id_result AS idResult,
    gr.score AS score,
    gr.creation_date AS creationDateResult,
    gr.is_active AS isActive
FROM Elderlies eld 
INNER JOIN Games gm ON eld.id = gm.id_elderly
INNER JOIN Categories c ON gm.id_category = c.id_category
LEFT JOIN GameResults gr ON gr.id_elderly = eld.id AND gr.id_game = gm.id_game
WHERE eld.id = :eldID AND (gr.is_active = 1 OR gr.is_active IS NULL)
ORDER BY 
    CASE   
        WHEN gr.is_active IS NULL THEN 0
        ELSE 1                            
    END,
    idGame;";
	}


		$stmt = $conn->prepare($sql);
		$stmt->execute(array(':eldID' => $eldID));

		$list = $stmt->fetchAll(PDO::FETCH_ASSOC);

   		 return $list;
}

function getElderById($eldID)
{

	$conn = db_connect();

	$eldID = htmlspecialchars($eldID);
	$sql = "SELECT 
	        eld.id as idEderly,
	        eld.name as name,
	        eld.surname as surname,
	        eld.place as city,
	        eld.room as room,
	        eld.gender as gender,
	        eld.birth as birthDate,
	        eld.propic as propic
	        
		FROM Elderlies eld 
		WHERE eld.id = :eldID;";

		$stmt = $conn->prepare($sql);
		$stmt->execute(array(':eldID' => $eldID));

		$list = $stmt->fetch(PDO::FETCH_ASSOC);
    	return $list;
}

function getElderCall($eldID)
{
	$conn = db_connect();
	
	$eldID = filter_var($eldID, FILTER_SANITIZE_STRING);

	if($eldID != "all")
	{
		$sql = "SELECT Contacts.name, Contacts.surname, Contacts.id, CONCAT('https://bettercallpepper.altervista.org/img/contacts/', Contacts.propic) as propic,
		Elderlies.name as ename, Elderlies.surname as esurname, CONCAT('https://bettercallpepper.altervista.org/img/elderlies/', Elderlies.propic) as epropic, Elderlies.id as eid
		FROM Contacts INNER JOIN Calls ON elderID = Calls.eldid
		INNER JOIN Elderlies ON elderID = Elderlies.id
		WHERE eldID = :eldID AND isActive = '1' AND isApproved = '1' AND type = 0 ORDER BY created_at DESC LIMIT 1";
		$stmt = $conn->prepare($sql);
		$stmt->execute(array(
		':eldID' => $eldID
		));	
	}
	else
	{
		$sql = "SELECT Contacts.name, Contacts.surname, Contacts.id, CONCAT('https://bettercallpepper.altervista.org/img/contacts/', Contacts.propic) as propic,
		Elderlies.name as ename, Elderlies.surname as esurname, CONCAT('https://bettercallpepper.altervista.org/img/elderlies/', Elderlies.propic) as epropic, Elderlies.id as eid
		FROM Contacts INNER JOIN Calls ON elderID = Calls.eldid
		INNER JOIN Elderlies ON elderID = Elderlies.id 
		WHERE isActive = '1' AND isApproved = '1' AND type = 0 ORDER BY created_at DESC LIMIT 1";
		$stmt = $conn->prepare($sql);
		$stmt->execute();	
	}	
	if($stmt->rowCount() > 0)
    {
		$list = $stmt->fetchAll(PDO::FETCH_ASSOC);
    	return $list;
    }
	else
    	return;	
}

function getCalls($id)
{
	$conn = db_connect();

	$id = filter_var($id, FILTER_SANITIZE_NUMBER_INT);

		$sql = "SELECT CASE
					WHEN type = '1' then CONCAT('Hai ricevuto una chiamata da ', Elderlies.name)
					WHEN type = '0' then CONCAT('Hai effettuato una chiamata a ', Elderlies.name)
				END as callmsg,
				CASE
					WHEN type = '1' then 'https://bettercallpepper.altervista.org/img/utils/in.png'
					WHEN type = '0' then 'https://bettercallpepper.altervista.org/img/utils/out.png'
				END as img,
				type, TIMESTAMPDIFF(SECOND,created_at,ended_at) as calltime FROM Calls INNER JOIN Elderlies ON eldID = Elderlies.id WHERE parID = :id AND isActive = 0 ORDER BY Calls.id DESC";
		$stmt = $conn->prepare($sql);
		$stmt->execute(array(':id' => $id));
		if($stmt->rowCount() > 0)
		{
			$calls = $stmt->fetchAll(PDO::FETCH_ASSOC);
			return $calls;
		}
		else
			return;	
}

function getCalls2($id)
{
	$conn = db_connect();

	$id = filter_var($id, FILTER_SANITIZE_NUMBER_INT);

		$sql = "SELECT CASE
					WHEN type = '0' then CONCAT('Hai ricevuto una chiamata da ', Contacts.name)
					WHEN type = '1' then CONCAT('Hai effettuato una chiamata a ', Contacts.name)
				END as callmsg,
				CASE
					WHEN type = '0' then 'https://bettercallpepper.altervista.org/img/utils/in.png'
					WHEN type = '1' then 'https://bettercallpepper.altervista.org/img/utils/out.png'
				END as img,
				type, TIMESTAMPDIFF(SECOND,created_at,ended_at) as calltime FROM Calls INNER JOIN Contacts ON parID = Contacts.id WHERE eldID = :id AND isActive = 0 ORDER BY Calls.id DESC";
		$stmt = $conn->prepare($sql);
		$stmt->execute(array(':id' => $id));
		if($stmt->rowCount() > 0)
		{
			$calls = $stmt->fetchAll(PDO::FETCH_ASSOC);
			return $calls;
		}
		else
			return;	
}

function getPanelCalls($type)
{
	$conn = db_connect();
	
	if($type == "in")
		$inout = 0;
	if($type == "out")
		$inout = 1;
		
	$sql = "SELECT 
				Elderlies.name as ename, Elderlies.surname as esurname, CONCAT('https://bettercallpepper.altervista.org/img/elderlies/', Elderlies.propic) as epropic,
				Contacts.name as cname, Contacts.surname as csurname, CONCAT('https://bettercallpepper.altervista.org/img/contacts/', Contacts.propic) as cpropic,
				TIMESTAMPDIFF(SECOND,created_at,ended_at) as calltime, Calls.id, type
			FROM Contacts 
			INNER JOIN Calls 
				ON parID = Contacts.id
			INNER JOIN Elderlies 
				ON eldID = Elderlies.id 
			WHERE type = :type";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(':type' => $inout));
	if($stmt->rowCount() > 0)
	{
		$calls = $stmt->fetchAll(PDO::FETCH_ASSOC);
		return $calls;
	}
	else
		return;
}

function getApprovedCalls()
{
	$conn = db_connect();
		
	$sql = "SELECT 
				Elderlies.name as ename, Elderlies.surname as esurname, CONCAT('https://bettercallpepper.altervista.org/img/elderlies/', Elderlies.propic) as epropic,
				Contacts.name as cname, Contacts.surname as csurname, CONCAT('https://bettercallpepper.altervista.org/img/contacts/', Contacts.propic) as cpropic,
				TIMESTAMPDIFF(SECOND,created_at,ended_at) as calltime, Calls.id, type
			FROM Contacts 
			INNER JOIN Calls 
				ON parID = Contacts.id
			INNER JOIN Elderlies 
				ON eldID = Elderlies.id 
			WHERE isApproved = '1'";
	$stmt = $conn->prepare($sql);
	$stmt->execute();
	if($stmt->rowCount() > 0)
	{
		$calls = $stmt->fetchAll(PDO::FETCH_ASSOC);
		return $calls;
	}
	else
		return;
}

function getNotApprovedCalls()
{
	$conn = db_connect();
		
	$sql = "SELECT 
				Elderlies.name as ename, Elderlies.surname as esurname, CONCAT('https://bettercallpepper.altervista.org/img/elderlies/', Elderlies.propic) as epropic,
				Contacts.name as cname, Contacts.surname as csurname, CONCAT('https://bettercallpepper.altervista.org/img/contacts/', Contacts.propic) as cpropic,
				TIMESTAMPDIFF(SECOND,created_at,ended_at) as calltime, Calls.id, type
			FROM Contacts 
			INNER JOIN Calls 
				ON parID = Contacts.id
			INNER JOIN Elderlies 
				ON eldID = Elderlies.id 
			WHERE isApproved = '0'";
	$stmt = $conn->prepare($sql);
	$stmt->execute();
	if($stmt->rowCount() > 0)
	{
		$calls = $stmt->fetchAll(PDO::FETCH_ASSOC);
		return $calls;
	}
	else
		return;
}

function getNotApprovedCallsNumber()
{
	$conn = db_connect();
		
	$sql = "SELECT COUNT(*) as n FROM Calls WHERE isApproved = '0'";
	$stmt = $conn->prepare($sql);
	$stmt->execute();
	$row = $stmt->fetch(PDO::FETCH_ASSOC);
	return $row['n'];
}

function getOperatoreRequest()
{
	$conn = db_connect();
		
	$sql = "SELECT COUNT(*) as n FROM Elderlies WHERE richiesta_operatore is not null";
	$stmt = $conn->prepare($sql);
	$stmt->execute();
	$row = $stmt->fetch(PDO::FETCH_ASSOC);
	return $row['n'];
}

function getNotApprovedPepperCallsNumber()
{
	$conn = db_connect();
		
	$sql = "SELECT COUNT(*) as n FROM Elderlies WHERE pepper_impegnato != 0 and pulsante = '1'";
	$stmt = $conn->prepare($sql);
	$stmt->execute();
	$row = $stmt->fetch(PDO::FETCH_ASSOC);
	return $row['n'];
}

function addElder($name, $surname, $ddn, $city, $prov, $sex, $filename, $room)
{
	$conn = db_connect();

	$name = filter_var($name, FILTER_SANITIZE_STRING);
	$surname = filter_var($surname, FILTER_SANITIZE_STRING);
	$city = filter_var($city, FILTER_SANITIZE_STRING);
	$prov = filter_var($prov, FILTER_SANITIZE_STRING);
	$sex = filter_var($sex, FILTER_SANITIZE_NUMBER_INT);
	$room = filter_var($room, FILTER_SANITIZE_NUMBER_INT);

	$sql = "INSERT INTO Elderlies(name, surname, birth, place, prov, propic, gender, room) VALUES (:name, :surname, :ddn, :city, :prov, :propic, :gender, :room)";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':name' => $name,
    ':surname' => $surname,
    ':ddn' => $ddn,
    ':city' => $city,
    ':prov' => $prov,
    ':gender' => $sex,	
    ':propic' => $filename,
	':room' => $room	
	));
}

function editElder($name, $surname, $ddn, $city, $prov, $sex, $filename, $id, $room)
{
	$conn = db_connect();

	$name = filter_var($name, FILTER_SANITIZE_STRING);
	$surname = filter_var($surname, FILTER_SANITIZE_STRING);
	$city = filter_var($city, FILTER_SANITIZE_STRING);
	$prov = filter_var($prov, FILTER_SANITIZE_STRING);
	$sex = filter_var($sex, FILTER_SANITIZE_NUMBER_INT);
	$id = filter_var($id, FILTER_SANITIZE_NUMBER_INT);
	$room = filter_var($room, FILTER_SANITIZE_NUMBER_INT);

	$sql = "UPDATE Elderlies SET
			name = :name, 
			surname = :surname,
			birth = :ddn,
			place = :city, 
			prov = :prov, 
			propic = :propic, 
			gender = :gender,
			room = :room
			WHERE id = :id";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':name' => $name,
    ':surname' => $surname,
    ':ddn' => $ddn,
    ':city' => $city,
    ':prov' => $prov,
    ':gender' => $sex,	
    ':propic' => $filename,
	':id' => $id,
	':room' => $room
	));
	
	//if (!$stmt->execute()) {
    //print_r($stmt->errorInfo());
}

function callPepper($bedn, $status)
{
	$conn = db_connect();

	$status = filter_var($status, FILTER_SANITIZE_NUMBER_INT);
	$bedn = filter_var($bedn, FILTER_SANITIZE_NUMBER_INT);

	$sql = "UPDATE Elderlies SET
			pulsante = :status
			WHERE room = :bedn";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':status' => $status,
    ':bedn' => $bedn
	));
	
	if (!$stmt->execute())
    echo $stmt->errorInfo();
}

function editContact($name, $surname, $mail, $sex, $filename, $id)
{
	$conn = db_connect();

	$name = filter_var($name, FILTER_SANITIZE_STRING);
	$surname = filter_var($surname, FILTER_SANITIZE_STRING);
	$mail = filter_var($mail, FILTER_SANITIZE_EMAIL);
	$sex = filter_var($sex, FILTER_SANITIZE_NUMBER_INT);
	$id = filter_var($id, FILTER_SANITIZE_NUMBER_INT);

	$sql = "UPDATE Contacts SET
			name = :name, 
			surname = :surname,
			propic = :propic, 
			gender = :gender
			WHERE id = :id";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':name' => $name,
    ':surname' => $surname,
    ':gender' => $sex,	
    ':propic' => $filename,
	':id' => $id
	));
	
	$sql = "UPDATE Users SET
			mail = :mail
			WHERE actualid = :id";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':mail' => $mail,
	':id' => $id
	));
	
	//if (!$stmt->execute()) {
    //print_r($stmt->errorInfo());
}

function addContact($name, $surname, $sex, $eldid, $mail, $password, $filename)
{
	$conn = db_connect();

	$name = filter_var($name, FILTER_SANITIZE_STRING);
	$surname = filter_var($surname, FILTER_SANITIZE_STRING);
	$sex = filter_var($sex, FILTER_SANITIZE_NUMBER_INT);
	$mail = filter_var($mail, FILTER_SANITIZE_EMAIL);
	$password = filter_var($password, FILTER_SANITIZE_STRING);
	$eldid = filter_var($eldid, FILTER_SANITIZE_NUMBER_INT);

	$sql = "INSERT INTO Contacts(name, surname, propic, gender, elderID) VALUES (:name, :surname, :propic, :gender, :eldid)";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':name' => $name,
    ':surname' => $surname,
    ':gender' => $sex,	
    ':propic' => $filename,
	':eldid' => $eldid,
	));	
	
	print_r($stmt->errorInfo());
	
	$last_id = $conn->lastInsertId();
	
	$sql = "INSERT INTO Users(mail, fullname, username, password, propic, isAdmin, actualid) 
					VALUES (:mail, :fullname, :username, :password, :propic, 0, :actid)";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':mail' => $mail,
    ':fullname' => "$name $surname",
    ':username' => $name,
	':password' => $password,	
    ':propic' => $filename,
	':actid' => $last_id
	));
	
    print_r($stmt->errorInfo());
}

function mobile_header($active)
{
	
	if($active == "calls")
	{
		$cindex = "";
		$ccalls = "has-sub active";
		$cpepper = "has-sub";
		$cquestionario = "";
	}
	
	if($active == "index")
	{
		$cindex = "active";
		$ccalls = "has-sub";
		$cpepper = "has-sub";
		$cquestionario = "";
	}

	if($active == "peppercalls")
	{
		$cindex = "";
		$ccalls = "has-sub";
		$cpepper = "has-sub active";
		$cquestionario = "";
	}

	if($active == "questionario")
	{
		$cindex = "";
		$ccalls = "has-sub";
		$cpepper = "has-sub";
		$cquestionario = "active";
	}

	$napp = getNotApprovedCallsNumber();
	$nappP = getNotApprovedPepperCallsNumber();
	$failed =  getFailedPepperGoNumber();
	$operatore = getOperatoreRequest();
	
	$string = '<header class="header-mobile d-block d-lg-none">
            <div class="header-mobile__bar">
                <div class="container-fluid">
                    <div class="header-mobile-inner">
                        <a class="logo" href="index.php">
                            <img src="img/icon/logo.png" alt="PepperApp" />
                        </a>
                        <button class="hamburger hamburger--slider" type="button">
                            <span class="hamburger-box">
                                <span class="hamburger-inner"></span>
                            </span>
                        </button>
                    </div>
                </div>
            </div>
            <nav class="navbar-mobile">
                <div class="container-fluid">
                    <ul class="navbar-mobile__list list-unstyled">
					<li class="'.$cindex.'">
						<a href="index.php">
							<i class="fas fa-user-alt"></i>Anziani</a>
					</li>						
					<li class="'.$ccalls.'">
						<a class="js-arrow" href="#">';
							if($napp != 0)
								$string .= '<i class="fas fa-phone-alt"></i>Chiamate<span class="badge badge-danger pull-right">!</span></a>';
							else
								$string .= '<i class="fas fa-phone-alt"></i>Chiamate</a>';
						$string .= '<ul class="list-unstyled navbar__sub-list js-sub-list">
							<li>
								<a href="calls.php?type=in"><i class="fas fa-phone-alt"></i>Ingoing</a>
							</li>
							<li>
								<a href="calls.php?type=out"><i class="fas fa-phone"></i>Outgoing</a>
							</li>
							<li>
								<a href="calls.php?type=toapp"><i class="fas fa-ellipsis-h"></i>Da approvare';
								if($napp != 0)
									$string.= '<span class="badge badge-danger pull-right">'.$napp.'</span>';
								$string.= '</a>
							</li>
							<li>
								<a href="calls.php?type=app"><i class="fas fa-thumbs-up"></i>Approvate</a>
							</li>							
						</ul>
					</li>
					<li class="'.$cpepper.'">										
						<a class="js-arrow" href="#">';
							if($nappP != 0 || $failed !=0 || $operatore != 0)
								$string .= '<i class="fas fa-user-alt"></i>Richieste Pepper<span class="badge badge-danger pull-right">!</span></a>';
							else
								$string .= '<i class="fas fa-user-alt"></i>Richieste Pepper</a>';
						$string .= '<ul class="list-unstyled navbar__sub-list js-sub-list">
							<li>
								<a href="pepperCalls.php?type=toapp"><i class="fas fa-ellipsis-h"></i>Da approvare';
								if($nappP != 0)
									$string.= '<span class="badge badge-danger pull-right">'.$nappP.'</span>';
								$string.= '</a>
							</li>
							<li>
								<a href="pepperCalls.php?type=failed"><i class="fas fa-exclamation-triangle"></i>Fallite';
								if($failed != 0)
									$string.= '<span class="badge badge-danger pull-right">'.$failed.'</span>';
								$string.= '</a>
							</li>
							<li>
								<a href="pepperCalls.php?type=operatore"><i class="fas fa-user-alt"></i>Operatore';
								if($operatore != 0)
									$string.= '<span class="badge badge-danger pull-right">'.$operatore.'</span>';
								$string.= '</a>
							</li>
													
						</ul>		
					</li>
					<li class="'.$cquestionario.'">
						<a href="questionario.php">
						<i class="fas fa-user-alt"></i>Questionario</a>
					</li>
				</ul>
			</div>
		</nav>
	</header>';
	
	return $string;
}

function menu_sidebar($active)
{
	if($active == "calls")
	{
		$cindex = "";
		$ccalls = "has-sub active";
		$cpepper = "has-sub";
		$cquestionario = "";
	}
	
	if($active == "index")
	{
		$cindex = "active";
		$ccalls = "has-sub";
		$cpepper = "has-sub";
		$cquestionario = "";
	}

	if($active == "peppercalls")
	{
		$cindex = "";
		$ccalls = "has-sub";
		$cpepper = "has-sub active";
		$cquestionario = "";
	}

	if($active == "questionario")
	{
		$cindex = "";
		$ccalls = "has-sub";
		$cpepper = "has-sub";
		$cquestionario = "active";
	}
	
	
	$napp = getNotApprovedCallsNumber();
	$nappP = getNotApprovedPepperCallsNumber();
	$failed =  getFailedPepperGoNumber();
	$operatore = getOperatoreRequest();
	
	$string = '<aside class="menu-sidebar d-none d-lg-block">
		<div class="logo">
			<a href="#">
				<img src="img/icon/logo.png" alt="PepperApp" />
			</a>
		</div>
		<div class="menu-sidebar__content js-scrollbar1">
			<nav class="navbar-sidebar">
				<ul class="list-unstyled navbar__list">
					<li class="'.$cindex.'">
						<a href="index.php">
							<i class="fas fa-user-alt"></i>Anziani</a>
					</li>						
					<li class="'.$ccalls.'">
						<a class="js-arrow" href="#">';
							if($napp != 0)
								$string .= '<i class="fas fa-phone-alt"></i>Chiamate<span class="badge badge-danger pull-right">!</span></a>';
							else
								$string .= '<i class="fas fa-phone-alt"></i>Chiamate</a>';
						$string .= '<ul class="list-unstyled navbar__sub-list js-sub-list">
							<li>
								<a href="calls.php?type=in"><i class="fas fa-phone-alt"></i>Ingoing</a>
							</li>
							<li>
								<a href="calls.php?type=out"><i class="fas fa-phone"></i>Outgoing</a>
							</li>
							<li>
								<a href="calls.php?type=toapp"><i class="fas fa-ellipsis-h"></i>Da approvare';
								if($napp != 0)
									$string.= '<span class="badge badge-danger pull-right">'.$napp.'</span>';
								$string.= '</a>
							</li>
							<li>
								<a href="calls.php?type=app"><i class="fas fa-thumbs-up"></i>Approvate</a>
							</li>							
						</ul>
					</li>
					<li class="'.$cpepper.'">										
						<a class="js-arrow" href="#">';
							if($nappP != 0 || $failed !=0 || $operatore != 0)
								$string .= '<i class="fas fa-user-alt"></i>Richieste Pepper<span class="badge badge-danger pull-right">!</span></a>';
							else
								$string .= '<i class="fas fa-user-alt"></i>Richieste Pepper</a>';
						$string .= '<ul class="list-unstyled navbar__sub-list js-sub-list">
							<li>
								<a href="pepperCalls.php?type=toapp"><i class="fas fa-ellipsis-h"></i>Da approvare';
								if($nappP != 0)
									$string.= '<span class="badge badge-danger pull-right">'.$nappP.'</span>';
								$string.= '</a>
							</li>
							<li>
								<a href="pepperCalls.php?type=failed"><i class="fas fa-exclamation-triangle"></i>Fallite';
								if($failed != 0)
									$string.= '<span class="badge badge-danger pull-right">'.$failed.'</span>';
								$string.= '</a>
							</li>
							<li>
								<a href="pepperCalls.php?type=operatore"><i class="fas fa-user-alt"></i>Operatore';
								if($operatore != 0)
									$string.= '<span class="badge badge-danger pull-right">'.$operatore.'</span>';
								$string.= '</a>
							</li>
													
						</ul>
					
					</li>
					<li class="'.$cquestionario.'">
						<a href="questionario.php">
						<i class="fas fa-user-alt"></i>Questionario</a>
					</li>
				</ul>
			</nav>
		</div>
	</aside>';
	
	return $string;
}

function header_desktop()
{
	$userdata = getUserData();
	
	return '<header class="header-desktop">
		<div class="section__content section__content">
			<div class="container-fluid">
				<div class="header-wrap">
					<form class="form-header" action="" method="POST">
                                <input class="au-input au-input--xl" type="hidden" name="search" placeholder="Search for datas &amp; reports..." />
                            </form>
					<div class="header-button">
						<div class="account-wrap">
							<div class="account-item clearfix js-item-menu">
								<div class="image">
									<img src="img/users/'.$userdata['propic'].'" alt="Image Loading Error" />
								</div>
								<div class="content">
									<a class="js-acc-btn" href="#">'.$userdata['fullname'].'</a>
								</div>
								<div class="account-dropdown js-dropdown">
									<div class="info clearfix">
										<div class="image">
											<a href="#">
												<img src="img/users/'.$userdata['propic'].'" alt="Image Loading Error" />
											</a>
										</div>
										<div class="content">
											<h5 class="name">
												<a href="#">'.$userdata['fullname'].'</a>
											</h5>
											<span class="email">'.$userdata['mail'].'</span>
										</div>
									</div>
									<div class="account-dropdown__body">
										<div class="account-dropdown__item">
											<a href="settings.php">
												<i class="zmdi zmdi-settings"></i>Impostazioni</a>
										</div>
									</div>
									<div class="account-dropdown__footer">
										<a href="logout.php">
											<i class="zmdi zmdi-power"></i>Logout</a>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</header>';
}

function getFancyCalltime($calltime)
{
	$seconds = $calltime % 60;
    $hours = $calltime / 60;
    $minutes = $hours % 60;
    $hours = floor($hours / 60);
	if($hours < 10)
		$hours = "0$hours";
	if($minutes < 10)
		$minutes = "0$minutes";
	if($seconds < 10)
		$seconds = "0$seconds";	
    return "$hours : $minutes : $seconds";
}

function calltype($type)
{
	if($type == "in")
		return "in entrata";
	if($type == "out")
		return "in uscita";
	if($type == "toapp")
		return "da approvare";
	if($type == "app")
		return "approvate";
	return "?";
}

function setPassword($username, $password, $psw)
{
	$conn = db_connect();

	$sql = "UPDATE Users SET password = :psw WHERE username = :username AND password = :password";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':psw' => $psw,
    ':username' => $username,
    ':password' => $password
	));
}

function approve($id)
{
	$conn = db_connect();

	$sql = "UPDATE Calls SET isApproved = 1 WHERE id = :id";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':id' => $id
	));
}

function deny($id)
{
	$conn = db_connect();

	$sql = "UPDATE Calls SET isApproved = '-' WHERE id = :id";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':id' => $id
	));
}

function setUsername($username, $password, $user)
{
	$conn = db_connect();

	$sql = "UPDATE Users SET username = :user WHERE username = :username AND password = :password";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':user' => $user,
    ':username' => $username,
    ':password' => $password
	));
}

function setAvatar($username, $password, $avatar)
{
	$conn = db_connect();

	$sql = "UPDATE Users SET propic = :avatar WHERE username = :username AND password = :password";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':avatar' => $avatar,
    ':username' => $username,
    ':password' => $password
	));
}

function checkvalidpasswordchange($username, $password, $oldpsw, $newpsw, $confpsw)
{
	if($password == $oldpsw && $newpsw == $confpsw && strlen($newpsw) > 7)
    	return 1;
	else
    	return 0;
}

function alert($type, $title, $text)
{
echo '<div class="sufee-alert alert with-close alert-'.$type.' alert-dismissible fade show">
		<span class="badge badge-pill badge-'.$type.'">'.$title.'</span>
		'.$text.'
		<button type="button" class="close" data-dismiss="alert" aria-label="Close">
			<span aria-hidden="true">×</span>
		</button>
	</div>';
}

function setPepperGo($bedn, $status) //per settare go da java tramite url
{


	$conn = db_connect();

	$status = filter_var($status, FILTER_SANITIZE_NUMBER_INT);
	$bedn = filter_var($bedn, FILTER_SANITIZE_NUMBER_INT);

	$sql = "UPDATE Elderlies SET
			pepper_go = :status
			WHERE room = :bedn";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':status' => $status,
    ':bedn' => $bedn
	));
	
	if (!$stmt->execute())
    echo $stmt->errorInfo();
}

function callOperatore($room){
	$conn = db_connect();
	
	$bedn = filter_var($room, FILTER_SANITIZE_NUMBER_INT);	
	

	$sql = "UPDATE Elderlies SET
			richiesta_operatore = NOW()
			WHERE room = :bedn";

	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':bedn' => $bedn,
	));
}	

function setPepperFailed($room, $pepper_failed){
	$conn = db_connect();
	$list = getElderliesList();
	foreach( $list as $element){
		if($element['room']==$room){
			$name = $element['name'];
			$surname = $element['surname'];
			$filename = $element['filename'];
			break;
		}

	}
	
	$status = filter_var($pepper_failed, FILTER_SANITIZE_NUMBER_INT);
	$bedn = filter_var($room, FILTER_SANITIZE_NUMBER_INT);
	
	
	$sql = "INSERT INTO Pepper_call(name,surname, filename, room, type, created_at) 
	VALUES (:name,:surname,:filename,:bedn, :status, NOW())";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
	':name' => $name,
	':surname' => $surname,
	':filename' => $filename,
    ':bedn' => $bedn,
    ':status' => $status
	));
	
	/*if (!$stmt->execute()) {
          print_r($stmt->errorInfo());
         }*/

}

function setPepperImpegnato($bedn, $status) //per settare impegnato da java tramite url
{
	$conn = db_connect();

	$status = filter_var($status, FILTER_SANITIZE_NUMBER_INT);
	$bedn = filter_var($bedn, FILTER_SANITIZE_NUMBER_INT);

	$sql = "UPDATE Elderlies SET
			pepper_impegnato = :status
			WHERE room = :bedn";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':status' => $status,
    ':bedn' => $bedn
	));
	
	if (!$stmt->execute())
    echo $stmt->errorInfo();
}

function getNotApprovedPepperCalls()
{
	$conn = db_connect();
		
	$sql = "SELECT 		name as ename, 
				surname as esurname, 
				room,
				CONCAT('https://bettercallpepper.altervista.org/img/elderlies/', Elderlies.propic) as epropic,
				pepper_impegnato
				
			FROM Elderlies 
			WHERE pepper_impegnato != 0";
	$stmt = $conn->prepare($sql);
	$stmt->execute();
	if($stmt->rowCount() > 0)
	{
		$calls = $stmt->fetchAll(PDO::FETCH_ASSOC);
		return $calls;
	}
	else
		return;
}

function getQuestionarioAnswers()
{
	$conn = db_connect();
		
	$sql = "SELECT 	*	
			FROM Questionario";
	$stmt = $conn->prepare($sql);
	$stmt->execute();

	if($stmt->rowCount() > 0)
	{
		$answers = $stmt->fetchAll(PDO::FETCH_ASSOC);
		return $answers;
	}
	else
		return;
}



function getFailedPepperGo()
{
	$conn = db_connect();
		
	$sql = "SELECT 		name as ename, 
				surname as esurname, 
				room,
				CONCAT('https://bettercallpepper.altervista.org/img/elderlies/', Pepper_call.filename) as epropic,
				type,
				created_at as orario
				
			FROM Pepper_call
			WHERE type = 1";
	$stmt = $conn->prepare($sql);
	$stmt->execute();
	if($stmt->rowCount() > 0)
	{
		$calls = $stmt->fetchAll(PDO::FETCH_ASSOC);
		return $calls;
	}
	else
		return;
}

function getOperatoreRequestList()
{
	$conn = db_connect();
		
	$sql = "SELECT 		name as ename, 
				surname as esurname, 
				room,
				CONCAT('https://bettercallpepper.altervista.org/img/elderlies/', Elderlies.propic) as epropic,
				richiesta_operatore
				
			FROM Elderlies
			WHERE richiesta_operatore is not null";
	$stmt = $conn->prepare($sql);
	$stmt->execute();
	if($stmt->rowCount() > 0)
	{
		$calls = $stmt->fetchAll(PDO::FETCH_ASSOC);
		return $calls;
	}
	else
		return;
}


function getFailedPepperGoNumber()
{
	$conn = db_connect();
		
	$sql = "SELECT COUNT(*) as n FROM Pepper_call WHERE type =1 ";
	$stmt = $conn->prepare($sql);
	$stmt->execute();
	$row = $stmt->fetch(PDO::FETCH_ASSOC);
	return $row['n'];
}



function approvePepperGo($id)
{
	$conn = db_connect();

	$sql = "UPDATE Elderlies SET pepper_go = 1 WHERE room = :id";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':id' => $id
	));

	$sql = "UPDATE Elderlies SET pepper_impegnato = 0 WHERE room = :id";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':id' => $id
	));

	$sql = "UPDATE Elderlies SET pulsante = 0 WHERE room = :id";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':id' => $id
	));
}

function denyPepperGo($id)
{
	$conn = db_connect();

	$sql = "UPDATE Elderlies SET pepper_go = 2 WHERE room = :id";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':id' => $id
	));

	$sql = "UPDATE Elderlies SET pepper_impegnato = 0 WHERE room = :id";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':id' => $id
	));

	$sql = "UPDATE Elderlies SET pulsante = 0 WHERE room = :id";
	$stmt = $conn->prepare($sql);
	$stmt->execute(array(
    ':id' => $id
	));
}
