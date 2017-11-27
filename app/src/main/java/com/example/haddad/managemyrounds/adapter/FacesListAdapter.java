package com.example.haddad.managemyrounds.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.haddad.managemyrounds.R;
import com.example.haddad.managemyrounds.controller.round.DisplayFacesInformations;
import com.example.haddad.managemyrounds.controller.round.OptimizationRound;
import com.example.haddad.managemyrounds.model.FurnitureDetail;
import com.example.haddad.managemyrounds.model.Round;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by haddad on 15/11/17.
 */


public class FacesListAdapter extends ArrayAdapter<FurnitureDetail> {
    //tweets est la liste des models à afficher


    View convertView;
    public FacesListAdapter(Context context, List<FurnitureDetail> furnitureDetails) {
        super(context, 0, furnitureDetails);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_faces,parent, false);
        }

        FurnitureDetailViewHolder viewHolder = (FurnitureDetailViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new FurnitureDetailViewHolder();
            viewHolder.newDesignImage= (ImageView) convertView.findViewById(R.id.newDesignImageView);
            viewHolder.oldDesignImage= (ImageView) convertView.findViewById(R.id.oldDesignImageView);
            viewHolder.faceCode = (TextView) convertView.findViewById(R.id.idFace);
            viewHolder.faceCode = (TextView) convertView.findViewById(R.id.idFace);
            viewHolder.newDesignName = (TextView) convertView.findViewById(R.id.newDesign);
            viewHolder.oldDesignName= (TextView) convertView.findViewById(R.id.oldDesign);

            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Round> rounds
        FurnitureDetail furnitureDetail = getItem(position);

        /**Link the object tot the view older**/

        //Load design image with library Glide
       Glide.with(getContext())
               .load(furnitureDetail.getnewDesignImage()).error(R.drawable.ic_dialog_close_dark)
                .into(viewHolder.newDesignImage);

        Glide.with(getContext())
                .load(furnitureDetail.getoldDesignImage()).error(R.drawable.ic_dialog_close_dark)
                .into(viewHolder.oldDesignImage);

        viewHolder.faceCode.setText(String.valueOf(furnitureDetail.getFaceCode()));

        return convertView;
    }

    private class FurnitureDetailViewHolder{
        private ImageView oldDesignImage;
        private ImageView newDesignImage;
        private TextView faceCode;
        private TextView newDesignName;
        private TextView oldDesignName;
    }
}
