package com.example.haddad.managemyrounds.controller.round;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.haddad.managemyrounds.R;
import com.example.haddad.managemyrounds.adapter.FurnitureListAdapter;
import com.example.haddad.managemyrounds.model.Furniture;
import com.example.haddad.managemyrounds.singleton.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayFurnituresInformation extends Activity {

    private static final String TAG = "DisplayFurnitures";
    ListView mListView;

    // Posting period json
    JSONArray jsonArrayWeeks;
    JSONArray jsonArrayFromDates;
    JSONArray jsonArrayToDates;
    int arrSizePostingPeriod;
    Spinner spinnerPostingPeriods;
    ArrayList<String> postingPeriods;
    String selectedPeriod;

    // Furniture Json
    JSONArray jsonArrayFurnCode;
    JSONArray jsonArrayNumberOfFaces;
    JSONArray jsonArrayRegion;
    JSONArray jsonArrayPostingDay;
    int arrSizeFurniture;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_rounds_information);

        mListView = (ListView) findViewById(R.id.listViewRound);
        spinnerPostingPeriods = (Spinner) findViewById(R.id.spinnerPostingPeriod) ;

        // GET POSTING PERIOD
        getPostingPeriodsInfomations();

        /*Select spinner value launch two events
            get the selected value
            launch getFurnituresInformations method
         */

        spinnerPostingPeriods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = adapterView.getItemAtPosition(i).toString();
                String[] periods=selected.split(" ");
                selectedPeriod=periods[0];
                getFurnitureInfomations(selectedPeriod);
                //Share the selected posting period with other activities
                shareSelectedPeriod(selectedPeriod);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //No event
            }
        });

        getPostingPeriodsInfomations();

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                TextView idFurniture = (TextView) view.findViewById(R.id.idFurniture);
                String stringIdFurniture = idFurniture.getText().toString();

                Intent intentDisplayFaces = new Intent(DisplayFurnituresInformation.this, DisplayFacesInformations.class);
                intentDisplayFaces.putExtra("roundFurnCode", stringIdFurniture);
                startActivity(intentDisplayFaces );
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                //No event
            }
        });

    }

    /*
    GET THE POSTING PERIOD AND POPULATE SPINNER WITH THE DATA
     */

    public void getPostingPeriodsInfomations() {

        String cancel_req_tag = "Posting Periods";
        String URL_API_REST_DISPLAY_POSTING_PERIODS = "http://managemyround.livehost.fr/JSON/getAllValidPostingPeriods.php";
        Log.e("Url", URL_API_REST_DISPLAY_POSTING_PERIODS);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                URL_API_REST_DISPLAY_POSTING_PERIODS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject o = new JSONObject(response);

                    jsonArrayWeeks = o.getJSONArray("week");
                    jsonArrayFromDates = o.getJSONArray("from_date");
                    jsonArrayToDates = o.getJSONArray("to_date");

                    arrSizePostingPeriod = jsonArrayWeeks.length();
                    postingPeriods = new ArrayList<String>();

                    for (int i = 0; i <= arrSizePostingPeriod; i++) {
                        try {
                            postingPeriods.add(jsonArrayWeeks.getString(i)+"  :"+jsonArrayFromDates.getString(i)+" - "+jsonArrayToDates.getString(i));
                            Log.i("t", String.valueOf(postingPeriods));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        populateSpinnerPostingPeriods();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })
        {
        };
        // Adding request to request queue

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void populateSpinnerPostingPeriods()  {
        spinnerPostingPeriods = (Spinner) findViewById(R.id.spinnerPostingPeriod) ;

        spinnerPostingPeriods.setAdapter(new ArrayAdapter<String>(DisplayFurnituresInformation.this, android.R.layout.simple_spinner_dropdown_item, postingPeriods));
        java.util.ArrayList<String> strings = new java.util.ArrayList<>();
    }

    /*
    Populate furnitures list view

    getFurnituresInformations : Https request to retrieve all furnitures (criterias: Posting period selected,id_agent)
    generateFurnitures: populate Furnitures list with json
    displayFurnituresList:  populate the list view  with furnitures list

     */

    private List<Furniture> generateFurnitures() throws JSONException {
        // Tag used to cancel the request
        final List<Furniture> furnitures = new ArrayList<Furniture>();

        for (int i = 0; i < arrSizeFurniture; ++i) {
            furnitures.add(new Furniture(jsonArrayPostingDay.getString(i), jsonArrayNumberOfFaces.getString(i), jsonArrayRegion.getString(i), jsonArrayFurnCode.getString(i)));
        }
        return furnitures;
    }

    private void displayFurnituresList() {
        List<Furniture> furnitures = null;
        try {
            furnitures = generateFurnitures();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        FurnitureListAdapter adapter = new FurnitureListAdapter(DisplayFurnituresInformation.this, furnitures);
        mListView.setAdapter(adapter);
    }

    private void getFurnitureInfomations(final String periodSelected) {

        String cancel_req_tag = "FACE";
        String URL_API_REST_DISPLAY_FACE = "http://managemyround.livehost.fr/JSON/retrieveAllFurnitureByPeriod.php?selectedPeriod=" + periodSelected;
        Log.e("Url", URL_API_REST_DISPLAY_FACE);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                URL_API_REST_DISPLAY_FACE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject o = new JSONObject(response);

                    jsonArrayFurnCode = o.getJSONArray("furnCode");
                    jsonArrayNumberOfFaces = o.getJSONArray("numberOfFaces");
                    jsonArrayRegion = o.getJSONArray("region");
                    jsonArrayPostingDay = o.getJSONArray("postingDay");
                    arrSizeFurniture = jsonArrayFurnCode.length();

                    displayFurnituresList();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })

        {
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("selectedPeriod", periodSelected);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }


    public void shareSelectedPeriod(String selectedPeriod){
        SharedPreferences myPrefs = this.getSharedPreferences("selectedPeriod", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putString("selectedPeriod", selectedPeriod);

        prefsEditor.commit();
    }
}
