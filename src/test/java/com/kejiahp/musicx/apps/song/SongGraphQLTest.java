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
import com.kejiahp.musicx.apps.artist.ArtistModel;
import com.kejiahp.musicx.config.GraphQLConfiguration;
import com.kejiahp.musicx.util.IsAuthUserService;

@GraphQlTest(controllers = SongController.class)
@Import(GraphQLConfiguration.class)
public class SongGraphQLTest {
    @Autowired
    private GraphQlTester graphQlTester;

    @MockitoBean
    private SongRepository songRepository;

    @MockitoBean
    private IsAuthUserService isAuthUserService;

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

    private SongModel getSongMockData() {
        ArtistModel artist = new ArtistModel();
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
}