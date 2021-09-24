<?php
 $db= new PDO("mysql:host=localhost;dbname=id12877554_rz","id12877554_binome","012345678");
	$results["error"]=false;
	$results["messages"]=[];
	if (isset($_POST)) {
		if ( isset($_POST["idD"]) && isset($_POST["cmnt"]) && isset($_POST["avis"])) {

            $idD=$_POST["idD"];
            $cmnt=$_POST["cmnt"];
            $avis=$_POST["avis"];

			$sql=$db->prepare("UPDATE logDemande SET clientClick = true, commentD=:cmnt, avisD=:avis WHERE id_Demande =:idD");
			$sql->execute([":cmnt" => $cmnt, ":avis" => $avis, ":idD" => $idD]);
			if (!$sql) {
				$results["error"]=true;
				$results["messages"]="Impossible de confirmer";
			}

		    echo json_encode($results);
	    }
	}
?>