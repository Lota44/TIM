package com.example.backend.controller;

import com.example.backend.model.Album;
import com.example.backend.model.Track;
import com.example.backend.model.TrackOfAlbum;
import com.example.backend.repository.AlbumRepository;
import com.example.backend.repository.TrackOfAlbumRepository;
import com.example.backend.repository.TrackRepository;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/")
public class TrackOfAlbumController {

    @Autowired
    private TrackOfAlbumRepository trackOfAlbumRepository;
    @Autowired
    private TrackRepository trackRepository;

    @GetMapping("track_of_album")
    public List<TrackOfAlbum> getTracksOfAlbums(){
        return this.trackOfAlbumRepository.findAll();
    }

//    @GetMapping("track_of_album/{album_id}")
//    public List<Track> getTracksOfAlbums(@PathVariable long album_id){
//        List<Track> lista = trackOfAlbumRepository.getAlbumTracks(album_id);
//        for (int i=0;i<lista.size();i++) System.out.println(lista.get(i).getName());
//        return lista;
////            List<TrackOfAlbum> listOfRelations = this.trackOfAlbumRepository.findAll();
////            List<Long> listOfTrackId = new ArrayList<>();
////            for(int i=0; i<listOfRelations.size(); i++){
////                if(album_id == listOfRelations.get(i).getAlbum()){
////                    listOfTrackId.add(listOfRelations.get(i).getTrack());
////                }
////            }
////            List<Track> finalTrackList = new ArrayList<>();
////            for(int i=0; i<listOfTrackId.size(); i++) {
////                finalTrackList.add(trackRepository.getById(listOfTrackId.get(i)));
////            }
////        for(int i=0; i<finalTrackList.size(); i++) {
////            System.out.println(finalTrackList.get(i).getName() + " Id tracku który powinno zwrócić");
////        }
////            return finalTrackList;
//    }

    @PostMapping("track_of_album")
    public TrackOfAlbum trackOfAlbumAdd(TrackOfAlbum trackOfAlbum)
    {
        return trackOfAlbumRepository.save(trackOfAlbum);
    }

}
