<?php
 $db= new PDO("mysql:host=localhost;dbname=id12877554_rz","id12877554_binome","012345678");
	$results["error"]=false;
	$results["messages"]=[];
	//$_POST["idR"]=1;
	if (isset($_POST)) {
		if ( isset($_POST["idR"]) || isset($_POST["cmnt"]) || isset($_POST["avis"])) {

            $idR=$_POST["idR"];
            $cmnt=$_POST["cmnt"];
            $avis=$_POST["avis"];

			$sql=$db->prepare("UPDATE logReserve SET clientDecline = true, commentR=:cmnt, avisR=:avis WHERE id_Reserve =:idR");
			$sql->execute([":cmnt" => $cmnt, ":avis" => $avis, ":idR" => $idR]);
			if (!$sql) {
				$results["error"]=true;
				$results["messages"]="Impossible de Rejeter";
				
			}else{
			    
    			$sql=$db->prepare("DELETE FROM reservation WHERE id_Reservation =:idR");
    			$sql->execute([":idR" => $idR]);
			}

		    echo json_encode($results);
	    }
	}
?>