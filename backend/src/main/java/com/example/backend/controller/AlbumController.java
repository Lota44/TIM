package com.example.backend.controller;


import com.example.backend.model.Album;
import com.example.backend.model.Track;
import com.example.backend.model.TrackOfAlbum;
import com.example.backend.repository.AlbumRepository;
import com.example.backend.repository.TrackOfAlbumRepository;
import com.example.backend.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/")
public class AlbumController {

    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private TrackRepository trackRepository;
    @Autowired
    private TrackOfAlbumRepository trackOfAlbumRepository;


    @GetMapping("music_album")
    public List<Album> getMusicAlbums(){

        System.out.println("TEST");
        return this.albumRepository.findAll();
    }

    @PostMapping("music_album")
    public Album albumAdd(Album album)
    {
        return albumRepository.save(album);
    }

    @DeleteMapping
    public String removeAlbum(long albumId){
        try {
            List<Track> tracks = trackRepository.getAlbumTracks(albumId); //Lista tracków, które mają ten album
//            System.out.println(tracks.size() + " ROZMIAR TRAKÓW");
            for (int i = 0; i < tracks.size(); i++) {
                TrackOfAlbum tmp = trackOfAlbumRepository.getByTrackId(tracks.get(i).getTrack_id());
                trackOfAlbumRepository.delete(tmp);
            }
            albumRepository.deleteById(albumId);
            return "usuwanie albumu, album zawierał elementy";
        } catch (NullPointerException npe){
            albumRepository.deleteById(albumId);
            return "NULL POINTER W USUWANIU ALBUMU, album jest pusty";
        }
    }


    @PutMapping("music_album/update")
    public void updateAlbumGenre(long id, String newGenre, String newAuthor, String newName) {
        Album album = albumRepository.findAllById(Collections.singleton(id)).get(0);
        album.setGenre(newGenre);
        album.setAuthor(newAuthor);
        album.setName(newName);
        albumRepository.save(album);
    }
}
