package com.example.backend.controller;

import com.example.backend.model.Album;
import com.example.backend.model.Track;
import com.example.backend.model.TrackOfAlbum;
import com.example.backend.repository.AlbumRepository;
import com.example.backend.repository.TrackOfAlbumRepository;
import com.example.backend.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/")
public class TrackController {

    @Autowired
    private TrackRepository trackRepository;
    @Autowired
    private TrackOfAlbumRepository trackOfAlbumRepository;

//    private TrackOfAlbumRepository trackOfAlbumRepository;


    @GetMapping("track")
    public List<Track> getTrack() {
        return this.trackRepository.findAll();
    }

    @GetMapping("track/{track_id}")
    public Track getTrackId(@PathVariable long track_id) {
//        List<Track> listOfTracks = this.trackRepository.findAll();
//        for(int i=0; i<listOfTracks.size(); i++){
//            if(listOfTracks.get(i).getTrack_id() == track_id){
//                return listOfTracks.get(i);
//
//            }
//        }
//        return null;
        return trackRepository.getTrackById(track_id);
    }

    @GetMapping("tracks/{album_id}")
    public List<Track> getTracksOfAlbums(@PathVariable long album_id) {
        List<Track> lista = trackRepository.getAlbumTracks(album_id);
//        for (int i=0;i<lista.size();i++) System.out.println(lista.get(i).getName());
        return lista;
//            List<TrackOfAlbum> listOfRelations = this.trackOfAlbumRepository.findAll();
//            List<Long> listOfTrackId = new ArrayList<>();
//            for(int i=0; i<listOfRelations.size(); i++){
//                if(album_id == listOfRelations.get(i).getAlbum()){
//                    listOfTrackId.add(listOfRelations.get(i).getTrack());
//                }
//            }
//            List<Track> finalTrackList = new ArrayList<>();
//            for(int i=0; i<listOfTrackId.size(); i++) {
//                finalTrackList.add(trackRepository.getById(listOfTrackId.get(i)));
//            }
//        for(int i=0; i<finalTrackList.size(); i++) {
//            System.out.println(finalTrackList.get(i).getName() + " Id tracku który powinno zwrócić");
//        }
//            return finalTrackList;
    }

    @PostMapping("track")
    public Track trackAdd(Track track) {
        return trackRepository.save(track);
    }

    @DeleteMapping("track/{track_id}")
    public String deleteTrackByID(long track_id) {
//        trackRepository.removeTrackFromAlbum(track_id);
//        trackRepository.removeTrackById(track_id);
        TrackOfAlbum toar = trackOfAlbumRepository.getByTrackId(track_id);
        trackOfAlbumRepository.delete(toar);
        trackRepository.deleteById(track_id);
        return "usunieto";
    }

//    @PostMapping("track/{title}, {author}")
//    public String deleteTrackByTitleAndAuthor(String title, String author){
//       Long id = trackRepository.getTrackIdByTitleAndAuthor(title, author);
//       System.out.println(id + " to id");
//       trackRepository.deleteById(id);
//
//       return "usunieto";
//    }

//    @PostMapping("track/{track_id}")
//    public String UpdateTrackByID(long track_id) {
//
//        trackRepository
//        return "usunieto";
//    }

    @PutMapping("track/update/{track_id}")
    public void updateTrackGenre(long id, String newGenre, String newAuthor, String newName) {
        Track track = trackRepository.findAllById(Collections.singleton(id)).get(0);
        track.setGenre(newGenre);
        track.setAuthor(newAuthor);
        track.setName(newName);
        trackRepository.save(track);
    }
}
