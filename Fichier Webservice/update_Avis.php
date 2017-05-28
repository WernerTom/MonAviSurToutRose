<?php

    /*
     * Following code will update a row information
     * A row is identified by  id (col1)
     */

    // array for JSON response
    $response = array();

    // check for required fields
    if (isset($_GET['id_Avis']) && isset($_GET['Auteur_Avis']) && isset($_GET['Titre_Avis']) && isset($_GET['Contenu_Avis']) && isset($_GET['Latitude_Avis']) && isset($_GET['Longitude_Avis'])) {

        $id_Avis = $_GET['id_Avis'];
        $Auteur_Avis = $_GET['Auteur_Avis'];
        $Titre_Avis = $_GET['Titre_Avis'];
        $Contenu_Avis = $_GET['Contenu_Avis'];
        $Latitude_Avis = $_GET["Latitude_Avis"];
        $Longitude_Avis = $_GET["Longitude_Avis"];

        // include db connect class
        require_once __DIR__ . '/connexion.php';

        // connecting to db
        $db = new CONNEXION_DB();

        // mysql update row with matched pid
        $result = mysqli_query($db->connection(), "UPDATE Avis SET Auteur_Avis = '$Auteur_Avis', Titre_Avis = '$Titre_Avis', Contenu_Avis = '$Contenu_Avis', Latitude_Avis = '$Latitude_Avis', Longitude_Avis = '$Longitude_Avis' WHERE id_Avis = '$id_Avis'");

        // check if row inserted or not
        if ($result) {
            // successfully updated
            $response["success"] = 1;
            $response["message"] = "Row successfully updated.";
        }
    } else {
        // required field is missing
        $response["success"] = 0;
        $response["message"] = "Required field(s) is missing";
    }

// echoing JSON response
echo json_encode($response);
