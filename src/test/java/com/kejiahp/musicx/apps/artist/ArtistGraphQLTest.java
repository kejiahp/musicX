package com.kejiahp.musicx.apps.artist;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.kejiahp.musicx.apps.artist.ArtistModel;
import com.kejiahp.musicx.apps.artist.ArtistRepository;
import com.kejiahp.musicx.apps.artist.ArtistController;
import com.kejiahp.musicx.apps.song.SongModel;
import com.kejiahp.musicx.apps.song.SongRepository;
import com.kejiahp.musicx.config.GlobalGraphQLExceptionHandler;
import com.kejiahp.musicx.config.GraphQLConfiguration;
import com.kejiahp.musicx.util.IsAuthUserService;

@GraphQlTest(controllers = ArtistController.class)
@Import({ GraphQLConfiguration.class, GlobalGraphQLExceptionHandler.class })
public class ArtistGraphQLTest {
    @Autowired
    private GraphQlTester graphQlTester;

    @MockitoBean
    private ArtistRepository artistRepository;

    @MockitoBean
    private SongRepository songRepository;

    @MockitoBean
    private IsAuthUserService isAuthUserService;

    @Test
    void shouldReturnArtistList() {
        // Authenticated user
        BDDMockito.when(isAuthUserService.isUserAuthenticated()).thenReturn(true);

        ArtistModel artist = getArtistMockData();

        // Mock Repository methods
        BDDMockito.when(artistRepository.findAll()).thenReturn(List.of(artist));

        graphQlTester.document("""
                query {
                    artists {
                        id
                        name
                        createdAt
                        updatedAt
                    }
                }
                """).execute()
                .path("artists").entityList(ArtistModel.class).hasSize(1)
                .path("artists[0].name").entity(String.class).isEqualTo("Hikaru Utada");
    }

    @Test
    void shouldReturnArtistWithSongs() {
        // Authenticated user
        BDDMockito.when(isAuthUserService.isUserAuthenticated()).thenReturn(true);

        ArtistModel artist = getArtistMockData();
        List<SongModel> songs = getSongsMockData(artist);

        UUID artistId = artist.getId();

        // Mock Repository methods
        BDDMockito.when(artistRepository.findById(artistId)).thenReturn(Optional.of(artist));
        BDDMockito.when(songRepository.findByArtist(artist)).thenReturn(songs);

        graphQlTester.document("""
                query {
                    artist(id: "%s") {
                        id
                        name
                        songs {
                            id
                            title
                            artist {
                                name
                            }
                        }
                        createdAt
                        updatedAt
                    }
                }
                """.formatted(artistId)).execute()
                .path("artist.id").entity(UUID.class).isEqualTo(artistId)
                .path("artist.name").entity(String.class).isEqualTo("Hikaru Utada")
                .path("artist.songs").entityList(SongModel.class).hasSize(2)
                .path("artist.songs[0].title").entity(String.class).isEqualTo("君に夢中 - Kimini Muchuu");
    }

    private ArtistModel getArtistMockData() {
        ArtistModel artist = new ArtistModel();
        artist.setId(UUID.randomUUID());
        artist.setName("Hikaru Utada");
        artist.setCreatedAt(Instant.now());
        artist.setUpdatedAt(Instant.now());

        return artist;
    }

    private List<SongModel> getSongsMockData(ArtistModel artist) {
        SongModel song1 = new SongModel();
        song1.setId(UUID.randomUUID());
        song1.setTitle("君に夢中 - Kimini Muchuu");
        song1.setArtist(artist);
        song1.setDurationSeconds(243);
        song1.setCreatedAt(Instant.now());
        song1.setUpdatedAt(Instant.now());

        SongModel song2 = new SongModel();
        song2.setId(UUID.randomUUID());
        song2.setTitle("PINK BLOOD");
        song2.setArtist(artist);
        song2.setDurationSeconds(325);
        song2.setCreatedAt(Instant.now());
        song2.setUpdatedAt(Instant.now());

        return List.of(song1, song2);
    }
}
