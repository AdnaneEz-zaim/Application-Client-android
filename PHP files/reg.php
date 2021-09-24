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

            $pass=password_hash($pass,PASSWORD_BCRYPT);
			$sql = $db->prepare("INSERT INTO utilisateur(id_Utilisateur,email_u,nom_u,prenom_u,passwd_ul,tele_u,id_pos,profileImg) VALUES(NULL,:email,:lname,:fname,:pass,:tele,:xx,:img)");
			$sql->execute([":fname" => $fname ,":lname" => $lname,":email" => $email ,":tele" => $tele,":pass" => $pass,"xx" => $xx,"img"=>$defaultImg]);
			if (!$sql) {
				$results["error"]=true;
				$results["messages"]="erreur lors de l'inscreption";
			}else{
				$sql=$db->prepare("SELECT id_Utilisateur FROM utilisateur WHERE email_u = :email ;");
				$sql->execute([":email" => $email]);
				$id=$sql->fetch();
				$sql = $db->prepare("INSERT INTO client(id_Client,adresse_c,id_Utilisateur) VALUES(NULL,:adrr,:id_Utilisateur)");
				$sql->execute([":adrr" => $adrr ,":id_Utilisateur"=>$id["id_Utilisateur"]]);
				if (!$sql) {
					$results["error"]=true;
					$results["messages"]="erreur lors de l'inscreption";
				}
			}
		}else{
			$results["error"]=true;
			$results["messages"]="Veuillez remplir tous les champs";
		}

		echo json_encode($results);
	}
?>