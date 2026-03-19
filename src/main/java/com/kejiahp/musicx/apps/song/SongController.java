package com.kejiahp.musicx.apps.song;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.kejiahp.musicx.apps.album.AlbumRepository;
import com.kejiahp.musicx.apps.artist.ArtistRepository;
import com.kejiahp.musicx.util.ApiResponse;
import com.kejiahp.musicx.util.IsAuthUserService;
import com.kejiahp.musicx.util.exceptions.authorization.UnauthorizedException;
import com.kejiahp.musicx.util.exceptions.validation.RecordNotFound;

@Controller
public class SongController {
    @Autowired
    private SongRepository songRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private IsAuthUserService isAuthUserService;

    @QueryMapping
    public List<SongModel> songs() {
        if (!isAuthUserService.isUserAuthenticated()) {
            throw new UnauthorizedException();
        }
        return songRepository.findAll();
    }

    @QueryMapping
    public SongModel song(@Argument UUID id) {
        if (!isAuthUserService.isUserAuthenticated()) {
            throw new UnauthorizedException();
        }

        return songRepository.findById(id).orElse(null);
    }

    @MutationMapping
    public ApiResponse<SongModel> createSong(
            @Argument String title,
            @Argument UUID artistId,
            @Argument UUID albumId,
            @Argument String url,
            @Argument Integer durationSeconds) {

        if (!isAuthUserService.isUserAuthenticated()) {
            throw new UnauthorizedException();
        }

        var artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new RecordNotFound("Artist not found", "artistId"));

        SongModel song = new SongModel();
        song.setTitle(title);
        song.setArtist(artist);
        song.setUrl(url);
        song.setDurationSeconds(durationSeconds);

        if (albumId != null) {
            var album = albumRepository.findById(albumId)
                    .orElseThrow(() -> new RecordNotFound("Album not found", "albumId"));
            song.setAlbum(album);
        }

        SongModel savedSong = songRepository.save(song);
        return new ApiResponse<>(true, "Song created successfully", savedSong);
    }

    @MutationMapping
    public ApiResponse<SongModel> updateSong(
            @Argument UUID id,
            @Argument String title,
            @Argument UUID artistId,
            @Argument UUID albumId,
            @Argument String url,
            @Argument Integer durationSeconds) {

        if (!isAuthUserService.isUserAuthenticated()) {
            throw new UnauthorizedException();
        }

        SongModel song = songRepository.findById(id)
                .orElseThrow(() -> new RecordNotFound("Song not found", "id"));

        if (title != null) {
            song.setTitle(title);
        }

        if (artistId != null) {
            var artist = artistRepository.findById(artistId)
                    .orElseThrow(() -> new RecordNotFound("Artist not found", "artistId"));
            song.setArtist(artist);
        }

        if (albumId != null) {
            var album = albumRepository.findById(albumId)
                    .orElseThrow(() -> new RecordNotFound("Album not found", "albumId"));
            song.setAlbum(album);
        }

        if (url != null) {
            song.setUrl(url);
        }

        if (durationSeconds != null) {
            song.setDurationSeconds(durationSeconds);
        }

        SongModel updatedSong = songRepository.save(song);
        return new ApiResponse<>(true, "Song updated successfully", updatedSong);
    }

    @MutationMapping
    public ApiResponse<String> deleteSong(@Argument UUID id) {
        if (!isAuthUserService.isUserAuthenticated()) {
            throw new UnauthorizedException();
        }

        SongModel song = songRepository.findById(id)
                .orElseThrow(() -> new RecordNotFound("Song not found", "id"));

        songRepository.delete(song);
        return new ApiResponse<>(true, "Song deleted successfully", null);
    }
}
