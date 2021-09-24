<?php
    $db =mysqli_connect("localhost","id12877554_binome","012345678","id12877554_rz");
    $mail=$_POST['mail'];
    
    $rs = array();

    $sql= mysqli_prepare($db,"SELECT * FROM utilisateur WHERE email_u = ?");
    mysqli_stmt_bind_param($sql,"s",$mail);
    mysqli_stmt_execute($sql);
    mysqli_stmt_store_result($sql);
    mysqli_stmt_bind_result($sql,$id,$mail,$nom,$prenom,$pass,$tele,$idPos,$profileimg);

    while (mysqli_stmt_fetch($sql)) {
        $rs["id"]=$id;
        $rs["mail"]=$mail;
        $rs["nom"]=$nom;
        $rs["prenom"]=$prenom;
        $rs["tele"]=$tele;
        $rs["idPos"]=$idPos;
        $rs["profileImg"]=$profileimg;
    }

    $sql= mysqli_prepare($db,"SELECT * FROM position WHERE id_pos = ?");
    mysqli_stmt_bind_param($sql,"s",$rs["idPos"]);
    mysqli_stmt_execute($sql);
    mysqli_stmt_store_result($sql);
    mysqli_stmt_bind_result($sql,$idPos,$x,$y);     

    while (mysqli_stmt_fetch($sql)) {
        $rs["x"]=$x;
        $rs["y"]=$y;
    }

    $sql= mysqli_prepare($db,"SELECT * FROM client WHERE id_Utilisateur = ?");
    mysqli_stmt_bind_param($sql,"s",$rs["id"]);
    mysqli_stmt_execute($sql);
    mysqli_stmt_store_result($sql);
    mysqli_stmt_bind_result($sql,$idClient,$adrr,$idU);
    
    while (mysqli_stmt_fetch($sql)) {
        $rs["adrr"]=$adrr;
        $rs["idClient"]=$idClient;
    }
    echo json_encode($rs);
?>