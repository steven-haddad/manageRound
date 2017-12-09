package com.example.haddad.managemyrounds.controller.round;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.haddad.managemyrounds.R;
import com.example.haddad.managemyrounds.adapter.FacesListAdapter;
import com.example.haddad.managemyrounds.model.Face;
import com.example.haddad.managemyrounds.singleton.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayFacesInformations extends AppCompatActivity {

    private static final String TAG = "DisplayFacesInformations";
    ListView mListView;
    JSONArray jsonArrayNewImageUrl;
    JSONArray jsonArrayOldImageUrl;
    JSONArray jsonArrayFaceCode;
    String test;
    private ImageView newDesign;
    int arrSize;
    String selectedPeriod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_faces_informations);
        mListView = (ListView) findViewById(R.id.listView);

        getPostingPeriod();

        Bundle bundle = getIntent().getExtras();

        String roundFurnCode = bundle.getString("roundFurnCode");
        getFacesInfomations(String.valueOf(roundFurnCode));

    }

    private List<Face> genererFaces() throws JSONException {

        // Tag used to cancel the request
        final List<Face> faces = new ArrayList<Face>();
        for (int i = 0; i < arrSize; ++i) {
            faces.add(new Face(jsonArrayFaceCode.getString(i),jsonArrayNewImageUrl.getString(i),jsonArrayOldImageUrl.getString(i), "s", "s0", "s2"));
        }
        return faces;
    }


    private void afficherListeFaces() {

        List<Face> faces = null;
        try {
            faces = genererFaces();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FacesListAdapter adapter = new FacesListAdapter(DisplayFacesInformations.this, faces);
            mListView.setAdapter(adapter);

    }


    private void getFacesInfomations(final String roundFurnCode){

        String cancel_req_tag = "FACE";
        String URL_API_REST_DISPLAY_FACE = "http://managemyround.livehost.fr/JSON/retrieveAllFacesByFurnitures.php?roundFurnCode=" + roundFurnCode;
        Log.e("Url", URL_API_REST_DISPLAY_FACE);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                URL_API_REST_DISPLAY_FACE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject o = new JSONObject(response);

                    jsonArrayNewImageUrl = o.getJSONArray("newDesignImage");
                    jsonArrayOldImageUrl = o.getJSONArray("oldDesignImage");
                    jsonArrayFaceCode = o.getJSONArray("faceCode");

                    arrSize = jsonArrayNewImageUrl.length();

                    afficherListeFaces();


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
                params.put("roundFurnCode", roundFurnCode);
                return params;

            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    public void getPostingPeriod() {

        SharedPreferences myPrefs = this.getSharedPreferences("selectedPeriod", MODE_PRIVATE);
        selectedPeriod = myPrefs.getString("selectedPeriod", null);

    }
}
