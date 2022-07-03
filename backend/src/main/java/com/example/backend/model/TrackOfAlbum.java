package com.example.backend.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;


//DTOS
@Entity
@Getter
@Setter
@Table(name = "track_of_album")
public class TrackOfAlbum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "track", nullable = false)
    private long track;
    @Column(name = "album", nullable = false)
    private long album;

    public TrackOfAlbum(){ }

    public TrackOfAlbum(long track, long album){
        super();
        this.track = track;
        this.album = album;
    }
}
