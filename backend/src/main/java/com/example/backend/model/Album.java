package com.example.backend.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

//DTOS
@Entity
@Getter
@Setter
@Table(name = "music_album")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private long album_id;
    private String name;
    private String author;
    private String genre;

    @Column(name = "release_date")
    private Date release_date;

    public Album(){ }

    public Album(String name, String author, String genre, String releaseDate){
        super();
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.release_date = Date.valueOf(releaseDate);
    }

    @OneToMany
    @JoinColumn(name = "album")
    public List<TrackOfAlbum> trackOfAlbum;
}
