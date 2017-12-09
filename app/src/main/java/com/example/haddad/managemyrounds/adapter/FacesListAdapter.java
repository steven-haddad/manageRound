package com.example.haddad.managemyrounds.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.haddad.managemyrounds.R;
import com.example.haddad.managemyrounds.model.Face;

import java.util.List;

/**
 * Created by haddad on 15/11/17.
 */


public class FacesListAdapter extends ArrayAdapter<Face> {

    View convertView;

    public FacesListAdapter(Context context, List<Face> faces) {
        super(context, 0, faces);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_faces,parent, false);
        }

        FurnitureDetailViewHolder viewHolder = (FurnitureDetailViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new FurnitureDetailViewHolder();
            viewHolder.newDesignImage= (ImageView) convertView.findViewById(R.id.newDesignImage);
            viewHolder.faceCode = (TextView) convertView.findViewById(R.id.idFace);

            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Furniture> rounds
        Face face = getItem(position);

        //Load design image with library Glide
       Glide.with(getContext())
               .load(face.getnewDesignImage()).error(R.drawable.ic_dialog_close_dark)
                .into(viewHolder.newDesignImage);

        viewHolder.faceCode.setText(String.valueOf(face.getFaceCode()));

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
