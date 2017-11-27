package com.example.haddad.managemyrounds.controller.round;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.haddad.managemyrounds.R;
import com.example.haddad.managemyrounds.adapter.RoundListAdapter;
import com.example.haddad.managemyrounds.model.Round;
import com.example.haddad.managemyrounds.singleton.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayRoundsInformation extends Activity {

    private static final String TAG = "DisplayFurnitures";
    ListView mListView;
    JSONArray jsonArrayFurnCode;
    JSONArray jsonArrayNumberOfFaces;
    JSONArray jsonArrayRegion;
    JSONArray jsonArrayPostingDay;
    int arrSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_rounds_information);
        mListView = (ListView) findViewById(R.id.listView);

        getFurnitureInfomations();

    }

    private List<Round> genererRounds() throws JSONException {
        // Tag used to cancel the request
        final List<Round> rounds = new ArrayList<Round>();
        for (int i = 0; i < arrSize; ++i) {
            rounds.add(new Round(jsonArrayPostingDay.getString(i), jsonArrayNumberOfFaces.getString(i), jsonArrayRegion.getString(i), jsonArrayFurnCode.getString(i)));
        }
        return rounds;
    }


    private void afficherListeRounds() {
        List<Round> rounds = null;
        try {
            rounds = genererRounds();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RoundListAdapter adapter = new RoundListAdapter(DisplayRoundsInformation.this, rounds);
        mListView.setAdapter(adapter);
    }

    private void getFurnitureInfomations() {

        String cancel_req_tag = "FACE";
        String URL_API_REST_DISPLAY_FACE = "http://managemyround.livehost.fr/JSON/retrieveAllFurnitureByPeriod.php";
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
                    arrSize = jsonArrayFurnCode.length();

                    afficherListeRounds();


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
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("roundFurnCode", "test");
                return params;

            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }


}

