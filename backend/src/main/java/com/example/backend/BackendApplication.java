package com.example.backend;

import com.example.backend.model.Album;
import com.example.backend.repository.AlbumRepository;
import com.example.backend.repository.TrackOfAlbumRepository;
import com.example.backend.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.sql.Date;

@EnableSwagger2
@SpringBootApplication
public class BackendApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Autowired
	private AlbumRepository albumRepository;
	private TrackOfAlbumRepository trackOfAlbumRepository;
	private TrackRepository trackRepository;

	@Override
	public void run(String... args) throws Exception {
//		this.albumRepository.save(new Album("NothingBack", "Michael Jordan", "Rock and Roll", new Date(2003, 11, 22)));
//		this.albumRepository.save(new Album("NothingBack", "Michael Jordan", "Rock and Roll", new Date(1998, 2, 15)));
	}
}

