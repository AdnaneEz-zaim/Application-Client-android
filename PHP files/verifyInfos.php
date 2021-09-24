<?php
$xx=1000;
/*
$_POST['fname']="xxx";

$_POST['lname']="xxx";
$_POST['adrr']="xxx";

$_POST['email']="iliass.dahman@usmba.ac.ma";
$_POST['tele']="06xxx";
$_POST['pass']="xxxxxxxx";
$_POST['conf']="xxxxxxxx";
*/

 $db= new PDO("mysql:host=localhost;dbname=id12877554_rz","id12877554_binome","012345678");
	$results["error"]=false;
	$results["messages"]=[];
	if (isset($_POST)) {
		if ( !empty($_POST["fname"]) && !empty($_POST["lname"]) && !empty($_POST["adrr"]) && !empty($_POST["email"]) && !empty($_POST["tele"]) && !empty($_POST["pass"]) && !empty($_POST["conf"])) {

			$fname=$_POST['fname'];
			$lname=$_POST['lname'];
			$adrr=$_POST['adrr'];
			$email=$_POST['email'];
			$tele=$_POST['tele'];
			$pass=$_POST['pass'];
			$conf=$_POST['conf'];
			$defaultImg="https://rzbusinessma.000webhostapp.com/profileImgs/Clients/default.png";

			//verifier fname-
			if (strlen($lname)<2 || !preg_match("/^[a-zA-Z0-9 _-]+$/", $lname) || strlen($lname)> 20) {
				$results["error"]=true;
				$results["messages"]["lname"]="le nom est incorrect utiliser a-z, A-Z et/ou 0-9";
			}else{
				$id=$db->prepare("SELECT id_Utilisateur FROM utilisateur WHERE nom_u = :nom_u ;");
				$id->execute([":nom_u" => $lname]);
				$id=$id->fetch();
				if ($id) {
					$results["error"]=true;
					$results["messages"]["lname"]="le nom est exist";
				}
			}

			if (strlen($fname)<2 || !preg_match("/^[a-zA-Z0-9 _-]+$/", $fname) || strlen($lname)> 20) {
				$results["error"]=true;
				$results["messages"]="le prenom est incorrect utiliser a-z, A-Z et/ou 0-9";
			}

			if (!filter_var($email,FILTER_VALIDATE_EMAIL)) {
				$results["error"]=true;
				$results["messages"]["email"]="email non valide";
			}else{
				
				$id=$db->prepare("SELECT id_Utilisateur FROM utilisateur WHERE email_u = :email_u ;");
				$id->execute([":email_u" => $email]);
				$id=$id->fetch();
				if ($id) {
					$results["error"]=true;
					$results["messages"]["email"]="email est exist";
				}
			}

			//for telephone
				
				$id=$db->prepare("SELECT id_Utilisateur FROM utilisateur WHERE tele_u = :tele_u ;");
				$id->execute([":tele_u" => $tele]);
				$id=$id->fetch();
				if ($id) {
					$results["error"]=true;
					$results["messages"]["tele"]="tele est exist";
				}


			if (strlen($pass)<8 || strlen($pass)>30) {
				$results["error"]=true;
				$results["messages"]["pass"]="mot de pass incorrect, le mot de pass doit être entre 8 et 30 chifres";
			}
			if ($pass != $conf) {
				$results["error"]=true;
				$results["messages"]["conf"]="le mot de pass n'est pas confirmé";
			}
		}else{
			$results["error"]=true;
			$results["messages"]="Veuillez remplir tous les champs";
		}

		echo json_encode($results);
	}
?>