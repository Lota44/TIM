package com.example.mobilenavigation;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowSavedLocationsList extends AppCompatActivity {

    private ListView lv_wayPoints;
    private Geocoder geocoder;
    private ArrayList<CustomMarker> markers = new ArrayList<>();
    private MyApplication myApplication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_locations_list);

        myApplication = (MyApplication)getApplicationContext();
        List<Location> savedLocations = myApplication.getMyLocations();

        geocoder = new Geocoder(ShowSavedLocationsList.this);
        lv_wayPoints = findViewById(R.id.lv_wayPoints);

        markers = new ArrayList<CustomMarker>();
        prepareList(markers);

        for(int i=0; i<markers.size(); i++){
            System.out.println(markers.get(i).getTitle()+ "  ELEMENT MOICH MARKERÓW DO wyświetlenia " + markers.get(i).getNumber());
        }
        try {
            WayPointAdapter adapter = new WayPointAdapter(this, R.layout.way_point_list_item, markers);
            Singleton.get().setWayPointAdapter(adapter);
            lv_wayPoints.setAdapter(adapter);

        } catch (Exception e) {
            System.out.println("NIE WYSZŁO ZROBIENIE ADAPTERA");
        }
    }

    private void prepareList(List<CustomMarker> markers){
        if(!markers.isEmpty()){
            markers.clear();
        }
        for(int i=0;i<myApplication.getWayPoints().size();i++){
            try {
                markers.add(myApplication.getWayPoint(i));
            } catch(NullPointerException npe) {
                System.out.println("Wyjątek, lista jest pusta");
            }

        }
    }
}