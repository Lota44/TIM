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
@Table(name = "track")
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_id")
    private long track_id;
    private String name;
    private String author;
    private String genre;
    private Date release_date;

    public Track(){ }

    public Track(String name, String author, String genre, Date releaseDate){
        super();
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.release_date = releaseDate;
    }

    @OneToMany
    @JoinColumn(name = "track")
    public List<TrackOfAlbum> trackOfAlbum;
}
