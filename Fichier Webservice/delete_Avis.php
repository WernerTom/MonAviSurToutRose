<?php

    /*
     Requete HTTP Post
     */

    // tableau de reponse JSON (array)
    $reponse = array();

    // tester s'il y a une donnée récue

    if (isset($_GET['id_Avis'])) {

        $id_Avis = $_GET['id_Avis'];
        // inclure la classe de connexion
        require_once __DIR__ . '/connexion.php';

        // connexion à la base
        $db = new CONNEXION_DB ();

        // supprimer la ligne
        $resultat = mysqli_query($db->connection(), "DELETE FROM Avis WHERE id_Avis = '$id_Avis'");

        // tester si la ligne est supprimée ou non
        if ($resultat) {
            // ligne supprimée
            $reponse["success"] = 1;
            $reponse["message"] = "ligne supprimée";
        } else {
            $reponse["success"] = 0;
            $reponse["message"] = "Erreur de suppression";
        }
    } else {
        // Champ manquant col1
        $reponse["success"] = 0;
        $reponse["message"] = "Champ manquant";
    }

// afficher  la reponse JSON
echo json_encode($reponse);
