package com.example.mobilenavigation;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int DEFAULT_UPDATE_INTERVAL = 300;
    private static final int FAST_UPDATE_INTERVAL = 50;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private static final int SIGN_IN = 1;


    // Definicje obiektów UI
    private TextView tv_lat, tv_lon, tv_altitude, tv_accuracy, tv_speed, tv_sensor,
            tv_updates, tv_address, tv_distance, tv_destination, tv_logged, tv_wayPointListAddress,
            tv_wayPointListDistance, tv_wayPointNumber;
    private Switch sw_locationsupdates, sw_gps;
    private Button btn_newWayPoint, btn_showWayPointList, btn_showMap, btn_login, btn_showAlbums;
    private EditText pt_login, pas_password;
    private ImageView iv_wayPointPin;
    private ListView lv_wayPoints;


    // API od Googla zapewniające usługi lokalizacji
    FusedLocationProviderClient fusedLocationProviderClient;

    // Obecna lokalizacja
    private Location currentLocation;

    // Lista zapisanych lokalizacji
    private List<Location> savedLocations;

    // Zmienna dotycząca tego, czy śledzimy na bieżąco lokalizację, czy nie
    private boolean updateOn = false;

    // Służy do obsługi zapytań co do lokalizacji
    LocationRequest locationRequest;
    LocationCallback locationCallBack;

    // Aplikacja, przydatna
    private MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Przypisanie UI id do zmiennych
        tv_lat = findViewById(R.id.tv_lat);
        tv_accuracy = findViewById(R.id.tv_accuracy);
        tv_address = findViewById(R.id.tv_address);
        tv_altitude = findViewById(R.id.tv_altitude);
        tv_lon = findViewById(R.id.tv_lon);
        tv_sensor = findViewById(R.id.tv_sensor);
        tv_distance = findViewById(R.id.tv_distance);
        tv_updates = findViewById(R.id.tv_updates);
        tv_destination = findViewById(R.id.tv_destination);
        tv_logged = findViewById(R.id.tw_logged);
        sw_gps = findViewById(R.id.sw_gps);
        sw_locationsupdates = findViewById(R.id.sw_locationsupdates);
        btn_showWayPointList = findViewById(R.id.btn_showWayPointList);
        btn_showMap = findViewById(R.id.btn_showMap);
        btn_login = findViewById(R.id.btn_login);
        btn_showAlbums = findViewById(R.id.btn_showAlbums);
        pt_login = findViewById(R.id.pt_login);
        pas_password = findViewById(R.id.pas_password);

        // Ustawienie właściwości LocationRequest
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        // Event, który jest wywoływany co każdy cykl sprawdzania lokalizacji
        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // Zapis lokalizacji
                updateUIValues(locationResult.getLastLocation());
            }
        };

        btn_showWayPointList.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ShowSavedLocationsList.class);
            startActivity(intent);
        });

        btn_showMap.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
        });

        btn_showAlbums.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AlbumsActivity.class);
            startActivity(intent);
        });

        btn_login.setOnClickListener(v -> {
            //TODO Sprawdzenie danych logowania w bazie danych // Obsługa logowania
        });

        sw_gps.setOnClickListener(v -> setRequestPriority());

        sw_locationsupdates.setOnClickListener(v -> {
            if (sw_locationsupdates.isChecked()) {
                // Włącz śledzenie lokalizacji
                startLocationUpdates();
            } else {
                // Wyłącz śledzenie lokalizacji
                stopLocationUpdates();
            }
        });

        myApplication = (MyApplication)getApplicationContext();
        updateGPS();
    } // Koniec metody onCreate()

//    @Override
//    protected void onStart() {
//        super.onStart();
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        updateUserInfo(account);
//    }

    @SuppressLint("SetTextI18n")
    private void stopLocationUpdates() {
        // Wyłączam odbieranie requestów GPS
        locationRequest.setPriority(LocationRequest.PRIORITY_NO_POWER);

        // Zmieniam wartości UI
        tv_updates.setText("Wyłączone");
        tv_lat.setText("Niedostępne");
        tv_lon.setText("Niedostępne");
        tv_distance.setText("Niedostępne");
        tv_address.setText("Niedostępne");
        tv_accuracy.setText("Niedostępne");
        tv_altitude.setText("Niedostępne");
        tv_sensor.setText("Niedostępne");

        fusedLocationProviderClient.removeLocationUpdates(locationCallBack);
    }

    @SuppressLint("SetTextI18n")
    private void startLocationUpdates() {
        tv_updates.setText("Włączone");
        setRequestPriority();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
        updateGPS();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sw_gps.setChecked(true);
                sw_locationsupdates.setChecked(true);
                updateGPS();
            } else {
                Toast.makeText(this, "Aplikacja wymaga zgody użytkownika na użycie lokalizacji urządzenia", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void updateGPS(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Zgoda udzielona
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
                // Wyświetl aktualną lokalizację na UI
                updateUIValues(location);
                currentLocation = location;
            });
        } else {
            // Zgoda nie została udzielona
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateUIValues(Location location) {
        // Aktualizacja wszystkich elementów UI wyświetlających tekst przez nową lokację
        try{
            tv_lat.setText(String.valueOf(location.getLatitude()));
        }catch(NullPointerException e){
            System.out.println("NULL POINTER W UPDATE UI");
        }
        try{
            tv_lon.setText(String.valueOf(location.getLongitude()));
        }catch(NullPointerException e){
            System.out.println("NULL POINTER W UPDATE UI");
        }
        try{
            tv_accuracy.setText(String.valueOf(location.getAccuracy()));
        }catch(NullPointerException e){
            System.out.println("NULL POINTER W UPDATE UI");
        }

        try{
            if (location.hasAltitude()) {
                tv_altitude.setText(String.valueOf(location.getAltitude()));
            } else {
                tv_altitude.setText("Niedostępne");
            }
        }catch(NullPointerException e){
            System.out.println("NULL POINTER W UPDATE UI");
        }

        if(myApplication.getWayPoints().size()!=0 && (tv_distance.getText() == "Niedostępne" || tv_distance.getText() ==  "0.0" || tv_destination.getText() != "Nie ustawiono znacznika Celu")) {
            try {
                if (sw_locationsupdates.isChecked()) {
                    java.text.DecimalFormat df=new java.text.DecimalFormat();
                    df.setMaximumFractionDigits(2);
                    tv_distance.setText(df.format(calculateDistance()) + " km");
                } else {
                    tv_distance.setText("Niedostępne");
                }
            } catch (NullPointerException e) {
                System.out.println("PRZY LICZENIU DYSTANSU SIĘ WYWALIŁO");
            }
        }

        Geocoder geocoder = new Geocoder(MainActivity.this);
//        Singleton.get().setGeocoder(geocoder);

        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            tv_address.setText(addresses.get(0).getAddressLine(0));
        } catch (IndexOutOfBoundsException | IOException  | NullPointerException e) {
            tv_address.setText("Nie można znaleźć adresu");
        }
        try{
            List<Address> addresses = geocoder.getFromLocation(myApplication.getWayPoint(0).getPosition().latitude, myApplication.getWayPoint(0).getPosition().longitude, 1);
            tv_destination.setText(addresses.get(0).getAddressLine(0));
        }catch(NullPointerException | IOException  | IndexOutOfBoundsException e){
            System.out.println("Jest problem z adresem celu");
            tv_destination.setText("Nie ustawiono znacznika Celu");
        }

        MyApplication myApplication = (MyApplication)getApplicationContext();
        savedLocations = myApplication.getMyLocations();

        // Pokaż liczbę zapisanych WayPointów
//        tv_wayPointCounts.setText(savedLocations.size() + "");
    }

//    private void updateUserInfo(GoogleSignInAccount account){
//        try{
//        tv_logged.setText("Zalogowano, jako:" + account.getDisplayName());}
//        catch (NullPointerException e){
//            System.out.println("Nie ma zalogowanego typa");
//        }
//        btn_login.setEnabled(false);
//        pas_password.setEnabled(false);
//        pt_email.setEnabled(false);
//    }

    @SuppressLint("SetTextI18n")
    private void setRequestPriority(){
        if (sw_gps.isChecked()) {
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            tv_sensor.setText("GPS w użyciu");
        } else {
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            tv_sensor.setText("Nawigacja niedokładna, GPS wyłączony");
        }
    }

    private double calculateDistance(){
        if(myApplication.getWayPoints().size()>0) {
            double x1 = myApplication.getWayPoint(0).getPosition().latitude;
            double y1 = myApplication.getWayPoint(0).getPosition().longitude;
            double x2 = currentLocation.getLatitude();
            double y2 = currentLocation.getLongitude();
            double result = (Math.sqrt( Math.pow((x2-x1),2) + Math.pow((Math.cos((x1*Math.PI)/180) * Math.pow((y2-y1),2)),2)) * (40075.704/360));
            return result;
        } else {
            return 0.0;
        }
    }

//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == SIGN_IN){
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            if(result.isSuccess()){
//                tv_logged.setText("Zalogowano jako jakiś user");
//
//            } else{
//                Toast.makeText(this, "Nie udało się zalogować", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }
}