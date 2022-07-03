package com.example.mobilenavigation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class AlbumAdapter extends ArrayAdapter<JSONArray> {

    JSONArray array;
    Button btn_deleteAlbum, btn_updateAlbum;
    AlbumsActivity activity;
    MyApplication myApplication;

    public AlbumAdapter(@NonNull Context context, int resource, JSONArray jsonArray, AlbumsActivity activity, MyApplication application) {
        super(context, resource, jsonArray);
        array = jsonArray;
        this.activity = activity;
        myApplication = application;
    }

    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.album_layout, parent, false);
        }

        TextView tv_author = convertView.findViewById(R.id.tv_author);
        TextView tv_title = convertView.findViewById(R.id.tv_title);
        TextView tv_genre = convertView.findViewById(R.id.tv_genre);
        TextView tv_date = convertView.findViewById(R.id.tv_date);
        btn_deleteAlbum = convertView.findViewById(R.id.btn_deleteAlbum);
        btn_updateAlbum = convertView.findViewById(R.id.btn_updateAlbum);

        final JSONObject[] object = {(JSONObject) array.get(position)};

        try{
        tv_author.setText(object[0].get("author").toString());}catch (Exception e){
            tv_author.setText("");
        }
        try{
        tv_title.setText(object[0].get("name").toString());} catch(Exception e){
            tv_title.setText("");
        }
        try{
        tv_genre.setText(object[0].get("genre").toString());} catch (Exception e){
            tv_genre.setText("");
        }
        try{
        tv_date.setText(object[0].get("release_date").toString());} catch (Exception e){
            tv_date.setText("");
        }
        btn_deleteAlbum.setOnClickListener(v -> {
             object[0] = (JSONObject) array.get(position);
            try {
                URL url = new URL("http://172.23.188.28:8080/api/?albumId=" + object[0].get("album_id"));
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("DELETE");
                con.setUseCaches(false);
                con.connect();
                int responseCode = 0;
                responseCode = con.getResponseCode();
                if (responseCode != 200) {
                    System.out.println("BŁĄD BYŁ");
                } else {
                    System.out.println("NO NIBY USUNĘŁO");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                activity.getAlbums();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        btn_updateAlbum.setOnClickListener(v -> {
            Intent intent = new Intent(activity, UpdateAlbumActivity.class);
            activity.startActivity(intent);
            object[0] = (JSONObject) array.get(position);
            long newId = (long) Integer.parseInt(String.valueOf(object[0].get("album_id")));
            String oldName = (String) object[0].get("name");
            String oldGenre = (String) object[0].get("genre");
            String oldAuthor = (String) object[0].get("author");
            myApplication.setId(newId);
            myApplication.setOldAuthor(oldAuthor);
            myApplication.setOldGenre(oldGenre);
            myApplication.setOldName(oldName);
        });

        return convertView;
    }
}
