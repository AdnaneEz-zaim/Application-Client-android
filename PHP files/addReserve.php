<?php
require("/storage/ssd3/554/12877554/public_html/Test/getNearest.php");
$db= new PDO("mysql:host=localhost;dbname=id12877554_rz","id12877554_binome","012345678");
	$results["error"]=false;
	$results["messages"]=[];
	if (isset($_POST)) {
		if ( !empty($_POST["quantite"]) && !empty($_POST["idC"]) && !empty($_POST["idP"]) && !empty($_POST["lng"]) && !empty($_POST["lat"])) {

            $quant=$_POST["quantite"];
            $idC=$_POST["idC"];
            $idP=$_POST["idP"];
            $lat=$_POST["lat"];
            $lng=$_POST["lng"];
            $date=$_POST["date"];
            
            $sql=$db->prepare("INSERT INTO `position` (`id_pos`,`x`,`y`) VALUES(NULL,:x,:y)");
            $sql->execute([":x" => $lat,":y" => $lng]);
            if (!$sql) {
                $results["error"]=true;
				$results["messages"]="Position non valide";
            }else{
                
                $sql=$db->prepare("SELECT MAX(id_pos) id FROM position;");
			    $sql->execute();
    			$idPosition=$sql->fetch();
    			
    			$sql=$db->prepare("SELECT DATE_FORMAT(CURDATE(), '%Y-%m-%d') today;");
			    $sql->execute();
    			$day=$sql->fetch();
    			
    			$dayOfWeek = strtolower(date("l", strtotime($day["today"])));
                $trajet=getRightTraject($db,$lat,$lng);
                
                $sql=$db->prepare("SELECT id_Vendeur FROM vendeurPlan WHERE id_Trajet=? and day=?;");
			    $sql->execute([$trajet,$dayOfWeek]);
    			$idVendeur=$sql->fetch();
    			
    			$sql=$db->prepare("INSERT INTO reservation(id_Reservation,quantite_dem,id_Client,id_Produit,id_Vendeur,id_trajet,id_position,date_reserve) VALUES(NULL,:quant,:idC,:idP,:idV,:idT,:idPos,STR_TO_DATE(:date,'%d,%m,%Y'));");
    			$sql->execute([":quant" => $quant,":idC" => $idC,":idV" => $idVendeur["id_Vendeur"],":idP" => $idP,":idT" => $trajet,":idPos" => $idPosition["id"],":date"=>$date]);
    			if (!$sql) {
    				$results["error"]=true;
    				$results["messages"]="Impossible de ajouter une demande";
    			}else{
    			    $sql=$db->prepare("SELECT MAX(id_Reservation) FROM reservation;");
    			    $sql->execute();
    			    $idR=$sql->fetch();
    			    
    			    $sql=$db->prepare("INSERT INTO logReserve(id_logR,id_Reserve,id_Client,id_Vendeur) VALUES(NULL,:idD,:idC,:idV);");
    			    $sql->execute([":idD" => $idR["MAX(id_Reservation)"],"idC"=>$idC,"idV"=>$idVendeur["id_Vendeur"]]);
    			}
    		    
            }
	   }
	}
	echo json_encode($results);
?>
