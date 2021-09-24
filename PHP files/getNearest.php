<?php
    function getRightTraject($db,$latitude,$longitude){
        $sql= $db->prepare("SELECT * FROM trajet");
        $sql->execute();
        while($row = $sql->fetch(PDO::FETCH_ASSOC)) {
            $idposition=$row["pos_debut"];
            $details= $db->query("SELECT * FROM position where id_pos='$idposition'");
            $row1 = $details->fetch();
            $locations [] =["id"=>$row["id_Trajet"],"lat"=>$row1["x"],"lng"=>$row1["y"]];
        }
        
        $base_location = array(
          'lat' => $latitude,
          'lng' => $longitude
        );
        
        $distances = array();

        foreach ($locations as $key => $location)
        {
          $a = $base_location['lat'] - $location['lat'];
          $b = $base_location['lng'] - $location['lng'];
          $distance = sqrt(($a**2) + ($b**2));
          $distances[$key] = $distance;
        }
        
        asort($distances);
        
        $closest = $locations[key($distances)];
        
        return $closest['id'];
    }
    
    
?>