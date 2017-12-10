package com.example.haddad.managemyrounds.controller.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.haddad.managemyrounds.R;

public class displayFacesFragment extends Fragment {

    public static final String TITLE = "Faces";
    public displayFacesFragment() {
// Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_display_faces, container, false);
    }

    public static displayFacesFragment newInstance() {
        return new displayFacesFragment();

    }

}
