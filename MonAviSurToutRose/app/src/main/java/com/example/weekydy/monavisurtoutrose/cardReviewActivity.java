package com.example.weekydy.monavisurtoutrose;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weekydy on 21/05/2017.
 */

public class cardReviewActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnInfoWindowClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    String urlGet = "http://monavissurtoutrose.weekydy.fr/affichage_liste.php";
    GetDataAsyncTask getData;
    String message;
    int success;
    List<String> lstData, lstPoints;

    GoogleMap theMap = null;
    Marker marker;
    Location userLocation;
    GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_review);

        lstData = new ArrayList<String>();
        lstPoints = new ArrayList<String>();
        getData = new GetDataAsyncTask();
        getData.execute();

        handleAccessPermissions(true);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bindApiClient();

        bindButton();
    }

    public void bindButton(){
        //Bouton vers Accueil
        final Button btMainActivity = (Button) findViewById(R.id.CardView_returnToMain);
        btMainActivity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cardReviewActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void bindApiClient(){
        //Initialisation googleApiClient
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void handleAccessPermissions(boolean requestPermissionsIfNeeded) {
        Log.d("POSITION", "Will check permissions...");

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        int fineLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        ArrayList<String> permissionsToRequest = new ArrayList<String>();

        //FINE LOCATION
        if (fineLocationPermission == PackageManager.PERMISSION_GRANTED) {
            Log.d("POSITION", "FINE location ok.");

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, this);
        } else {
            Log.d("POSITION", "FINE location nok.");

            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        //COARSE LOCATION
        if (coarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            Log.d("POSITION", "COARSE location ok.");

            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        } else {
            Log.d("POSITION", "COARSE location nok.");

            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (permissionsToRequest.size() == 0) {
            return;
        }

        if (!requestPermissionsIfNeeded) {
            return;
        }

        Log.d("POSITION", "Need to ask some permissions.");
        String[] permissionsToRequestArray = permissionsToRequest.toArray(new String[0]);
        ActivityCompat.requestPermissions(this, permissionsToRequestArray, 0);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("POSITION", "Received permissions results...");
        handleAccessPermissions(false);
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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("POSITION", "MAP is ready.");
        LatLng target;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        userLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (userLocation != null) {
            //Récupération user longitude et latitude
            Double longitude = userLocation.getLongitude();
            Double latitude = userLocation.getLatitude();

            target = new LatLng(latitude, longitude);
        }
         else
        {
            target = new LatLng(43.31, 5.365);

        }
        googleMap.setMyLocationEnabled(true);

        googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(target, 13) );
        theMap = googleMap;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        double latitude = marker.getPosition().latitude;
        double longitude = marker.getPosition().longitude;

        if (( String.valueOf(latitude) != null) && (String.valueOf(longitude) != null))
        {
            for(int x=0; x<lstData.size();x++)
            {
                String strItem =(String) lstData.get(x);
                String[] patrs = strItem.split(" - ");

                Double searchLatitude = Double.parseDouble(patrs[4]);
                Double searchLongitude = Double.parseDouble(patrs[5]);

                if ((searchLatitude == latitude) &&(searchLongitude == longitude))
                {
                    Intent intent = new Intent(cardReviewActivity.this, editReviewActivity.class);
                    intent.putExtra("id_Avis", patrs[0]);
                    intent.putExtra("Auteur_Avis", patrs[1]);
                    intent.putExtra("Titre_Avis", patrs[2]);
                    intent.putExtra("Contenu_Avis", patrs[3]);
                    intent.putExtra("Latitude_Avis", patrs[4]);
                    intent.putExtra("Longitude_Avis", patrs[5]);
                    intent.setAction("fromMap");
                    startActivityForResult(intent, 101);
                    finish();
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class GetDataAsyncTask extends  AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            Log.i("add", "onPreExecute");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("add", " start doInBackground");
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlGet, ServiceHandler.GET);

            Log.d("Response: ",jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // return value of success
                    success=jsonObj.getInt("success");
                    Log.i("success", String.valueOf(success));
                    if (success==0)
                    {
                        // success=0 ==> there is a string = message
                        message=jsonObj.getString("message");
                        Log.i("message", message);
                    }
                    else if (success==1)
                    {
                        // success=1 ==> there is an array of data = valeurs
                        JSONArray dataValues = jsonObj.getJSONArray("valeurs");
                        // loop each row in the array
                        for(int j=0;j<dataValues.length();j++)
                        {
                            JSONObject values = dataValues.getJSONObject(j);
                            String valCol1= values.getString("id_Avis");
                            String valCol2= values.getString("Auteur_Avis");
                            String valCol3= values.getString("Titre_Avis");
                            String valCol4= values.getString("Contenu_Avis");
                            String valCol5= values.getString("Latitude_Avis");
                            String valCol6= values.getString("Longitude_Avis");
                            //add a string witch contains all of data getted from the response
                            lstData.add(valCol1+" - "+valCol2+" - "+valCol3+" - "+valCol4+" - "+valCol5+" - "+valCol6);
                            Log.i("Row "+(j+1), valCol2+" - "+valCol3);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
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
                for(int x=0; x<lstData.size();x++)
                {
                    String strItem =(String) lstData.get(x);
                    String[] patrs = strItem.split(" - ");
                    LatLng target2 = new LatLng(Double.parseDouble(patrs[4]), Double.parseDouble(patrs[5]));


                    MarkerOptions markerOptions = new MarkerOptions()
                            .title(patrs[2])
                            .snippet(patrs[3])
                            .position(target2);

                    marker = theMap.addMarker(markerOptions);

                    theMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {

                            double latitude = marker.getPosition().latitude;
                            double longitude = marker.getPosition().longitude;

                            if (( String.valueOf(latitude) != null) && (String.valueOf(longitude) != null))
                            {
                                for(int x=0; x<lstData.size();x++)
                                {
                                    String strItem =(String) lstData.get(x);
                                    String[] patrs = strItem.split(" - ");

                                    Double searchLatitude = Double.parseDouble(patrs[4]);
                                    Double searchLongitude = Double.parseDouble(patrs[5]);

                                    if ((searchLatitude == latitude) &&(searchLongitude == longitude))
                                    {
                                        Intent intent = new Intent(cardReviewActivity.this, editReviewActivity.class);
                                        intent.putExtra("id_Avis", patrs[0]);
                                        intent.putExtra("Auteur_Avis", patrs[1]);
                                        intent.putExtra("Titre_Avis", patrs[2]);
                                        intent.putExtra("Contenu_Avis", patrs[3]);
                                        intent.putExtra("Latitude_Avis", patrs[4]);
                                        intent.putExtra("Longitude_Avis", patrs[5]);
                                        intent.setAction("fromMap");
                                        startActivityForResult(intent, 101);
                                        finish();
                                    }
                                }
                            }
                        }
                    });
                }
//                Toast.makeText(getApplicationContext(), "Bien reçues ", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Erreur", Toast.LENGTH_LONG).show();
            }


        }

    }

}
