package com.kejiahp.musicx.apps.album;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.kejiahp.musicx.apps.artist.ArtistModel;
import com.kejiahp.musicx.apps.song.SongModel;
import com.kejiahp.musicx.apps.song.SongRepository;
import com.kejiahp.musicx.util.IsAuthUserService;
import com.kejiahp.musicx.util.exceptions.authorization.UnauthorizedException;

@Controller
public class AlbumController {
    @Autowired
    private IsAuthUserService isAuthUserService;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private SongRepository songRepository;

    @QueryMapping
    public List<AlbumModel> albums() {
        if (!isAuthUserService.isUserAuthenticated()) {
            throw new UnauthorizedException();
        }
        return albumRepository.findAll();
    }

    @QueryMapping
    public AlbumSongList album(@Argument UUID id) {
        if (!isAuthUserService.isUserAuthenticated()) {
            throw new UnauthorizedException();
        }
        AlbumModel album = albumRepository.findById(id).orElse(null);
        if (album == null)
            return null;

        List<SongModel> songs = songRepository.findByAlbum(album);

        return new AlbumSongList(album.getId(), album.getTitle(), songs, album.getCoverImage(), album.getArtist(),
                album.getCreatedAt(),
                album.getUpdatedAt());
    }

    private record AlbumSongList(
            UUID id,
            String title,
            List<SongModel> songs,
            String coverImage,
            ArtistModel artist,
            Instant createdAt,
            Instant updatedAt) {
    }
}
