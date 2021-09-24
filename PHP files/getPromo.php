<?php
    $db= new PDO("mysql:host=localhost;dbname=id12877554_rz","id12877554_binome","012345678");
    
    $prods=$db->query("select id_produit,img_promo,percent from promo")->fetchAll();
    
    $result=array();
    foreach($prods as $prod){
        $info=$db->query("select * from produit where id_produit='".$prod["id_produit"]."'")->fetch();
        $result[]=["idProduit"=>$prod["id_produit"],
                    "nomProduit"=>$info["nom_produit"],
                    "categ"=>$info["categorie"],
                    "prix"=>$info["prix"],
                    "image"=>$info["image"],
                    "imgPromo"=>$prod["img_promo"],
                    "percent"=>$prod["percent"]];
    }
    
    echo json_encode($result);
?>