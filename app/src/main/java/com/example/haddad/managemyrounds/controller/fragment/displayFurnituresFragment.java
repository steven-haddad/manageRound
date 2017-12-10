package com.example.haddad.managemyrounds.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.haddad.managemyrounds.controller.round.DisplayFacesInformations;
import com.example.haddad.managemyrounds.controller.round.DisplayFurnituresInformation;
import com.example.haddad.managemyrounds.model.Furniture;
import com.example.haddad.managemyrounds.singleton.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;



public class displayFurnituresFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    public static final String TITLE = "Furnitures";
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

    CharSequence mLabel;
    View v;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragmentHandle = inflater.inflate(R.layout.fragment_display_furnitures, container, false);
        spinnerPostingPeriods= (Spinner) fragmentHandle.findViewById(R.id.spinnerPostingPeriod2);

        spinnerPostingPeriods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("youpi","wouhouuuuuuuuuuu");
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //No event
            }
        });
           ;

        return fragmentHandle;
    }

    public static displayFurnituresFragment newInstance() {
        return new displayFurnituresFragment();

    }


    public displayFurnituresFragment() {
// Required empty public constructor
    }


    public void onClick(View v) {

    }


}


