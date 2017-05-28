package com.example.weekydy.monavisurtoutrose;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Weekydy on 25/05/2017.
 */

public class editReviewActivity extends Activity {

    String col1, col2,col3,col4,col5,col6;
    EditText valCOL1,valCOL2,valCOL3,valCOL4,valCOL5,valCOL6;
    Button supp, edit;
    String urlSupp="http://monavissurtoutrose.weekydy.fr/delete_Avis.php";
    String urlUpd="http://monavissurtoutrose.weekydy.fr/update_Avis.php";
    String message;
    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editreview);
        Intent i=getIntent();
        String intentAction = getIntent().getAction();

        bindComposant(i);

        bindButton(intentAction);
    }

    public void bindComposant(Intent i){
        col1=i.getStringExtra("id_Avis");
        col2=i.getStringExtra("Auteur_Avis");
        col3=i.getStringExtra("Titre_Avis");
        col4=i.getStringExtra("Contenu_Avis");
        col5=i.getStringExtra("Latitude_Avis");
        col6=i.getStringExtra("Longitude_Avis");
        Log.i("verification", col1+"*"+col2+"*"+col3+"*"+col4+"*"+col5+"*"+col6);

        valCOL1=(EditText)findViewById(R.id.EditView_idAvis);
        valCOL2=(EditText)findViewById(R.id.EditView_Auteur);
        valCOL3=(EditText)findViewById(R.id.EditView_Title);
        valCOL4=(EditText)findViewById(R.id.EditView_Content);
        valCOL5=(EditText)findViewById(R.id.EditView_Latitude);
        valCOL6=(EditText)findViewById(R.id.EditView_Longitude);

        edit=(Button)findViewById(R.id.EditView_Submit);
        supp=(Button)findViewById(R.id.EditView_deleteReview);

        // On empeche la saisie des latitude/longitude
        valCOL1.setEnabled(false);
        valCOL5.setEnabled(false);
        valCOL6.setEnabled(false);

        // On rend l'id de l'Avis invisible
        valCOL1.setVisibility(View.INVISIBLE);

        valCOL1.setText(col1);
        valCOL2.setText(col2);
        valCOL3.setText(col3);
        valCOL4.setText(col4);
        valCOL5.setText(col5);
        valCOL6.setText(col6);

        // Récupération adresse via latitude/longitude
        getCompleteAddressString(Double.valueOf(col5), Double.valueOf(col6));

        // Récupération latitude/longitude si adresse change
        EditText txtEdit = (EditText) findViewById(R.id.EditView_Adresse);
        txtEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    getLocationFromAddress(((EditText) findViewById(R.id.EditView_Adresse)).getText().toString());
                }
            }
        });
    }

    public void bindButton(String intentAction){
        edit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new UpdateDataAsyncTask().execute();
            }
        });

        supp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new SuppDataAsyncTask().execute();
            }
        });

        //Bouton vers ListReview/Google Map
        if(intentAction == "fromListReview")
        {
            final Button btlisteActivity = (Button) findViewById(R.id.EditView_returnToList);
            btlisteActivity.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(editReviewActivity.this, listReviewActivity.class);
                    startActivity(intent);
                }
            });
        }

        if(intentAction == "fromMap")
        {
            final Button btlisteActivity = (Button) findViewById(R.id.EditView_returnToList);
            btlisteActivity.setText("Retour Google Map");
            btlisteActivity.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(editReviewActivity.this, cardReviewActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();

                EditText etAdresse = (EditText)findViewById(R.id.EditView_Adresse);
                etAdresse.setText(strAdd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            valCOL5.setText(String.valueOf(location.getLatitude()));
            valCOL6.setText(String.valueOf(location.getLongitude()));

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    private class UpdateDataAsyncTask extends  AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            Log.i("apdate", "onPreExecute");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("update", " start doInBackground");
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
            nameValuePair.add(new BasicNameValuePair("id_Avis",valCOL1.getText().toString()));
            nameValuePair.add(new BasicNameValuePair("Auteur_Avis",valCOL2.getText().toString()));
            nameValuePair.add(new BasicNameValuePair("Titre_Avis",valCOL3.getText().toString()));
            nameValuePair.add(new BasicNameValuePair("Contenu_Avis",valCOL4.getText().toString()));
            nameValuePair.add(new BasicNameValuePair("Latitude_Avis", valCOL5.getText().toString()));
            nameValuePair.add(new BasicNameValuePair("Longitude_Avis", valCOL6.getText().toString()));

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlUpd, ServiceHandler.GET,nameValuePair);

            Log.d("Response: ",jsonStr);
            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);
                    success = jsonObj.getInt("success");
                    message = jsonObj.getString("message");
                    Log.i("suucess", String.valueOf(success));
                    Log.i("message", message);

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

            Log.i("update", " end doInBackground");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("update", "onPostExecute");
            super.onPostExecute(result);
            if(success==1)
            {
                Toast.makeText(getApplicationContext(), "Mise à jour avec succée  "+message, Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Erreur" +message, Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent(editReviewActivity.this, listReviewActivity.class);
            startActivityForResult(intent, 100);
            finish();

        }

    }
    private class SuppDataAsyncTask extends  AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            Log.i("supp", "onPreExecute");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("supp", " start doInBackground");
            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
            nameValuePair.add(new BasicNameValuePair("id_Avis",valCOL1.getText().toString()));
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlSupp, ServiceHandler.GET,nameValuePair);

            Log.d("Response: ",jsonStr);
            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // return value of success
                    success=jsonObj.getInt("success");
                    message = jsonObj.getString("message");
                    Log.i("success", String.valueOf(success));
                    Log.i("message", message);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            Log.i("supp", " end doInBackground");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("supp", "onPostExecute");
            super.onPostExecute(result);
            if(success==1)
            {
                Toast.makeText(getApplicationContext(), "Supprimé ", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Erreur", Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent(editReviewActivity.this, listReviewActivity.class);
            startActivityForResult(intent, 100);
            finish();
        }
    }

}
