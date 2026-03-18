package com.kejiahp.musicx.seed;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class DBDataService {
        public record AlbumData(String albumName, String coverImage, List<SongData> songList) {
        };

        public record SongData(String title, int duration, String url) {
        };

        public HashMap<String, AlbumData> artistAlbum = new HashMap<>();

        public DBDataService() {
                // INSERT ARTIST ALBUMS
                artistAlbum.put("$ucideboy$", new AlbumData("Sing Me a Lullaby, My Sweet Temptation",
                                "https://lh3.googleusercontent.com/puplFkOoa2ef7W7btQvi5yowP0smBmX_jUtCgDn9aIMhEDtoKoBPS_cFbZePFXWZkSu__pyToZlhdHmp=w544-h544-l90-rj",
                                List.of(new SongData("Fucking Your Culture", 3 * 60 + 16,
                                                "https://music.youtube.com/watch?v=3576QK91Qv0&si=nV7gcj89SJxBRnmp"),
                                                new SongData("Escape from BABYLON", 2 * 60 + 22,
                                                                "https://music.youtube.com/watch?v=--UOaC30fys&si=6eANq4su58AV-WLR"),
                                                new SongData("$ucideboy$ Were Better In 2015", 2 * 60 + 24,
                                                                "https://music.youtube.com/watch?v=dXGyi9YiiTo&si=76uX2ug4dXhP1JK-"),
                                                new SongData("1000 Blunts", 2 * 60 + 56,
                                                                "https://music.youtube.com/watch?v=81uYuA1N4Qo&si=O4pFmOu1SyTVGfDB"))));

                artistAlbum.put("SleepToken", new AlbumData("This Place Will Become Your Tomb",
                                "https://lh3.googleusercontent.com/Rv3R2N0ObnUueOlIq2CO_v3D8_pDrz6AzHaOVq68XuptfO0jg78Zeew_d-eMDzREiV2x6QPdq2WngDl3=w544-h544-l90-rj",
                                List.of(new SongData("Atlantic", 4 * 60 + 53,
                                                "https://music.youtube.com/watch?v=dqLk4san-Bg&si=aciaYVu_jY0jo5Hz"),
                                                new SongData("Like That", 3 * 60 + 35,
                                                                "https://music.youtube.com/watch?v=3Iz46MdOaO8&si=TXKFRSkit0iU59vV"),
                                                new SongData("Fall For Me", 2 * 60 + 27,
                                                                "https://music.youtube.com/watch?v=d0Edderqf2A&si=Lq4mWivU4ejG43ah"),
                                                new SongData("Alkaline", 3 * 60 + 35,
                                                                "https://music.youtube.com/watch?v=Loa_OSFotDQ&si=AZAbD0qqD59JQGCj"))));
                artistAlbum.put("Joji", new AlbumData("Piss In The Wind",
                                "https://yt3.googleusercontent.com/dUYF7tUeaiD1FVo-ZWvSDUjUY-TEw2Hnc4tMFWRjxck1Li1GS5SfDNTKFJsqT4nh7PLKAfM2MTQS_hYH=w544-h544-l90-rj",
                                List.of(new SongData("Pixelated Kisses", 1 * 60 + 51,
                                                "https://music.youtube.com/watch?v=X1ge7lXie_Y&si=By6YXPlISyKemHw9"),
                                                new SongData("Tarmac", 1 * 60 + 36,
                                                                "https://music.youtube.com/watch?v=xd9zqy_KMPQ&si=1kA0tbtpMYzZg5XP"),
                                                new SongData("Sojourn", 2 * 60 + 57,
                                                                "https://music.youtube.com/watch?v=tRUzHnjhGKQ&si=GB6ARu7NlKwzUOKx"),
                                                new SongData("Last of a Dying Breed", 2 * 60 + 30,
                                                                "https://music.youtube.com/watch?v=M6ZBoV6xOLM&si=tUb5raK9Z5AkXmhg"))));
                artistAlbum.put("J.cole", new AlbumData("The Fall-Off",
                                "https://lh3.googleusercontent.com/dYqRDNV0XWvR1ucsy0Wg1K60FWwKr9ueSc42-EC5phtokKIsVCK0dPHrSrfWpF_Q5aQjJpe-rkrMfNM=w544-h544-l90-rj",
                                List.of(new SongData("Two Six", 3 * 60 + 17,
                                                "https://music.youtube.com/watch?v=XOM7nrXClj0&si=ZATZSQmn376yQWa0"),
                                                new SongData("Poor Thang", 4 * 60 + 51,
                                                                "https://music.youtube.com/watch?v=rWBLVn0pol0&si=76OMyNmUdfvum9Pj"),
                                                new SongData("WHO TF IZ U", 4 * 60 + 38,
                                                                "https://music.youtube.com/watch?v=p1tB2s5cEOk&si=Ts1OHb_HtKNm8xl0"),
                                                new SongData("39 Intro", 6 * 60 + 07,
                                                                "https://music.youtube.com/watch?v=dcKYFTZruLM&si=HQTpMU0z6N_P1j5v"))));
                artistAlbum.put("Hikaru Utada", new AlbumData("BAD MODE",
                                "https://lh3.googleusercontent.com/U2abxp9PWBLLU4W-2G-9QeidnF2Q-xaYHKaDv9FKigY8crkYXE7a9aoWRMUFkMIfgXsGlAiMJklc5KNi=w544-h544-l90-rj",
                                List.of(new SongData("BADモード - BAD MODE", 5 * 60 + 4,
                                                "https://music.youtube.com/watch?v=bUmnh3omeE4&si=XKsSN2DclSEf1p7O"),
                                                new SongData("君に夢中 - Kimini Muchuu", 4 * 60 + 18,
                                                                "https://music.youtube.com/watch?v=yUrMVw7osA4&si=HhkSRm7BAB-0y2fl"),
                                                new SongData("PINK BLOOD", 3 * 60 + 18,
                                                                "https://music.youtube.com/watch?v=y9h6S5v4kQw&si=HtnV07lwXHeCrV5n"))));
        }

}
