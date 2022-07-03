package com.example.mobilenavigation;


import android.content.Intent;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;
import android.view.View.OnClickListener;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AlbumsActivity extends AppCompatActivity {

    private MyApplication myApplication;
    private Button btn_addAlbum;
    private ListView lv_albumsListView;
    private JSONArray array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);

        myApplication = (MyApplication)getApplicationContext();

        btn_addAlbum = findViewById(R.id.btn_addNewAlbum);
        lv_albumsListView = findViewById(R.id.lv_albumsListView);

        try {
            getAlbums();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        lv_albumsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                try{
                JSONObject object = (JSONObject) array.get(position);
//                Toast.makeText(AlbumsActivity.this, object + "", Toast.LENGTH_LONG).show();
//                } catch (NullPointerException npe){
//                    System.out.println("Błąd");
//                }

//                URL url = null;
//                try {
//                    url = new URL("http://172.23.188.28:8080/api/?albumId=" + object.get("album_id"));
//                    System.out.println("Weszło 1");
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                    Toast.makeText(AlbumsActivity.this, "NIE WYKONANO", Toast.LENGTH_LONG).show();
//                }
//                HttpURLConnection con = null;
//                try {
//                    assert url != null;
//                    con = (HttpURLConnection)url.openConnection();
//                    System.out.println("Weszło 2");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(AlbumsActivity.this, "NIE WYKONANO", Toast.LENGTH_LONG).show();
//                }
//                try {
//                    assert con != null;
//                    con.setRequestMethod("DELETE");
//                    System.out.println("Weszło 3");
//                } catch (ProtocolException e) {
//                    e.printStackTrace();
//                    Toast.makeText(AlbumsActivity.this, "NIE WYKONANO", Toast.LENGTH_LONG).show();
//                }
//                con.setUseCaches(false);
//                try {
//                    con.connect();
//                    System.out.println("Weszło 4");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(AlbumsActivity.this, "NIE WYKONANO", Toast.LENGTH_LONG).show();
//                }
//                int responseCode = 0;
//                try {
//                    responseCode = con.getResponseCode();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                if(responseCode != 200){
//                    System.out.println("BŁĄD BYŁ");
//                } else{
//                    System.out.println("NO NIBY KURWA USUNĘŁO");
//                }
//
//
//                try {
//                    getAlbums();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
            }
        });

        btn_addAlbum.setOnClickListener(v -> {
            Intent intent = new Intent(AlbumsActivity.this, AddAlbumActivity.class);
            startActivity(intent);
        });

    }



    public void getAlbums() throws IOException, ParseException, NetworkOnMainThreadException {
        URL url = new URL("http://172.23.188.28:8080/api/music_album/");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();

        if (responseCode != 200){
            throw new RuntimeException("HttpResponseCode = " + responseCode);
        } else {
            StringBuilder informationString = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()){
                informationString.append(scanner.nextLine());
            }
            scanner.close();
//            Toast.makeText(AlbumsActivity.this ,informationString, Toast.LENGTH_LONG).show();

            JSONParser parser = new JSONParser();
            array = (JSONArray) parser.parse(String.valueOf(informationString));

            Toast.makeText(AlbumsActivity.this ,array.size() + "", Toast.LENGTH_LONG).show();

            ArrayAdapter<JSONArray> adapter = new AlbumAdapter(this, R.layout.album_layout, array, AlbumsActivity.this, myApplication);
            lv_albumsListView.setAdapter(adapter);
            connection.disconnect();
        }
    }
}
