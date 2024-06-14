<?php
$img = $_REQUEST['img'];

$uri = substr($img,strpos($img,",")+1);
$encodedData = str_replace(' ','+',$uri);
$decodedData = base64_decode($encodedData);

file_put_contents("login.png", $decodedData);

$image = imagecreatefrompng("login.png");
//$image = imagecrop($image, ['x' => 225, 'y' => 320, 'width' => 90, 'height' => 160]);
$image = imagecrop($image, ['x' => 230, 'y' => 140, 'width' => 180, 'height' => 320]);
$image = imagescale ($image, 90 , 160);

imagepgm($image, "login.pgm");

function imagepgm($image, $filename = null)
    {
        $pgm = "P5\n".imagesx($image)." ".imagesy($image)."\n255\n";
        for($y = 0; $y < imagesy($image); $y++)
        {
            for($x = 0; $x < imagesx($image); $x++)
            {
                $colors = imagecolorsforindex($image, imagecolorat($image, $x, $y));
                $pgm .= chr(0.3 * $colors["red"] + 0.59 * $colors["green"] + 0.11 * $colors["blue"]);
            }
        }
        if($filename != null)
        {
            $fp = fopen($filename, "w");
            fwrite($fp, $pgm);
            fclose($fp);
        }
        else
        {
            return $pgm;
        }
    }
?>