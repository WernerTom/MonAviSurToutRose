<?php

    /*
     * Following code will list all the data in the db
     */

    // array for JSON response
    $response = array();

    // Classe de connexion
    require_once __DIR__ . '/connexion.php';

    // connecting to db
    $db = new CONNEXION_DB();

    /// $connexion à la base
    $result = mysqli_query($db->connection(), "SELECT * FROM Avis");

    // On vérifie si result est vide
    if (mysqli_num_rows($result) > 0) {
        $response["valeurs"] = array();

        while ($row = mysqli_fetch_array($result)) {
            $ligne = array();
            $ligne["id_Avis"] = $row["id_Avis"];
            $ligne["Auteur_Avis"] = $row["Auteur_Avis"];
            $ligne["Titre_Avis"] = $row["Titre_Avis"];
            $ligne["Contenu_Avis"] = $row["Contenu_Avis"];
            $ligne["Date_Avis"] = $row["Date_Avis"];
            $ligne["Latitude_Avis"] = $row["Latitude_Avis"];
            $ligne["Longitude_Avis"] = $row["Longitude_Avis"];


            // push single row into final response array
            array_push($response["valeurs"], $ligne);
        }
        // success
        $response["success"] = 1;

    } else {
        // no products found
        $response["success"] = 0;
        $response["message"] = "No data found";
    }

// echoing JSON response
echo json_encode($response);