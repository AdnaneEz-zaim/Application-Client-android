<?php
    $db =mysqli_connect("localhost","id12877554_binome","012345678","id12877554_rz");
    $idClient=$_POST['idClient'];
    
    $sql= mysqli_prepare($db,"SELECT * FROM logReserve WHERE id_Client = ? and ClientConfirmer=0 and ClientDecline=0");
        mysqli_stmt_bind_param($sql,"s",$idClient);
        mysqli_stmt_execute($sql);
        mysqli_stmt_store_result($sql);
        mysqli_stmt_bind_result($sql,$idlogR,$idD,$idClient,$idVendeur,$clientConfirmer,$clientDecline,$VendeurConfirmer,$VendeurDecline,$cmnt,$avis);
        $i=0;
        while (mysqli_stmt_fetch($sql)) {
            $rs[] = array(
            'idD' => $idD
            );

            $i++;
        }
        for ($j=0; $j <= $i ; $j++) { 
            $sql= mysqli_prepare($db,"SELECT * FROM reservation WHERE id_Reservation = ?");
            mysqli_stmt_bind_param($sql,"s",$rs[$j]["idD"]);
            mysqli_stmt_execute($sql);
            mysqli_stmt_store_result($sql);
            mysqli_stmt_bind_result($sql,$idReserve,$quantite,$idC,$idProduit,$idVendeur,$idTrajet,$idPos,$date);
            while (mysqli_stmt_fetch($sql)) {
                $rs[$j]["quantite"]=$quantite;
                $rs[$j]["idProduit"]=$idProduit;
                $rs[$j]["idVendeur"]=$idVendeur;
                $rs[$j]["date"]=$date;
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
        }
    echo json_encode($rs);
?>