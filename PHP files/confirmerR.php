<?php
 $db= new PDO("mysql:host=localhost;dbname=id12877554_rz","id12877554_binome","012345678");
	$results["error"]=false;
	$results["messages"]=[];
	/*
	$_POST["idD"]=64;
    $_POST["cmnt"]="";
    $_POST["avis"]="";
	*/
	if (isset($_POST)) {
		if ( isset($_POST["idD"]) && isset($_POST["cmnt"]) && isset($_POST["avis"])) {

            $idD=$_POST["idD"];
            $cmnt=$_POST["cmnt"];
            $avis=$_POST["avis"];

			$sql=$db->prepare("UPDATE logReserve SET clientConfirmer = true, commentR=:cmnt, avisR=:avis WHERE id_Reserve =:idD");
			$sql->execute([":cmnt" => $cmnt, ":avis" => $avis, ":idD" => $idD]);
			if (!$sql) {
				$results["error"]=true;
				$results["messages"]="Impossible de confirmer";
			}
	   }
	}
	echo json_encode($results);
?>