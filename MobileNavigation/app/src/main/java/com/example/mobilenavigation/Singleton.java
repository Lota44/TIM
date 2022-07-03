package com.example.mobilenavigation;

import android.location.Geocoder;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class Singleton {
    private WayPointAdapter adapter;

    public ArrayList<CustomMarker> wayPoints;
    private Geocoder geocoder;


    private Singleton(){
        //adapter = new WayPointAdapter(null, R.layout.way_point_list_item, null);
        wayPoints = new ArrayList<CustomMarker>();
    }
    private static Singleton singleton;

    public WayPointAdapter getWayPointAdapter(){
        return adapter;
    }

    public Geocoder getGeocoder(){ return geocoder;}

    public void setGeocoder(Geocoder geo){
        geocoder = geo;
    }

    public void setWayPointAdapter(WayPointAdapter adapter){
         this.adapter = adapter;
    }

    public void addWayPoint(CustomMarker marker){
    }

    public static Singleton get(){


        if( singleton == null){
            singleton = new Singleton();
        }
        return singleton;
    }
}

