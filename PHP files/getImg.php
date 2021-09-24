<?php
/*
$_POST['mail']="x@g.com";
$_POST['pass']="12345678";
*/

 $db= new PDO("mysql:host=localhost;dbname=id12877554_rz","id12877554_binome","012345678");
	$results["error"]=false;
	$results["messages"]=[];
if (isset($_GET)) {
		if (!empty($_GET["idU"])){
            
            $idU=$_GET['idU'];
            
            $sql= $db->prepare("SELECT * FROM utilisateur WHERE id_Utilisateur = :idU");
            $sql->execute([":idU" => $idU]);
            $row = $sql->fetch(PDO::FETCH_OBJ);
            
            if($row){
                $results["error"]=false;
                $results["image"]=$row->profileImg;
            }else{
                $results["error"]=true;
	            $results["messages"]="erreur";
            }
            
        }else{
            $results["error"]=true;
			$results["messages"]="erreur";
        }
    echo json_encode($results);
}
?>