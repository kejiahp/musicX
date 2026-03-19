package com.kejiahp.musicx.apps.genre;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.kejiahp.musicx.apps.song.SongModel;
import com.kejiahp.musicx.config.GlobalGraphQLExceptionHandler;
import com.kejiahp.musicx.config.GraphQLConfiguration;
import com.kejiahp.musicx.util.IsAuthUserService;

@GraphQlTest(controllers = GenreController.class)
@Import({ GraphQLConfiguration.class, GlobalGraphQLExceptionHandler.class })
public class GenreGraphQLTest {
    @Autowired
    private GraphQlTester graphQlTester;

    @MockitoBean
    private GenreRepository genreRepository;

    @MockitoBean
    private IsAuthUserService isAuthUserService;

    @Test
    void shouldReturnGenreList() {
        // Authenticated user
        BDDMockito.when(isAuthUserService.isUserAuthenticated()).thenReturn(true);

        GenreModel genre = getGenreMockData();

        // Mock Repository methods
        BDDMockito.when(genreRepository.findAll()).thenReturn(List.of(genre));

        graphQlTester.document("""
                query {
                    genres {
                        id
                        name
                        createdAt
                        updatedAt
                    }
                }
                """).execute()
                .path("genres").entityList(GenreModel.class).hasSize(1)
                .path("genres[0].name").entity(String.class).isEqualTo("J-Pop (Japanese Pop)");
    }

    @Test
    void shouldReturnGenreWithSongs() {
        // Authenticated user
        BDDMockito.when(isAuthUserService.isUserAuthenticated()).thenReturn(true);

        GenreModel genre = getGenreMockData();
        Set<SongModel> songs = getSongsMockData();
        genre.setSongs(songs);

        UUID genreId = genre.getId();

        // Mock Repository methods
        BDDMockito.when(genreRepository.findById(genreId)).thenReturn(Optional.of(genre));

        graphQlTester.document("""
                query {
                    genre(id: "%s") {
                        id
                        name
                        songs {
                            id
                            title
                        }
                        createdAt
                        updatedAt
                    }
                }
                """.formatted(genreId)).execute()
                .path("genre.id").entity(UUID.class).isEqualTo(genreId)
                .path("genre.name").entity(String.class).isEqualTo("J-Pop (Japanese Pop)")
                .path("genre.songs").entityList(SongModel.class).hasSize(2);
    }

    private GenreModel getGenreMockData() {
        GenreModel genre = new GenreModel();
        genre.setId(UUID.randomUUID());
        genre.setName("J-Pop (Japanese Pop)");
        genre.setCreatedAt(Instant.now());
        genre.setUpdatedAt(Instant.now());

        return genre;
    }

    private Set<SongModel> getSongsMockData() {
        SongModel song1 = new SongModel();
        song1.setId(UUID.randomUUID());
        song1.setTitle("君に夢中 - Kimini Muchuu");
        song1.setDurationSeconds(243);
        song1.setCreatedAt(Instant.now());
        song1.setUpdatedAt(Instant.now());

        SongModel song2 = new SongModel();
        song2.setId(UUID.randomUUID());
        song2.setTitle("PINK BLOOD");
        song2.setDurationSeconds(325);
        song2.setCreatedAt(Instant.now());
        song2.setUpdatedAt(Instant.now());

        Set<SongModel> songs = new HashSet<>();
        songs.add(song1);
        songs.add(song2);

        return songs;
    }
}
