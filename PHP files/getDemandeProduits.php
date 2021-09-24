<?php
    $db =mysqli_connect("localhost","id12877554_binome","012345678","id12877554_rz");
    $idClient=$_POST['idClient'];
    
    $sql= mysqli_prepare($db,"SELECT * FROM logDemande WHERE id_Client = ? and ClientClick=0");
        mysqli_stmt_bind_param($sql,"s",$idClient);
        mysqli_stmt_execute($sql);
        mysqli_stmt_store_result($sql);
        mysqli_stmt_bind_result($sql,$idlogD,$idD,$idClient,$idVendeur,$clientClick,$VendeurConfirmer,$VendeurDecline,$cmnt,$avis);
        $i=0;
        while (mysqli_stmt_fetch($sql)) {
            $rs[] = array(
            'idD' => $idD
            );

            $i++;
        }
        for ($j=0; $j <= $i ; $j++) { 
            $sql= mysqli_prepare($db,"SELECT * FROM demande WHERE id_Demande = ?");
            mysqli_stmt_bind_param($sql,"s",$rs[$j]["idD"]);
            mysqli_stmt_execute($sql);
            mysqli_stmt_store_result($sql);
            mysqli_stmt_bind_result($sql,$idDemande,$quantite,$idC,$idVendeur,$idProduit,$idTrajet,$idPos);
            while (mysqli_stmt_fetch($sql)) {
                $rs[$j]["quantite"]=$quantite;
                $rs[$j]["idProduit"]=$idProduit;
                $rs[$j]["idVendeur"]=$idVendeur;
            }
            $sql= mysqli_prepare($db,"SELECT * FROM produit WHERE id_Produit = ?");
            mysqli_stmt_bind_param($sql,"s",$rs[$j]["idProduit"]);
            mysqli_stmt_execute($sql);
            mysqli_stmt_store_result($sql);
            mysqli_stmt_bind_result($sql,$idP,$nom,$categ,$prix,$img);
            while (mysqli_stmt_fetch($sql)) {
                $rs[$j]["nomProduit"]=$nom;
                $rs[$j]["categ"]=$categ;
                $rs[$j]["prix"]=$prix;
                $rs[$j]["img"]=$img;
            }
            $sql= mysqli_prepare($db,"SELECT count(*) FROM promo WHERE id_produit = ?");
            mysqli_stmt_bind_param($sql,"s",$rs[$j]["idProduit"]);
            mysqli_stmt_execute($sql);
            mysqli_stmt_store_result($sql);
            mysqli_stmt_bind_result($sql,$count);
            mysqli_stmt_fetch($sql);
            $n=$count;
            if($n==1){
                $sql= mysqli_prepare($db,"SELECT percent FROM promo WHERE id_produit = ?");
                mysqli_stmt_bind_param($sql,"s",$rs[$j]["idProduit"]);
                mysqli_stmt_execute($sql);
                mysqli_stmt_store_result($sql);
                mysqli_stmt_bind_result($sql,$promo);
                mysqli_stmt_fetch($sql);
                $rs[$j]["promo"]=$promo;
            }else{
                $rs[$j]["promo"]=0;
            }
        }

    echo json_encode($rs);
?>