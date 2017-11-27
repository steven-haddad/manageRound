package com.example.haddad.managemyrounds.adapter;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haddad.managemyrounds.R;
import com.example.haddad.managemyrounds.controller.Main.MainActivity;
import com.example.haddad.managemyrounds.controller.round.DisplayFacesInformations;
import com.example.haddad.managemyrounds.controller.round.DisplayRoundsInformation;
import com.example.haddad.managemyrounds.controller.round.OptimizationRound;
import com.example.haddad.managemyrounds.model.Round;

import java.util.List;

public class RoundListAdapter extends ArrayAdapter<Round> {
    //tweets est la liste des models à afficher

    View convertView;
    Round round;
    public RoundListAdapter(Context context, List<Round> rounds) {
        super(context, 0, rounds);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_round,parent, false);
        }

        TweetViewHolder viewHolder = (TweetViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new TweetViewHolder();
            viewHolder.postingDay = (TextView) convertView.findViewById(R.id.postingDay);
            viewHolder.numberOfFaces = (TextView) convertView.findViewById(R.id.faceNumber);
            viewHolder.region = (TextView) convertView.findViewById(R.id.region);
            viewHolder.furnCode = (TextView) convertView.findViewById(R.id.idFurniture);

            convertView.setTag(viewHolder);


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView t1 = (TextView) view.findViewById(R.id.faceNumber);
                    String regionString = t1.getText().toString();
                    Intent intentDisplayFaces = new Intent(getContext(), DisplayFacesInformations.class);
                    intentDisplayFaces.putExtra("roundFurnCode", round.getFurnCode());
                    getContext().startActivity(intentDisplayFaces );

                }
            });
        }

        //getItem(position) va récupérer l'item [position] de la List<Round> rounds
        round = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.postingDay.setText(round.getpostingDay());
        viewHolder.numberOfFaces.setText(String.valueOf(round.getnumberOfFaces()));
        viewHolder.region.setText(round.getRegion());
        viewHolder.furnCode.setText(String.valueOf(round.getFurnCode()));

        return convertView;
    }

    private class TweetViewHolder{
        private TextView postingDay;
        private TextView numberOfFaces;
        private TextView region;
        private TextView furnCode;
    }


}
