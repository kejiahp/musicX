package com.kejiahp.musicx.apps.song;

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

import com.kejiahp.musicx.apps.album.AlbumModel;
import com.kejiahp.musicx.apps.album.AlbumRepository;
import com.kejiahp.musicx.apps.artist.ArtistModel;
import com.kejiahp.musicx.apps.artist.ArtistRepository;
import com.kejiahp.musicx.config.GlobalGraphQLExceptionHandler;
import com.kejiahp.musicx.config.GraphQLConfiguration;
import com.kejiahp.musicx.util.IsAuthUserService;

@GraphQlTest(controllers = SongController.class)
@Import({ GraphQLConfiguration.class, GlobalGraphQLExceptionHandler.class })
public class SongGraphQLTest {
    @Autowired
    private GraphQlTester graphQlTester;

    @MockitoBean
    private SongRepository songRepository;

    @MockitoBean
    private ArtistRepository artistRepository;

    @MockitoBean
    private AlbumRepository albumRepository;

    @MockitoBean
    private IsAuthUserService isAuthUserService;

    @Test
    void createSong_shouldCreateNewSong() {
        // Authenticated user
        BDDMockito.when(isAuthUserService.isUserAuthenticated()).thenReturn(true);

        ArtistModel artist = new ArtistModel();
        artist.setId(UUID.randomUUID());
        artist.setName("Hikaru Utada");

        AlbumModel album = new AlbumModel();
        album.setId(UUID.randomUUID());
        album.setTitle("BAD MODE");

        UUID artistId = artist.getId();
        UUID albumId = album.getId();

        // Mock Repository methods
        BDDMockito.when(artistRepository.findById(artistId)).thenReturn(Optional.of(artist));
        BDDMockito.when(albumRepository.findById(albumId)).thenReturn(Optional.of(album));

        SongModel newSong = new SongModel();
        newSong.setId(UUID.randomUUID());
        newSong.setTitle("PINK BLOOD");
        newSong.setUrl("https://music.youtube.com/watch?v=test");
        newSong.setDurationSeconds(325);
        newSong.setArtist(artist);
        newSong.setAlbum(album);

        BDDMockito.when(songRepository.save(BDDMockito.any(SongModel.class))).thenReturn(newSong);

        graphQlTester.document("""
                mutation {
                    createSong(
                        title: "PINK BLOOD"
                        artistId: "%s"
                        albumId: "%s"
                        url: "https://music.youtube.com/watch?v=test"
                        durationSeconds: 325
                    ) {
                        success
                        message
                        data {
                            id
                            title
                            artist {
                                name
                            }
                            album {
                                title
                            }
                            durationSeconds
                        }
                    }
                }
                """.formatted(artistId, albumId)).execute()
                .path("createSong.success").entity(Boolean.class).isEqualTo(true)
                .path("createSong.message").entity(String.class).isEqualTo("Song created successfully")
                .path("createSong.data.title").entity(String.class).isEqualTo("PINK BLOOD")
                .path("createSong.data.durationSeconds").entity(Integer.class).isEqualTo(325);
    }

    @Test
    void updateSong_shouldUpdateExistingSong() {
        // Authenticated user
        BDDMockito.when(isAuthUserService.isUserAuthenticated()).thenReturn(true);

        SongModel song = getSongMockData();
        UUID songId = song.getId();

        ArtistModel artist = song.getArtist();
        UUID artistId = artist.getId();

        // Mock Repository methods
        BDDMockito.when(songRepository.findById(songId)).thenReturn(Optional.of(song));
        BDDMockito.when(artistRepository.findById(artistId)).thenReturn(Optional.of(artist));

        SongModel updatedSong = new SongModel();
        updatedSong.setId(songId);
        updatedSong.setTitle("Updated Title");
        updatedSong.setUrl("https://music.youtube.com/watch?v=updated");
        updatedSong.setDurationSeconds(400);
        updatedSong.setArtist(artist);
        updatedSong.setAlbum(song.getAlbum());

        BDDMockito.when(songRepository.save(BDDMockito.any(SongModel.class))).thenReturn(updatedSong);

        graphQlTester.document("""
                mutation {
                    updateSong(
                        id: "%s"
                        title: "Updated Title"
                        artistId: "%s"
                        url: "https://music.youtube.com/watch?v=updated"
                        durationSeconds: 400
                    ) {
                        success
                        message
                        data {
                            id
                            title
                            url
                            durationSeconds
                        }
                    }
                }
                """.formatted(songId, artistId)).execute()
                .path("updateSong.success").entity(Boolean.class).isEqualTo(true)
                .path("updateSong.message").entity(String.class).isEqualTo("Song updated successfully")
                .path("updateSong.data.title").entity(String.class).isEqualTo("Updated Title")
                .path("updateSong.data.url").entity(String.class).isEqualTo("https://music.youtube.com/watch?v=updated")
                .path("updateSong.data.durationSeconds").entity(Integer.class).isEqualTo(400);
    }

    @Test
    void deleteSong_shouldDeleteExistingSong() {
        // Authenticated user
        BDDMockito.when(isAuthUserService.isUserAuthenticated()).thenReturn(true);

        SongModel song = getSongMockData();
        UUID songId = song.getId();

        // Mock Repository methods
        BDDMockito.when(songRepository.findById(songId)).thenReturn(Optional.of(song));
        BDDMockito.doNothing().when(songRepository).delete(song);

        graphQlTester.document("""
                mutation {
                    deleteSong(id: "%s") {
                        success
                        message
                        data {
                            title
                        }
                    }
                }
                """.formatted(songId)).execute()
                .path("deleteSong.success").entity(Boolean.class).isEqualTo(true)
                .path("deleteSong.message").entity(String.class).isEqualTo("Song deleted successfully")
                .path("deleteSong.data").valueIsNull();
    }

    private SongModel getSongMockData() {
        ArtistModel artist = new ArtistModel();
        artist.setId(UUID.randomUUID());
        artist.setName("Hikaru Utada");

        AlbumModel album = new AlbumModel();
        album.setTitle("BAD MODE");
        album.setCoverImage(
                "https://lh3.googleusercontent.com/U2abxp9PWBLLU4W-2G-9QeidnF2Q-xaYHKaDv9FKigY8crkYXE7a9aoWRMUFkMIfgXsGlAiMJklc5KNi=w544-h544-l90-rj");
        album.setArtist(artist);

        SongModel song = new SongModel();
        song.setTitle("君に夢中 - Kimini Muchuu");
        song.setUrl("https://music.youtube.com/watch?v=bUmnh3omeE4&si=XKsSN2DclSEf1p7O");
        song.setDurationSeconds(4 * 60 + 18);
        song.setArtist(artist);
        song.setAlbum(album);
        song.setId(UUID.fromString("ea6098eb-1117-452c-bd47-e1505dd332a5"));

        return song;
    }

    @Test
    void shouldReturnSongList() {
        // Authenticated user
        BDDMockito.when(isAuthUserService.isUserAuthenticated()).thenReturn(true);

        SongModel song = getSongMockData();

        // Mock Repository methods
        BDDMockito.when(songRepository.findAll()).thenReturn(List.of(song));

        graphQlTester.document("""
                query {
                    songs {
                        id
                        title
                        url
                        artist {
                            name
                        }
                        album {
                            coverImage
                        }
                    }
                }
                """).execute()
                .path("songs").entityList(SongModel.class).hasSize(1)
                .path("songs[0].title").entity(String.class).isEqualTo("君に夢中 - Kimini Muchuu");
    }

    @Test
    void shouldReturnASongById() {
        // Authenticated user
        BDDMockito.when(isAuthUserService.isUserAuthenticated()).thenReturn(true);

        SongModel song = getSongMockData();

        // Mock Repository methods
        BDDMockito.when(songRepository.findById(song.getId())).thenReturn(Optional.of(song));

        graphQlTester.document("""
                query {
                    song(id: "ea6098eb-1117-452c-bd47-e1505dd332a5") {
                        id
                        title
                        url
                        artist {
                            name
                        }
                        album {
                            coverImage
                        }
                    }
                }
                """).execute()
                .path("song.id").entity(UUID.class).isEqualTo(song.getId())
                .path("song.title").entity(String.class).isEqualTo(song.getTitle());
    }
}