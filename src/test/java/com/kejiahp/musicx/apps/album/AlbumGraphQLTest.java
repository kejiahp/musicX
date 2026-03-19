package com.kejiahp.musicx.apps.album;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
import com.kejiahp.musicx.apps.song.SongModel;
import com.kejiahp.musicx.apps.song.SongRepository;
import com.kejiahp.musicx.config.GlobalGraphQLExceptionHandler;
import com.kejiahp.musicx.config.GraphQLConfiguration;
import com.kejiahp.musicx.util.IsAuthUserService;

@GraphQlTest(controllers = AlbumController.class)
@Import({ GraphQLConfiguration.class, GlobalGraphQLExceptionHandler.class })
public class AlbumGraphQLTest {
    @Autowired
    private GraphQlTester graphQlTester;

    @MockitoBean
    private AlbumRepository albumRepository;

    @MockitoBean
    private SongRepository songRepository;

    @MockitoBean
    private IsAuthUserService isAuthUserService;

    @Test
    void shouldReturnAlbumList() {
        // Authenticated user
        BDDMockito.when(isAuthUserService.isUserAuthenticated()).thenReturn(true);

        AlbumModel album = getAlbumMockData();

        // Mock Repository methods
        BDDMockito.when(albumRepository.findAll()).thenReturn(List.of(album));

        graphQlTester.document("""
                query {
                    albums {
                        id
                        title
                        coverImage
                        artist {
                            name
                        }
                        createdAt
                        updatedAt
                    }
                }
                """).execute()
                .path("albums").entityList(AlbumModel.class).hasSize(1)
                .path("albums[0].title").entity(String.class).isEqualTo("BAD MODE");
    }

    @Test
    void shouldFailToReturnAlbumList_whenUserNotAuthenticated() {
        // User not authenticated
        BDDMockito.when(isAuthUserService.isUserAuthenticated()).thenReturn(false);

        graphQlTester.document("""
                query {
                    albums {
                        id
                        title
                        coverImage
                    }
                }
                """).execute()
                .errors()
                .satisfy(errors -> {
                    assertTrue(!errors.isEmpty());
                    assertTrue(errors.get(0).getMessage().contains("Insufficient authentication"));
                });
    }

    @Test
    void shouldReturnAlbumWithSongs() {
        // Authenticated user
        BDDMockito.when(isAuthUserService.isUserAuthenticated()).thenReturn(true);

        AlbumModel album = getAlbumMockData();
        List<SongModel> songs = getSongsMockData(album);

        UUID albumId = album.getId();

        // Mock Repository methods
        BDDMockito.when(albumRepository.findById(albumId)).thenReturn(Optional.of(album));
        BDDMockito.when(songRepository.findByAlbum(album)).thenReturn(songs);

        graphQlTester.document("""
                query {
                    album(id: "%s") {
                        id
                        title
                        coverImage
                        artist {
                            name
                        }
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
                """.formatted(albumId)).execute()
                .path("album.id").entity(UUID.class).isEqualTo(albumId)
                .path("album.title").entity(String.class).isEqualTo("BAD MODE")
                .path("album.songs").entityList(SongModel.class).hasSize(2)
                .path("album.songs[0].title").entity(String.class).isEqualTo("君に夢中 - Kimini Muchuu");
    }

    @Test
    void shouldFailToReturnAlbum_whenUserNotAuthenticated() {
        // User not authenticated
        BDDMockito.when(isAuthUserService.isUserAuthenticated()).thenReturn(false);

        UUID albumId = UUID.randomUUID();

        graphQlTester.document("""
                query {
                    album(id: "%s") {
                        id
                        title
                    }
                }
                """.formatted(albumId)).execute()
                .errors()
                .satisfy(errors -> {
                    assertTrue(!errors.isEmpty());
                    assertTrue(errors.get(0).getMessage().contains("Insufficient authentication"));
                });
    }

    @Test
    void shouldReturnNullAlbum_whenAlbumNotFound() {
        // Authenticated user
        BDDMockito.when(isAuthUserService.isUserAuthenticated()).thenReturn(true);

        UUID albumId = UUID.randomUUID();

        // Mock Repository methods
        BDDMockito.when(albumRepository.findById(albumId)).thenReturn(Optional.empty());

        graphQlTester.document("""
                query {
                    album(id: "%s") {
                        id
                        title
                    }
                }
                """.formatted(albumId)).execute()
                .path("album").valueIsNull();
    }

    private AlbumModel getAlbumMockData() {
        ArtistModel artist = new ArtistModel();
        artist.setId(UUID.randomUUID());
        artist.setName("Hikaru Utada");
        artist.setCreatedAt(Instant.now());
        artist.setUpdatedAt(Instant.now());

        AlbumModel album = new AlbumModel();
        album.setId(UUID.fromString("a1b2c3d4-e5f6-4a5b-8c9d-0e1f2a3b4c5d"));
        album.setTitle("BAD MODE");
        album.setCoverImage(
                "https://lh3.googleusercontent.com/U2abxp9PWBLLU4W-2G-9QeidnF2Q-xaYHKaDv9FKigY8crkYXE7a9aoWRMUFkMIfgXsGlAiMJklc5KNi=w544-h544-l90-rj");
        album.setArtist(artist);
        album.setCreatedAt(Instant.now());
        album.setUpdatedAt(Instant.now());

        return album;
    }

    private List<SongModel> getSongsMockData(AlbumModel album) {
        ArtistModel artist = album.getArtist();

        SongModel song1 = new SongModel();
        song1.setId(UUID.randomUUID());
        song1.setTitle("君に夢中 - Kimini Muchuu");
        song1.setArtist(artist);
        song1.setAlbum(album);
        song1.setDurationSeconds(243);
        song1.setCreatedAt(Instant.now());
        song1.setUpdatedAt(Instant.now());

        SongModel song2 = new SongModel();
        song2.setId(UUID.randomUUID());
        song2.setTitle("PINK BLOOD");
        song2.setArtist(artist);
        song2.setAlbum(album);
        song2.setDurationSeconds(325);
        song2.setCreatedAt(Instant.now());
        song2.setUpdatedAt(Instant.now());

        return List.of(song1, song2);
    }
}
