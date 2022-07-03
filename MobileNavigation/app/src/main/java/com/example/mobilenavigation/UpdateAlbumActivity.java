package com.example.mobilenavigation;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class UpdateAlbumActivity extends AppCompatActivity {
    private MyApplication myApplication;
    private Button btn_confirmUpdateAlbum;
    private EditText ptxt_title, ptxt_genre, ptxt_author;
    private long id;
    private String oldName, oldGenre, oldAuthor;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_album);

        myApplication = (MyApplication)getApplicationContext();
        id = myApplication.getId();
        oldAuthor = myApplication.getOldAuthor();
        oldGenre = myApplication.getOldGenre();
        oldName = myApplication.getOldName();



        btn_confirmUpdateAlbum = findViewById(R.id.btn_confirmUpdateAlbum);
        ptxt_author = findViewById(R.id.ptxt_updateAlbumAuthor);
        ptxt_genre = findViewById(R.id.ptxt_updateAlbumGenre);
        ptxt_title = findViewById(R.id.ptxt_updateAlbumTitle);
        ptxt_author.setText(oldAuthor);
        ptxt_genre.setText(oldGenre);
        ptxt_title.setText(oldName);

        btn_confirmUpdateAlbum.setOnClickListener(v -> {
            try {
                updateAlbum();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(UpdateAlbumActivity.this ,  "Zaktualizowano " + ptxt_title.getText() + " " + ptxt_author.getText() + " " + ptxt_genre.getText(), Toast.LENGTH_LONG).show();
        });

    }

    private void updateAlbum() throws IOException {
        URL url = new URL("http://172.23.188.28:8080/api/music_album/update/");

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("id", id);
        params.put("newName", ptxt_title.getText());
        params.put("newAuthor", ptxt_author.getText());
        params.put("newGenre", ptxt_genre.getText());

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param:params.entrySet()){
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn= (HttpURLConnection) url.openConnection();
        conn.setRequestMethod( "PUT" );
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
