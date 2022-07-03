package com.example.mobilenavigation;


import android.app.Application;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

    private static MyApplication singleton;

    private List<Location> myLocations;
    private List<CustomMarker> wayPoints;
    private long id;
    private String oldName, oldGenre, oldAuthor;

    public List<CustomMarker> getWayPoints(){
        return wayPoints;
    }

    public CustomMarker returnWayPoint(){
        CustomMarker removed = wayPoints.get(0);
        removeWayPoint();
        return removed;
    }

    public CustomMarker getWayPoint(int i){
        return wayPoints.get(i);
    }

    public void addDestination(CustomMarker marker){
        wayPoints.add(marker);
    }

    public void removeWayPointByInstance(CustomMarker removed){
        for(int i=0; i<wayPoints.size();i++){
            if(wayPoints.get(i).equals(removed)){
                wayPoints.remove(i);
            }
        }
    }

    public void removeWayPoint(){
        if(wayPoints.size()>0){
            wayPoints.remove(0);
        }
    }

    public List<Location> getMyLocations(){
        return myLocations;
    }

    public MyApplication getInstance(){
        return singleton;
    }

    public void onCreate(){
        super.onCreate();
        singleton = this;
        myLocations = new ArrayList<>();
        wayPoints = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getOldGenre() {
        return oldGenre;
    }

    public void setOldGenre(String oldGenre) {
        this.oldGenre = oldGenre;
    }

    public String getOldAuthor() {
        return oldAuthor;
    }

    public void setOldAuthor(String oldAuthor) {
        this.oldAuthor = oldAuthor;
    }
}
