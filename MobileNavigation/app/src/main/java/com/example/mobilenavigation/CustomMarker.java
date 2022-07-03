package com.example.mobilenavigation;

import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

public class CustomMarker {

    private LatLng position;
    private String title, address;
    private Integer number;


    public CustomMarker(LatLng pos, String tit){
        position = pos;
        title = tit;
    }

    public CustomMarker(LatLng pos, String tit, Integer i){
        position = pos;
        title = tit;
        number = i;
    }

    public CustomMarker(LatLng pos, String tit, String address, Integer i){
        position = pos;
        title = tit;
        number = i;
        this.address = address;
    }

    public Integer getNumber(){
        return number;
    }

    public LatLng getPosition(){
        return position;
    }

    public String getTitle(){
        return title;
    }

    public String getAddress() {
        return address;
    }
}
