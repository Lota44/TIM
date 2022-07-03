package com.example.backend.repository;

import com.example.backend.model.Track;
import lombok.Value;

import java.sql.Date;

public interface TrackType {

    Long getTrack_Id();
    String getName();
    String getAuthor();
    String getGenre();
    Date getReleaseDate();
}
