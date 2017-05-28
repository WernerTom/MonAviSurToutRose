package com.example.weekydy.monavisurtoutrose;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Weekydy on 21/05/2017.
 */

public class addReviewActivity extends Activity implements LocationListener {

    Button ajout;
    EditText col2Valeur, col3Valeur, col4Valeur, col5Valeur, col6Valeur;
    String urlAdd = "http://monavissurtoutrose.weekydy.fr/add_Avis.php";
    AddDataAsyncTask AddData;
    String message;
    int success;

    Location userLocation;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_review);

        ajout = (Button) findViewById(R.id.AddView_Submit);
        col2Valeur = (EditText) findViewById(R.id.AddView_Auteur);
        col3Valeur = (EditText) findViewById(R.id.AddView_Title);
        col4Valeur = (EditText) findViewById(R.id.AddView_Content);

        col5Valeur = (EditText) findViewById(R.id.AddView_Latitude);
        col6Valeur = (EditText) findViewById(R.id.AddView_Longitude);

        col5Valeur.setEnabled(false);
        col6Valeur.setEnabled(false);
        ajout.setEnabled(false);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        bindButton();
    }

    public void bindButton(){
        final CheckBox chkMyPosition = (CheckBox) findViewById(R.id.AddView_chkMyPos);
        chkMyPosition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                {
                    getCompleteAddressString(userLocation.getLatitude(), userLocation.getLongitude());
                    col5Valeur.setText(String.valueOf(userLocation.getLatitude()));
                    col6Valeur.setText(String.valueOf(userLocation.getLongitude()));

                }
                else
                {
                    EditText adressValue = null;
                    adressValue.setText("");
                    getLocationFromAddress((adressValue).getText().toString());
                }

            }
        });

        //Bouton vers Accueil
        final Button btMainActivity = (Button) findViewById(R.id.AddView_returnToMain);
        btMainActivity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addReviewActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        EditText txtEdit = (EditText) findViewById(R.id.AddView_Adresse);
        txtEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    getLocationFromAddress(((EditText) findViewById(R.id.AddView_Adresse)).getText().toString());
                }
            }
        });

        txtEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                chkMyPosition.setChecked(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ajout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                AddData =new AddDataAsyncTask();
                AddData.execute();

            }
        });
    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address.size() == 0 || strAddress == "") {

                String strErreur = "Adresse incorrect, la saisie est obligatoire";
                Toast.makeText(getApplicationContext(), "Erreur : "+strErreur, Toast.LENGTH_LONG).show();

                col5Valeur.setText("");
                col6Valeur.setText("");
                ajout.setEnabled(false);

                return null;
            }
            else
            {
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();

                col5Valeur.setText(String.valueOf(location.getLatitude()));
                col6Valeur.setText(String.valueOf(location.getLongitude()));

                ajout.setEnabled(true);

                p1 = new LatLng(location.getLatitude(), location.getLongitude() );
            }
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
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

                EditText etAdresse = (EditText)findViewById(R.id.AddView_Adresse);
                etAdresse.setText(strAdd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    private class AddDataAsyncTask extends  AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            Log.i("add", "onPreExecute");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("add", " start doInBackground");

            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
            Date todayDate = new Date();
            String thisDate = currentDate.format(todayDate);
            Log.d("add", thisDate);


            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);

            nameValuePair.add(new BasicNameValuePair("Auteur_Avis",col2Valeur.getText().toString()));
            nameValuePair.add(new BasicNameValuePair("Titre_Avis",col3Valeur.getText().toString()));
            nameValuePair.add(new BasicNameValuePair("Contenu_Avis",col4Valeur.getText().toString()));
            nameValuePair.add(new BasicNameValuePair("Date_Avis", thisDate));
            nameValuePair.add(new BasicNameValuePair("Latitude_Avis", col5Valeur.getText().toString()));
            nameValuePair.add(new BasicNameValuePair("Longitude_Avis", col6Valeur.getText().toString()));

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlAdd, ServiceHandler.GET,nameValuePair);

            Log.d("Response: ",jsonStr);
            if (jsonStr != null) {
                try {

                    JSONObject jsonObj = new JSONObject(jsonStr);
                    success = jsonObj.getInt("success");
                    message = jsonObj.getString("message");
                    Log.i("success", String.valueOf(success));
                    Log.i("message", message);

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

            Log.i("add", " end doInBackground");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("add", "onPostExecute");
            super.onPostExecute(result);
            if(success==1)
            {
                Toast.makeText(getApplicationContext(), "Succes :"+message, Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Erreur" +message, Toast.LENGTH_LONG).show();
            }
        }

    }
}
