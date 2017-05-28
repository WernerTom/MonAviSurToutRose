<?php
    /* Requête HTTP Post */

    // tableau de réponse JSON (array)
    $reponse = array();

    // tester si les champs sont valides
    if (isset($_GET['Auteur_Avis']) && isset($_GET['Titre_Avis']) && isset($_GET['Contenu_Avis']) && isset($_GET['Date_Avis']) && isset($_GET['Latitude_Avis']) && isset($_GET['Longitude_Avis'])) {
        $valeur_Auteur = $_GET['Auteur_Avis'];
        $valeur_Titre = $_GET['Titre_Avis'];
        $valeur_Contenu = $_GET['Contenu_Avis'];
		$valeur_Date = $_GET['Date_Avis'];
        $valeur_Latitude = $_GET["Latitude_Avis"];
        $valeur_Longitude = $_GET["Longitude_Avis"];

        // Classe de connexion
        require_once __DIR__ . '/connexion.php';

        // $connexion à la base
        $db = new CONNEXION_DB ();

        // requéte pour insérer les données
        $resultat = mysqli_query($db->connection(), "INSERT INTO Avis(	Auteur_Avis, Titre_Avis, Contenu_Avis, Date_Avis, Latitude_Avis, Longitude_Avis) VALUES ('$valeur_Auteur', '$valeur_Titre','$valeur_Contenu','$valeur_Date','$valeur_Latitude','$valeur_Longitude')");


        // tester si les données sont bien insérées
        if (false===$resultat) {
            // erreur d'insertion
            $reponse["success"] = 0;
            $reponse["message"] = "Oops! Erreur d'insertion.";
        }
        else
        {
            // Données bien insérées
            $reponse["success"] = 1;
            $reponse["message"] = "Donnees bien inserees";
        }
    }
    else
    {
        // Champ(s) manquant(s)
        $reponse["success"] = 0;
        $reponse["message"] = "Champ(s) manquant(s)";
    }

// afficher la réponse JSON
echo json_encode($reponse);