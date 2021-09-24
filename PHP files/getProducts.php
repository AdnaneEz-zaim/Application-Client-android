<?php
    $db= new PDO("mysql:host=localhost;dbname=id12877554_rz","id12877554_binome","012345678");
    $sql= $db->prepare("SELECT * FROM produit");
    $sql->execute();
    while($row = $sql->fetch(PDO::FETCH_ASSOC)) {
        $prods[] =$row;
    }
    echo json_encode($prods);
?>
