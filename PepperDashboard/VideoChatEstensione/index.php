<?php
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST, GET, OPTIONS');
?>

<html>

    <head>
        <title>Better Call Pepper</title>
        <link rel="stylesheet" href="styles.css">
    </head>	
		<h1 class="maintitle" id="maintitle">Better Call Pepper</h1>
		<button class="downtitle1" onClick="document.location.href='http://exitme';".>Chiudi</button>
		<button class="downtitle2" onClick="getVitalParam()".>Parametri Vitali</button> 
		<meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
		
    <body>		
        <p id="notification" hidden></p>
        <div class="meet-area">
            <!-- Remote Video Element-->
            <video id="remote-video"></video>
			
			
            <!-- Local Video Element-->
            <video id="local-video"></video>
			<audio id="audio-video"></audio>
        </div>
    </body>
	
    
	<script src="https://unpkg.com/peerjs@1.3.1/dist/peerjs.min.js"></script>
	<script type="text/javascript" src="https://www.webrtc-experiment.com/RecordRTC.js"> </script>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"> </script>
	<!--<script type="text/javascript" src="js/jquery.ajax-cross-origin.min.js"></script>-->
	<!--<script type='text/javascript' src="https://cdnjs.cloudflare.com/ajax/libs/jquery-ajaxtransport-xdomainrequest/1.0.1/jquery.xdomainrequest.min.js"></script>-->
	<script type="text/javascript">
		
	if(getq("mode") == "j"){
        joinRoom();
	}
    else
       	if(getq("mode") == "c"){
			createRoom();
		}	
    	else
           	document.write('Error');
		
	var room_id;
	//var peer_id;
	var is_pepper;
	var getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia;
	var local_stream;
	var peer;

	if(getq("pepper") == 1)
		is_pepper = 1;
	else is_pepper = 0;
	
	function getq(name) {
		let params = new URLSearchParams(window.location.search);
		return params.get(name);
	}

	function getq(name) {
		let params = new URLSearchParams(window.location.search);
		return params.get(name);
	}

	function executeAsync(func) {

		setTimeout(func, 3000);
    
	}	
	
	function createRoom(){
	
		console.log("Creating Room");
		let room = getq("room");
		alert(room);
		document.getElementById("maintitle").innerHTML = "Better Call Pepper  " //+ "room: " + room//;
		if(room == " " || room == "")   {
			alert("Please enter room number")
			return;
		}
		room_id = room;
		peer = new Peer(room_id)
		peer.on('open', (id)=>{
			console.log("Peer Connected with ID: ", id)
			getUserMedia({video: true, audio: true}, (stream)=>{
				local_stream = stream;
				setLocalStream(local_stream)
			},(err)=>{
				console.log(err)
			})
			//notify("Waiting for peer to join.")
		})
		peer.on('call',(call)=>{
			call.answer(local_stream);
			call.on('stream',(stream)=>{
				setRemoteStream(stream)
			})
		})

		console.log("end creating room")
			
	}
    

	function setLocalStream(stream){
		
		let video = document.getElementById("local-video");
		video.srcObject = stream;
		video.muted = true;
		video.play();
	}


	function setRemoteStream(stream){
	   
	   let video = document.getElementById("remote-video");
	   video.srcObject = stream;
	   video.play();
   }

   var fileFullName;

   function recLocalStream(){
			navigator.mediaDevices.getUserMedia({
				video: true,
				audio: true
			}).then(async function(stream) {
    			let recorder = RecordRTC(stream, {
        		type: 'video',
				mimeType: 'video/webm',
				frameRate: 30,
				canvas: {
        			width: 640,
         			height: 480
    			}
    			});
				recorder.startRecording();
				const sleep = m => new Promise(r => setTimeout(r, m));
				await sleep(2000);
				recorder.stopRecording( function() {
				let blob = recorder.getBlob();
				//invokeSaveAsDialog(blob, "local-video.webm");
					if(blob==null)
						console.log("Null");

					//var myfile = prova(blob, "video.webm");	
					//var myfile = blobToFile(blob, "video.webm");
					let nameFile = room_id+"_video.webm";
					var myfile = new File([blob], nameFile);

					postRequestToService(myfile);


					console.log(myfile);
					console.log(fileFullName);
					/*const url = 'https://bettercallpepper.altervista.org/VideoChatEstensione/upload.php';
					let urlVideo = 'https://bettercallpepper.altervista.org/VideoChatEstensione/uploads/'+ nameFile;
				
					const formData = new FormData();
					formData.append('files[]', myfile);
					formData.append('userid', 'id');
					formData.append('password', 'pw');
					
					fetch(url, {
						method: 'POST',
						body: formData
					}).then(response => {
						return response.text();
					}).then(data => {
						console.log(data);
					});*/
				});
		    });
   }


   function postRequestToService(myfile){
	   var ip_json = $.ajax({
		   url: 'https://api.ipify.org?format=json',
		   async: false
	   }).responseText;

	   console.log(JSON.parse(ip_json).ip);

	   //release camera
	   var fd = new FormData();
	   fd.append('user','simone');
	   fd.append('pass','simone2021');
	   fd.append('userid',JSON.parse(ip_json).ip);
	   fd.append('file',myfile);

	   /*var contentType ="application/x-www-form-urlencoded; charset=utf-8";
 
	   if(window.XDomainRequest) //for IE8,IE9
	 		contentType = "text/plain";*/

	   // crossOrigin: true,
	   // contentType: false
	   //crossDomain: true,
	   //dataType: 'jsonp',
	   //'Access-Control-Allow-Origin':'*',

	   $.ajax({
		   type: 'POST',
		   url: 'https://vs1.herovision.it:8000/vitals/',
		   data: fd,
		   processData: false,
		   secure: true,
		   headers:{
				'Access-Control-Allow-Origin':'*',
				'Access-Control-Request-Method': 'POST'
		   },
		   contentType : false
	   }).done(function(data){
			console.log(data.json());

		   /*var dati = JSON.parse(data)
		   var bpm = dati.BPM;
		   var rpm = dati.RPM;
		   var spo2 = dati.SPO2;
		   console.log("ho ricevuto bmp: " + bpm + " rpm: " + rpm +" spo2: " + spo2);*/
	   });

	   

		/*			let url = "www.prova.it"; //url del servizio

					const formData = new FormData();
					formData.append('files[]', myfile);
					
					fetch(url, {
						method: 'POST',
						body: formData
					}).then(response => {
						return response.text();
					}).then(data => {
						console.log(data);
					}); */

					
	
		//send url to video
		//receive json
		//print results
   }





   /*function prova(file,fileName){
		if (!file) {
			throw 'Blob object is required.';
		}

		if (!file.type) {
			try {
				file.type = 'video/webm';
			} catch (e) {}
		}
		file = file.slice(0, file.size, "video/webm");
	

		var fileExtension = (file.type || 'video/webm').split('/')[1];
		if (fileExtension.indexOf(';') !== -1) {
			// extended mimetype, e.g. 'video/webm;codecs=vp8,opus'
			fileExtension = fileExtension.split(';')[0];
		}
		if (fileName && fileName.indexOf('.') !== -1) {
			var splitted = fileName.split('.');
			fileName = splitted[0];
			fileExtension = splitted[1];
		}

		fileFullName = (fileName || (Math.round(Math.random() * 9999999999) + 888888888)) + '.' + fileExtension;

		file.name = fileFullName;
		return file;
   }

   function blobToFile(theBlob, fileName){
    //A Blob() is almost a File() - it's just missing the two properties below which we will add
	theBlob = theBlob.slice(0, theBlob.size, "video/webm");
    theBlob.lastModifiedDate = new Date();
    theBlob.name = fileName;
	theBlob.type = "video/webm";
    return theBlob;
}
*/

	function getVitalParam(){
	   
		if(is_pepper == 1){
			console.log("Is Pepper");
			recLocalStream();		
		}
		else if(is_pepper == 0){
			console.log("not pepper")
			connect(idToConnect)
		}

	}


/*
	function notify(msg){
		let notification = document.getElementById("notification")
		notification.innerHTML = msg
		notification.hidden = false
		setTimeout(()=>{
			notification.hidden = true;
		}, 3000)
	}
*/
function joinRoom(){

		console.log("Joining Room")
		let room = getq("room")
		if(room == " " || room == "")   {
			alert("Please enter room number")
			return;
		}
		room_id = room;
		let join_id = "_join"
		document.getElementById("maintitle").innerHTML = "Better Call Pepper - " + room;
		peer = new Peer(room_id+join_id)
		peer.on('open', (id)=>{
			console.log("Connected with Id: "+id)
			getUserMedia({video: true, audio: true}, (stream)=>{
				local_stream = stream;
				setLocalStream(local_stream)
				//notify("Joining peer")
				let call = peer.call(room_id, stream)
				call.on('stream', (stream)=>{
					setRemoteStream(stream);
				})
			}, (err)=>{
				console.log(err)
			})
		})
		
	}


let idmode;
var idToConnect;
if(getq("mode") == "j"){
	idToConnect = getq("room");
	idmode = getq("room")+"_join";
}
else{ 
	idToConnect = getq("room")+"_join";
	idmode = getq("room");
}

console.log("my peer id is:" + peer.id);

// Print PeerID to the Console
let peer_id;


peer.on('open', function(idmode) {
	
	peer_id = idmode;
	console.log('Your peer ID: ' + idmode);

});

// Listen for an incoming connection
peer.on('connection', function(conn) {
	conn.on('data', function(data) {
		// Log received data
		console.log(data);
		if(data == "parametri vitali"){
			recLocalStream();
		}
	});
});

// Connect to a peer
var conn;
function connect(peerID) {
	conn = peer.connect(peerID);

	// 'open' is called on successful connection to PeerServer
	conn.on('open', function() {

		// Send a hello message to a peer            
		conn.send(peer_id + " has connected with you");
		conn.send("parametri vitali");

		// Inform client of connection
		console.log("You have connected with: " + peerID);
	});
}

// Send a message to a Peer you have connected with
function send(msg) {
	if (conn != undefined)
		conn.send(msg);
}




				//let file = new File(blob, "local-video.webm");
				//invokeSaveAsDialog(blob, "local-video.webm");
				//let myFile = new File(blob, "local-video.webm", {type: "video/webm"});



				/*const params = {
					data: "ciao"
				};
				const options = {
					method: 'POST',
					body: JSON.stringify( params )  
				};
				console.log(options.body);
				fetch( 'https://www.bettercallpepper.altervista.org/provaData/try.php', options )
					.then( response => response.json() )
					.then( response => {
						console.log(response);
						// Do something with response.
					} );*/


			/*	var http = new XMLHttpRequest();
				let url = "https://www.bettercallpepper.altervista.org/provaData/try.php"
				http.open("POST", url, true);
				http.setRequestHeader("Content-type", "application/x-www-form-urlencoded  ");
				http.onreadystatechange = function() {
					if (this.readyState == 4 && this.status == 200) {
						console.log(this.responseText);
					}
				};
				http.send("data="+blob);



/*
				var oReq = new XMLHttpRequest();
				let url = "https://www.bettercallpepper.altervista.org/provaData/try.php"
				oReq.open("POST", url, true);
				oReq.onload = function (oEvent) {
				// Uploaded.
				};

				

				oReq.onreadystatechange = function() {//Call a function when the state changes.
					if(oReq.readyState == 4 && oReq.status == 200) {
						alert(oReq.responseText);
					}
					//alert(oReq.responseText);
				}

				oReq.send('data=ciao');
*/


				//var blob2 = new Blob(['abc123'], {type: 'video', mimeType: 'video/webm'});

				//oReq.send('data='+blob);

				/*qui devo inviare i dati con la richiesta post*/

   	</script>

</html>