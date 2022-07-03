package com.example.backend.repository;

import com.example.backend.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {

    @Query(value = "SELECT * FROM track as t LEFT JOIN track_of_album as toa ON toa.track = t.track_id WHERE toa.album = ?1", nativeQuery = true)
    List<Track> getAlbumTracks(@Param("album_id") long album_id);

    @Query(value = "SELECT * FROM track WHERE track_id = ?1", nativeQuery = true)
    Track getTrackById(@Param("id") long id);

//    @Query(value = "DELETE FROM track WHERE track_id = ?1", nativeQuery = true)
//    void removeTrackById(@Param("id") long id);
//
//    @Query(value = "DELETE FROM track_of_album WHERE track = ?1", nativeQuery = true)
//    void removeTrackFromAlbum(@Param("id") long id);

//    @Query(value = "SELECT track_id FROM track WHERE name = '?1' AND author = '?2' ", nativeQuery = true)
//    Long getTrackIdByTitleAndAuthor(@Param("title") String title, @Param("author") String author);

}
