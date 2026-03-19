Start server and run migrations: `./gradlew bootRun --args="migrate"`
Start server and run seed command: `./gradlew bootRun --args="seed"`

Start server, run migrations then attempt database seed`./gradlew bootRun --args="migrate seed"`

Start server: `./gradlew bootRun`

Refresh Dependencies: `./gradlew build --refresh-dependencies`

Full Resolve: `./gradlew dependencies`

`gradlew clean build --refresh-dependencies`

### References

[https://dimitri.codes/graphql-mutations-spring/](https://dimitri.codes/graphql-mutations-spring/)

[https://dimitri.codes/graphql-spring-boot/](https://dimitri.codes/graphql-spring-boot/)

[https://medium.com/@ahmettemelkundupoglu/building-a-graphql-integration-in-a-spring-boot-3-project-with-java-21-53dede779a46](https://medium.com/@ahmettemelkundupoglu/building-a-graphql-integration-in-a-spring-boot-3-project-with-java-21-53dede779a46)

[https://spring.io/guides/gs/graphql-server](https://spring.io/guides/gs/graphql-server)

[https://spring.io/guides/topicals/observing-graphql-in-action](https://spring.io/guides/topicals/observing-graphql-in-action)

[https://github.com/spring-guides](https://github.com/spring-guides)

#### My Example GraphQL Schema

```graphql
type Query {
  songs: [Song]
  song(id: ID!): Song
  artists: [Artist]
  albums: [Album]
}

type Song {
  id: ID!
  title: String!
  artist: Artist
  album: Album
}

type Artist {
  id: ID!
  name: String!
}

type Album {
  id: ID!
  title: String!
  year: Int!
}
```

#### Regular Resolvers

```java
@Component
public class QueryResolver {

    private final SongRepository songRepo;
    private final ArtistRepository artistRepo;
    private final AlbumRepository albumRepo;

    public QueryResolver(SongRepository songRepo, ArtistRepository artistRepo, AlbumRepository albumRepo) {
        this.songRepo = songRepo;
        this.artistRepo = artistRepo;
        this.albumRepo = albumRepo;
    }

    // -----------------------------
    // Query: Get all songs
    // -----------------------------
    @QueryMapping
    public List<Song> songs() {
        // GraphQL calls this method when the client queries "songs"
        return songRepo.findAll();
    }

    // -----------------------------
    // Query: Get a single song by ID
    // -----------------------------
    @QueryMapping
    public Song song(@Argument Long id) {
        // @Argument maps GraphQL arguments to method parameters
        return songRepo.findById(id).orElse(null);
    }

    // -----------------------------
    // Query: Get all artists
    // -----------------------------
    @QueryMapping
    public List<Artist> artists() {
        return artistRepo.findAll();
    }

    // -----------------------------
    // Query: Get all albums
    // -----------------------------
    @QueryMapping
    public List<Album> albums() {
        return albumRepo.findAll();
    }
}
```

#### Custom resolvers for nested fields (Spring Data JPA does most of this already)

```java
@Component
public class SongFieldResolver {

    private final ArtistRepository artistRepo;
    private final AlbumRepository albumRepo;

    public SongFieldResolver(ArtistRepository artistRepo, AlbumRepository albumRepo) {
        this.artistRepo = artistRepo;
        this.albumRepo = albumRepo;
    }

    // Resolve "artist" field inside Song
    @SchemaMapping(typeName = "Song", field = "artist")
    public Artist getArtist(Song song) {
        // GraphQL automatically passes the parent object (Song)
        return artistRepo.findById(song.getArtistId()).orElse(null);
    }

    // Resolve "album" field inside Song
    @SchemaMapping(typeName = "Song", field = "album")
    public Album getAlbum(Song song) {
        return albumRepo.findById(song.getAlbumId()).orElse(null);
    }
}


```
