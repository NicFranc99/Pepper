<?php
header("Access-Control-Allow-Origin: *");
  //lets assume you are uploading an .jpg image
 /*$filename = 'file.webm';//<-- your filename and extension
 $filecontent = $_POST["data"];
 $filecontent.to
 file_put_contents($filename, $filecontent);
 echo readfile($filename);*/
 //echo blob($_POST["data"]). "try";
 
 //file_put_contents($filename, $_POST["data"]);
 //echo readfile($filename) . "try";

  //echo htmlspecialchars($_POST["data"]) . " try";
  $blob = $_POST["data"];


  $myfile = fopen("newfile.webm", "w") or die("Unable to open file!");
  //$txt = "John Doe\n";
  fwrite($myfile, $blob);
  fclose($myfile);

  echo readfile($myfile);
 
  echo $blob
 
 /* $stringa = $_FILE[];
  $array = Array (
    "0" => Array (
        "ricevuto" => $stringa,
    )
  );
  $json = json_encode($array);
  $filename = "myfile.json";
  file_put_contents("myfile.json", $json); 
  echo readfile($filename);
  //now you have image.jpg
  //echo 'Hello ' . htmlspecialchars($_POST["data"]) . '!';*/
?>