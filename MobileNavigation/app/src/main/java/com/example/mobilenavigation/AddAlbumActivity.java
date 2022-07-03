package com.example.mobilenavigation;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AddAlbumActivity extends AppCompatActivity {

    private MyApplication myApplication;
    private Button btn_confirmAddAlbum;
    private EditText ptxt_title, ptxt_genre, ptxt_author;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_album);

        myApplication = (MyApplication)getApplicationContext();

        btn_confirmAddAlbum = findViewById(R.id.btn_confirmAddAlbum);
        ptxt_author = findViewById(R.id.ptxt_addAlbumAuthor);
        ptxt_genre = findViewById(R.id.ptxt_addAlbumGenre);
        ptxt_title = findViewById(R.id.ptxt_addAlbumTitle);

        btn_confirmAddAlbum.setOnClickListener(v -> {
            try {
                addNewAlbum();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(AddAlbumActivity.this ,  "Dodano " + ptxt_title.getText() + " " + ptxt_author.getText() + " " + ptxt_genre.getText(), Toast.LENGTH_LONG).show();
        });

    }

    public void addNewAlbum() throws IOException {
        URL url = new URL("http://172.23.188.28:8080/api/music_album/");
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("name", ptxt_title.getText());
        params.put("author", ptxt_author.getText());
        params.put("genre", ptxt_genre.getText());

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param:params.entrySet()){
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn= (HttpURLConnection) url.openConnection();
        conn.setRequestMethod( "POST" );
        conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty( "Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput( true );
        conn.getOutputStream().write(postDataBytes);
        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

        StringBuilder sb = new StringBuilder();
        for(int c; (c = in.read()) >= 0;)
            sb.append((char)c);
        String response = sb.toString();
        System.out.println(response);
    }
}
