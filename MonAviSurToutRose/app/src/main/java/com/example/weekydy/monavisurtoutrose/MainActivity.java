package com.example.weekydy.monavisurtoutrose;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bouton vers Ajout Avis
        final Button btAddView = (Button) findViewById(R.id.main_Ajout);
        btAddView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, addReviewActivity.class);
                startActivity(intent);
            }
        });

        //Bouton vers Liste des avis
        final Button btListeView = (Button) findViewById(R.id.main_Liste);
        btListeView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, listReviewActivity.class);
                startActivity(intent);
            }
        });

        //Bouton vers la carte de geolocalisation
        final Button btCarteView = (Button) findViewById(R.id.main_Carte);
        btCarteView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, cardReviewActivity.class);
                startActivity(intent);
            }
        });
    }
}
