<?php

class CONNEXION_DB
{
    function __construct()
    {
        // connexion à la base
        $this->connection();
    }

    function __destruct()
    {
        // fermer la connexion
        $this->fermer();
    }


   function connection() {
       $host_name  = "db682854157.db.1and1.com";
       $database   = "db682854157";
       $user_name  = "dbo682854157";
       $password   = "MonAvisSurToutRose123";

        // connexion à la base , ici : "zied" = mon mot de passe
        $connexion = mysqli_connect($host_name, $user_name, $password, $database) or die(mysqli_error());
        // selection de la base
        // $db = mysqli_select_db("db682854157") or die(mysqli_error()) or die(mysqli_error());

        return $connexion;
    }

    function fermer() {
    mysqli_close(); //Fermer la connexion
    }

}
