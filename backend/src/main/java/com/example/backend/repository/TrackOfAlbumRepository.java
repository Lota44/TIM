package com.example.backend.repository;

import com.example.backend.model.Track;
import com.example.backend.model.TrackOfAlbum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackOfAlbumRepository extends JpaRepository<TrackOfAlbum, Long> {
//    @Query(value = "SELECT * FROM track t LEFT JOIN track_of_album toa ON toa.track = t.track_id WHERE toa.album = ?1", nativeQuery = true)
//    List<Track> getAlbumTracks(@Param("album_id") long album_id);
    @Query(value = "SELECT * FROM track_of_album toa WHERE track = ?1", nativeQuery = true)
    TrackOfAlbum getByTrackId(@Param("track_id") long track_id);
}