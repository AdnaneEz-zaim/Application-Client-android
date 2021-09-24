<?php
/*
$_POST['mail']="x@g.com";
$_POST['pass']="12345678";
*/

 $db= new PDO("mysql:host=localhost;dbname=id12877554_rz","id12877554_binome","012345678");
	$results["error"]=false;
	$results["messages"]=[];
if (isset($_POST)) {
		if (!empty($_POST["mail"]) && !empty($_POST["pass"])){
            
            $mail=$_POST['mail'];
			$pass=$_POST['pass'];
            
            $sql= $db->prepare("SELECT * FROM utilisateur WHERE email_u = :email");
            $sql->execute([":email" => $mail]);
            $row = $sql->fetch(PDO::FETCH_OBJ);
            
            if($row){
                if(password_verify($pass,$row->passwd_ul)){

                    $sql= $db->prepare("SELECT * FROM client WHERE id_Utilisateur = :id");
                    $sql->execute([":id" => $row->id_Utilisateur]);
                    $row1 = $sql->fetch(PDO::FETCH_OBJ);
                    
                    $sql= $db->prepare("SELECT * FROM position WHERE id_pos = :id");
                    $sql->execute([":id" => $row->id_pos]);
                    $row2 = $sql->fetch(PDO::FETCH_OBJ);


                    $results["error"]=false;
                    $results["idU"]=$row->id_Utilisateur;
                    $results["mail"]=$row->email_u;
                    $results["nom"]=$row->nom_u;
                    $results["prenom"]=$row->prenom_u;
                    $results["tele"]=$row->tele_u;
                    $results["profileImg"]=$row->profileImg;

                    $results["x"]=$row2->x;
                    $results["y"]=$row2->y;

                    $results["idC"]=$row1->id_Client;
                    $results["adrr"]=$row1->adresse_c;
                }else{
                    $results["error"]=true;
	                $results["messages"]="email ou mot de pass incorrect";
                }
            }else{
                 $results["error"]=true;
	             $results["messages"]="email ou mot de pass incorrect";
            }
            
        }else{
            $results["error"]=true;
			$results["messages"]="Veuillez remplir tous les champs";
        }
    echo json_encode($results);
}
?>