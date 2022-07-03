package com.example.mobilenavigation;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mobilenavigation.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private List<Location> savedLocation;
//    private Integer markerCounter = 0;
//    private List<CustomMarker> markers;
    private MyApplication myApplication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(Singleton.get().wayPoints != null)
        Singleton.get().wayPoints.clear();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        myApplication = (MyApplication)getApplicationContext();
        savedLocation = myApplication.getMyLocations();


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        System.out.println("Powinno robić marker");
        showWayPoints();


        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(52.237049, 21.017532);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        mMap.setOnMapLongClickListener(latLng -> {
            Integer counter = myApplication.getWayPoints().size() +1;
            String title = "Cel podróży nr: " + counter.toString();
//            Geocoder geocoder = new Geocoder(getApplicationContext());

//            List<Address> newAddresses = new ArrayList<>();
//            String newAddress ="";

//            try {
//                newAddresses = Singleton.get().getGeocoder().getFromLocation(latLng.latitude, latLng.longitude, 1);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                newAddress = newAddresses.get(0).getAddressLine(0);
//            } catch(NullPointerException | IndexOutOfBoundsException e){
//                System.out.println("Nie zrobiło adresu w znacznikach!");
//            }

            CustomMarker marker = new CustomMarker(latLng, title, counter);

            mMap.addMarker(new MarkerOptions().position(latLng).title(title));
            myApplication = (MyApplication)getApplicationContext();
            myApplication.addDestination(marker);
            Singleton.get().wayPoints.add(marker);


            // Nie wiem czy trzeba Context

            System.out.println(myApplication.getWayPoints().toString() + " LISTA MOICH PUNKTÓW");

//            for (int i = 0; i < markers.size(); i++) {
//                try {
//                    System.out.println("Marker nr " + i + " " + markers.get(i).getTitle());
//                }catch(Exception e){
//                    System.out.println("NIE MA W BAZIE MARKERKA TAKIEGO");
//                }
//            }
        });

    }

    private void showPoint()
    {
        try{
        myApplication = (MyApplication)getApplicationContext();
        LatLng position = myApplication.getWayPoint(0).getPosition();
        String text = myApplication.getWayPoint(0).getTitle();
        mMap.addMarker(new MarkerOptions().position(position).title(text));} catch (NullPointerException e ){
            System.out.println(e.getMessage() + " PROBLEMIK MAM");
            System.out.println(mMap.getMapType() + " MOJA MAPA");
        } catch (IndexOutOfBoundsException ie){
            System.out.println(" Zły index bo pierwsze przejście");
        }


    }

    private void showWayPoints(){
        int i;
        System.out.println(myApplication.getWayPoints().size() + " TAKI JEST ROZMIAR LISTY MARKERÓW");
        for(i=0;i<myApplication.getWayPoints().size();i++){
            System.out.println("Tworzy marker????????????????");
            try {
                myApplication = (MyApplication)getApplicationContext();
                LatLng position = myApplication.getWayPoint(i).getPosition();
                String text = myApplication.getWayPoint(i).getTitle();
                mMap.addMarker(new MarkerOptions().position(position).title(text));
            }catch (NullPointerException e){
                System.out.println("ŁAPIE WYJĄTEK W NADAWANIU NOWYCH ZNACZNIKÓW");
                System.out.println(myApplication.getWayPoint(i).getPosition().toString() + " <- pozycja " + myApplication.getWayPoint(i).getTitle() + " <= Tytuł");
            }
        }
    }
}