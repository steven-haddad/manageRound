package com.example.haddad.managemyrounds.adapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.haddad.managemyrounds.R;
import com.example.haddad.managemyrounds.controller.Main.MainActivity;
import com.example.haddad.managemyrounds.controller.round.DisplayFacesInformations;
import com.example.haddad.managemyrounds.controller.round.DisplayFurnituresInformation;
import com.example.haddad.managemyrounds.model.Furniture;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FurnitureListAdapter extends ArrayAdapter<Furniture> {

    View convertView;
    Furniture furniture;
    String regionString;
    Spinner spinnerPostingPeriods;
    String selectedPeriod;

    public FurnitureListAdapter(Context context, List<Furniture> furnitures) {
        super(context, 0, furnitures);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_round,parent, false);
        }

        FurnitureViewHolder viewHolder = (FurnitureViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new FurnitureViewHolder();
            viewHolder.postingDay = (TextView) convertView.findViewById(R.id.postingDay);
            viewHolder.numberOfFaces = (TextView) convertView.findViewById(R.id.faceNumber);
            viewHolder.region = (TextView) convertView.findViewById(R.id.region);
            viewHolder.furnCode = (TextView) convertView.findViewById(R.id.idFurniture);
            viewHolder.selectedPeriod=(Spinner)convertView.findViewById(R.id.spinnerPostingPeriod);
            viewHolder.validate=(CircleImageView) convertView.findViewById(R.id.imageButtonValidation);
            convertView.setTag(viewHolder);


        }

        //getItem(position) va récupérer l'item [position] de la List<Furniture> rounds
        furniture = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.postingDay.setText(furniture.getpostingDay());
        viewHolder.numberOfFaces.setText(String.valueOf(furniture.getnumberOfFaces()));
        viewHolder.region.setText(furniture.getRegion());
        viewHolder.furnCode.setText(String.valueOf(furniture.getFurnCode()));
        viewHolder.validate.setImageResource(R.mipmap.validation_icon);

        return convertView;
    }

    private class FurnitureViewHolder{
        private TextView postingDay;
        private TextView numberOfFaces;
        private TextView region;
        private TextView furnCode;
        private Spinner selectedPeriod;

        private ImageView validate;


}


}
