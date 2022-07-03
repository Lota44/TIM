package com.example.mobilenavigation;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class WayPointAdapter extends ArrayAdapter<CustomMarker> {

    private ArrayList<CustomMarker> markers;


    public WayPointAdapter(@NonNull Context context, int resource, ArrayList<CustomMarker> list){
        super(context, R.layout.way_point_list_item,list);
        for(int i=0; i<list.size();i++){
            System.out.println(list.get(i).getTitle());
        }
        markers = (ArrayList<CustomMarker>) list;
        System.out.println("Singleton " + Singleton.get().wayPoints.size());
        System.out.println("Tworzenie Adaptera");
        System.out.println(this.getCount() + " Wynik funcki getcount");
        System.out.println(markers.size() + " Rozmiar listy markers");
        System.out.println(list.size() + "roziar listy list");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int phraseIndex = position;
        if(convertView == null){
            System.out.println("Wszedłem do ifa w adapterze");
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.way_point_list_item, parent, false);
        }

        ImageView iv_wayPointPin = convertView.findViewById(R.id.iv_wayPointPin);
        TextView tv_wayPointListAddress = convertView.findViewById(R.id.tv_wayPointListAddress);
        TextView tv_wayPointListDistance = convertView.findViewById(R.id.tv_wayPointListDistance);
        TextView tv_wayPointNumber = convertView.findViewById(R.id.tv_wayPointNumber);


        iv_wayPointPin.setImageResource(R.drawable.map_pin);
        tv_wayPointListAddress.setText(markers.get(position).getTitle());
        tv_wayPointListDistance.setText(markers.get(position).getAddress());
        tv_wayPointNumber.setText("");
//        tv_wayPointNumber.setText(markers.get(position).getNumber().toString());

        System.out.println("JESTEM W ADAPTERZE NA KOŃCU!!!!!!!!!!!!!!!!!!");

        return convertView;
    }
}
