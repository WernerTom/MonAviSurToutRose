package com.example.weekydy.monavisurtoutrose;

import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Weekydy on 21/05/2017.
 */

public class listReviewActivity extends Activity {
    String urlGet="http://monavissurtoutrose.weekydy.fr/affichage_liste.php";
    GetDataAsyncTask getData;
    String message;
    int success;
    ListView lv, lvData;
    List<String> lstData, lstDataView;
    ArrayAdapter arrayadp, arrayadp2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_review);

        //Bouton vers Accueil
        final Button btMainActivity = (Button) findViewById(R.id.ListView_returnToMain);
        btMainActivity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(listReviewActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        lv=(ListView)findViewById(R.id.reviews_list);
        lvData=(ListView)findViewById(R.id.reviews_list);
        lstData = new ArrayList<String>();
        lstDataView = new ArrayList<String>();
        getData = new GetDataAsyncTask();
        getData.execute();
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)
            {
                String s=(String) (lstData.get(arg2));
                String[] patrs = s.split(" - ");
                Intent intent = new Intent(listReviewActivity.this, editReviewActivity.class);
                intent.putExtra("id_Avis", patrs[0]);
                intent.putExtra("Auteur_Avis", patrs[1]);
                intent.putExtra("Titre_Avis", patrs[2]);
                intent.putExtra("Contenu_Avis", patrs[3]);
                intent.putExtra("Latitude_Avis", patrs[4]);
                intent.putExtra("Longitude_Avis", patrs[5]);
                intent.setAction("fromListReview");
                startActivityForResult(intent, 100);
                finish();
            }
        });
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
                            lstDataView.add("Titre : "+valCol3+"\nContenu : "+valCol4);
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
                // Toast.makeText(getApplicationContext(), "Bien recues ", Toast.LENGTH_LONG).show();
                // show the list view contains the data
                arrayadp=new ArrayAdapter(getApplicationContext(),  android.R.layout.simple_list_item_1, lstDataView);
                lv.setAdapter(arrayadp);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Erreur", Toast.LENGTH_LONG).show();
            }


        }

    }

}
