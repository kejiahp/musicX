package com.kejiahp.musicx.seed;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kejiahp.musicx.apps.album.AlbumModel;
import com.kejiahp.musicx.apps.album.AlbumRepository;
import com.kejiahp.musicx.apps.artist.ArtistModel;
import com.kejiahp.musicx.apps.artist.ArtistRepository;
import com.kejiahp.musicx.apps.genre.GenreModel;
import com.kejiahp.musicx.apps.genre.GenreRepository;
import com.kejiahp.musicx.apps.song.SongModel;
import com.kejiahp.musicx.apps.song.SongRepository;

@Service
public class SeedDBService {
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private DBDataService dbDataService;

    String[] genres = { "Pop", "Hip-Hop", "Rock", "EDM (Electronic Dance Music)", "Country", "R&B (Rhythm & Blues)",
            "Indie Rock", "Alternative Rock", "Jazz", "Metal", "Reggaeton", "Latin Pop", "K-Pop (Korean Pop)",
            "Folk", "Classical", "Blues", "Dancehall", "Funk", "Soul", "Punk Rock"
    };

    @Transactional
    private void seedArtists() {
        Set<String> artistNames = dbDataService.artistAlbum.keySet();
        List<ArtistModel> allArtists = new ArrayList<>();
        for (String artistName : artistNames) {
            ArtistModel artist = new ArtistModel();
            artist.setName(artistName);
            allArtists.add(artist);
        }
        this.artistRepository.saveAll(allArtists);
    }

    @Transactional
    private void seedGenres() {
        List<GenreModel> allGenres = new ArrayList<>();
        for (String genreName : genres) {
            GenreModel genre = new GenreModel();
            genre.setName(genreName);
            allGenres.add(genre);
        }
        this.genreRepository.saveAll(allGenres);
    }

    @Transactional
    private void seedAlbumAndSongs() throws ArtistNotFoundException, GenreNotFoundException {
        for (String artistName : dbDataService.artistAlbum.keySet()) {
            var artistAlbumDetail = dbDataService.artistAlbum.get(artistName);

            // Fetch the artist
            ArtistModel artist = artistRepository.findByName(artistName).orElseThrow(() -> new ArtistNotFoundException(
                    "[seedAlbumAndSongs() -> artistRepository.findByName()] Artist not found."));

            // Create the album
            AlbumModel album = new AlbumModel();
            album.setTitle(artistAlbumDetail.albumName());
            album.setCoverImage(artistAlbumDetail.coverImage());
            album.setArtist(artist);
            album = albumRepository.save(album);

            // Create the albums Songs
            List<SongModel> allAlbumSongs = new ArrayList<>();

            for (var songData : artistAlbumDetail.songList()) {
                SongModel song = new SongModel();
                song.setAlbum(album);
                song.setArtist(artist);
                song.setTitle(songData.title());
                song.setUrl(songData.url());
                song.setDurationSeconds(songData.duration());

                GenreModel genre = genreRepository.findByName(genres[new Random().nextInt(genres.length)])
                        .orElseThrow(() -> new GenreNotFoundException(
                                "[seedAlbumAndSongs() -> genreRepository.findByName()] Genre not found."));

                song.getGenres().add(genre);
                allAlbumSongs.add(song);
            }
            songRepository.saveAll(allAlbumSongs);
        }
    }

    public void seedAll() throws ArtistNotFoundException, GenreNotFoundException {

        // Check if the `sleeptoken` artist exists, If not attempt seeding else don't
        // attempt seeding.
        Optional<ArtistModel> sleepTokenArtist = artistRepository.findByName("SleepToken");

        if (sleepTokenArtist.isPresent()) {
            return;
        }

        seedArtists();
        seedGenres();
        seedAlbumAndSongs();
    }

    private class ArtistNotFoundException extends Exception {
        public ArtistNotFoundException(String message) {
            super(message);
        }
    }

    private class GenreNotFoundException extends Exception {
        public GenreNotFoundException(String message) {
            super(message);
        }
    }
}
