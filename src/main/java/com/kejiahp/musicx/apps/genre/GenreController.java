package com.kejiahp.musicx.apps.genre;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.kejiahp.musicx.apps.song.SongModel;
import com.kejiahp.musicx.util.IsAuthUserService;
import com.kejiahp.musicx.util.exceptions.authorization.UnauthorizedException;

@Controller
public class GenreController {
    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private IsAuthUserService isAuthUserService;

    @QueryMapping
    public List<GenreModel> genres() {
        if (!isAuthUserService.isUserAuthenticated()) {
            throw new UnauthorizedException();
        }
        return genreRepository.findAll();
    }

    @QueryMapping
    public GenreSongListResponse genre(@Argument UUID id) {
        if (!isAuthUserService.isUserAuthenticated()) {
            throw new UnauthorizedException();
        }
        GenreModel genre = genreRepository.findById(id).orElse(null);
        if (genre == null)
            return null;
        return new GenreSongListResponse(genre.getId(), genre.getName(), new ArrayList<>(genre.getSongs()),
                genre.getCreatedAt(), genre.getUpdatedAt());

    }

    private record GenreSongListResponse(
            UUID id,
            String name,
            List<SongModel> songs,
            Instant createdAt,
            Instant updatedAt) {
    };
}
