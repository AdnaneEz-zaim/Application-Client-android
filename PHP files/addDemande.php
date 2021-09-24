<?php
    require("/storage/ssd3/554/12877554/public_html/Test/getNearest.php");
    $db= new PDO("mysql:host=localhost;dbname=id12877554_rz","id12877554_binome","012345678");
	$results["error"]=false;
	$results["messages"]=[];
	/*$_POST["quantite"]="4";
    $_POST["idC"]="6";
    $_POST["idP"]="10";
    $_POST["lat"]="34.024554";
    $_POST["lng"]="-5.009895";*/
	if (isset($_POST)) {
		if ( !empty($_POST["quantite"]) && !empty($_POST["idC"]) && !empty($_POST["idP"]) && !empty($_POST["lng"]) && !empty($_POST["lat"])) {
            $quant=$_POST["quantite"];
            $idC=$_POST["idC"];
            $idP=$_POST["idP"];
            $lat=$_POST["lat"];
            $lng=$_POST["lng"];
            
            $sql=$db->prepare("INSERT INTO `position` (`id_pos`,`x`,`y`) VALUES(NULL,:x,:y)");
            $sql->execute([":x" => $lat,":y" => $lng]);
            if (!$sql) {
                $results["error"]=true;
				$results["messages"]="Position non valide";
            }else{
                $sql=$db->prepare("SELECT MAX(id_pos) id FROM position;");
			    $sql->execute();
    			$idPosition=$sql->fetch();
    			
    			/*
    			$sql=$db->prepare("SELECT DATE_FORMAT(CURDATE(), '%Y-%m-%d') today;");
			    $sql->execute();
    			$date=$sql->fetch();
    			
    			$dayOfWeek = strtolower(date("l", strtotime($date["today"])));
    			*/
    			$datee=getDate();
                $days=["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"];
                $dayOfWeek = strtolower($days[$datee["wday"]]);
                
                $trajet=getRightTraject($db,$lat,$lng);
                
                $sql=$db->prepare("SELECT id_Vendeur FROM vendeurPlan WHERE id_Trajet=? and day=?;");
			    $sql->execute([$trajet,$dayOfWeek]);
    			$idVendeur=$sql->fetch();
                
                $sql=$db->prepare("INSERT INTO demande(id_Demande,quantite_res,id_Client,id_Vendeur,id_Produit,id_trajet,id_position) VALUES(NULL,:quant,:idC,:idV,:idP,:idT,:idPos);");
    			$sql->execute([":quant" => $quant,":idC" => $idC,":idV" => $idVendeur["id_Vendeur"],":idP" => $idP,":idT" => $trajet,":idPos" => $idPosition["id"]]);
    			if (!$sql) {
    				$results["error"]=true;
    				$results["messages"]="Impossible de ajouter une demande";
    			}else{
    			    $sql=$db->prepare("SELECT MAX(id_Demande) FROM demande;");
    			    $sql->execute();
    			    $idD=$sql->fetch();
    			    
    			    $sql=$db->prepare("INSERT INTO logDemande(id_logD,id_Demande,id_Client,id_Vendeur) VALUES(NULL,:idD,:idC,:idV);");
    			    $sql->execute([":idD" => $idD["MAX(id_Demande)"],"idC"=>$idC,"idV"=>$idVendeur["id_Vendeur"]]);
    			}
            }
	        echo json_encode($results);
	    }
	}
?>
