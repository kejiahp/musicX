package com.kejiahp.musicx.apps.artist;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.kejiahp.musicx.apps.song.SongModel;
import com.kejiahp.musicx.apps.song.SongRepository;
import com.kejiahp.musicx.util.IsAuthUserService;
import com.kejiahp.musicx.util.exceptions.authorization.UnauthorizedException;

@Controller
public class ArtistController {
    @Autowired
    private IsAuthUserService isAuthUserService;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @QueryMapping
    public List<ArtistModel> artists() {
        if (!isAuthUserService.isUserAuthenticated()) {
            throw new UnauthorizedException();
        }
        return artistRepository.findAll();
    }

    @QueryMapping
    public ArtistSongResponse artist(@Argument UUID id) {
        if (!isAuthUserService.isUserAuthenticated()) {
            throw new UnauthorizedException();
        }
        ArtistModel artist = artistRepository.findById(id).orElse(null);
        if (artist == null)
            return null;

        List<SongModel> songs = songRepository.findByArtist(artist);

        return new ArtistSongResponse(artist.getId(), artist.getName(), songs, artist.getCreatedAt(),
                artist.getUpdatedAt());
    }

    private record ArtistSongResponse(
            UUID id,
            String name,
            List<SongModel> songs,
            Instant createdAt,
            Instant updatedAt) {
    };
}
