package com.example.haddad.managemyrounds.controller.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.haddad.managemyrounds.R;

import java.util.ArrayList;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class displayFacesFragment extends Fragment {

    public static final String TITLE = "Faces";
    public String selectedPeriod;
    public Set<String>  idFurnitures;


    public displayFacesFragment() {
// Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup containerFaces, Bundle savedInstanceState) {
        View fragmentHandle = inflater.inflate(R.layout.fragment_display_faces, containerFaces, false);
        Button button= (Button) fragmentHandle.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelectedFurnitures();
            }
        });


        return inflater.inflate(R.layout.fragment_display_faces, containerFaces, false);
    }

    public static displayFacesFragment newInstance() {
        return new displayFacesFragment();

    }




    public void getSelectedPeriod(){

        SharedPreferences myPrefs = this.getActivity().getSharedPreferences("selectedPeriod", MODE_PRIVATE);
        selectedPeriod = myPrefs.getString("selectedPeriod", null);
        String selectedPeriod1=selectedPeriod;
    }


    public void getSelectedFurnitures(){

        Bundle bundle = getArguments();
        ArrayList<String> a1= bundle.getStringArrayList("selectedFurnitures");



    }





}
