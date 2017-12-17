package com.example.haddad.managemyrounds.controller.round;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.haddad.managemyrounds.R;
import com.example.haddad.managemyrounds.adapter.FurnitureListAdapter;
import com.example.haddad.managemyrounds.controller.Main.MainActivity;
import com.example.haddad.managemyrounds.model.Furniture;
import com.example.haddad.managemyrounds.singleton.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class DisplayFurnituresInformation extends MainActivity{

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

    //Send the furnitures selected
    ArrayList<String> listIdFurnitures =  new ArrayList<String>();
    TextView idFurniture;



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

        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL); // Important


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                RelativeLayout relativeLayout= (RelativeLayout) view.findViewById(R.id.relativeLayoutRowRound);
                relativeLayout.setBackgroundColor(Color.parseColor("#FF81879F"));
                idFurniture = (TextView) view.findViewById(R.id.idFurniture);


                ColorDrawable viewColor = (ColorDrawable) relativeLayout.getBackground();
                int colorId = viewColor.getColor();
                System.out.print((colorId));

                listIdFurnitures.add(idFurniture.getText().toString());
/*
                Intent intentDisplayFaces = new Intent(DisplayFurnituresInformation.this, DisplayFacesInformations.class);
                intentDisplayFaces.putExtra("roundFurnCode", idFurniture.getText().toString());
                startActivity(intentDisplayFaces );*/
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

    public void intentActivitiDisplayFaces(View v){
        Intent intentDisplayFaces = new Intent(DisplayFurnituresInformation.this, DisplayFacesInformations.class);
        intentDisplayFaces.putExtra("roundFurnCode", listIdFurnitures);
        startActivity(intentDisplayFaces );
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_displayRounds:


            case R.id.nav_gallery:



        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }








}
