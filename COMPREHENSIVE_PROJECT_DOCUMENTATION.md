# MusicX Backend Project Report

## Personal Details

- Module Name: Mobile Application Development
- Student Name: Morenikeji Elijah Popoola
- Student ID: 30121940
- Description: A report on the implementation of a GraphQL backend service, submitted in fulfillment of The Mobile Application Development coursework one (1) assessment.
- Date Written: 19th March, 2026
- GitHub URL: [Github - kejiahp/musicx](https://github.com/kejiahp/musicX)
- Postman Documentation URL: [Postman Documentation](https://kejiahp.postman.co/workspace/My-Workspace~5a5bae25-0678-49b3-824f-8520d8b5b7be/collection/20655642-a4082d6d-da5b-4d60-8dff-0722006d98f0?action=share&source=copy-link&creator=20655642)

## 1. Justification of Theme Choice

I chose to build MusicX with **Java Spring Boot and GraphQL** instead of the prescribed Node.js/Express/MongoDB stack, with explicit approval from my lecturer Alun King. My rationale was twofold: I wanted to deepen my knowledge of enterprise-grade backend technologies and strategically position myself for job opportunities at companies that leverage the Java ecosystem extensively, including Amazon, Netflix, JP Morgan & Chase, TikTok, and Google.

Spring Boot provides production-ready patterns for building scalable applications, while GraphQL offers a single, flexible endpoint for clients to query exactly what they need. The stateless JWT authentication approach aligns with modern microservices architectures. This hands-on experience with Java's type safety, dependency injection, and database migrations gives me a competitive advantage during my job search.

---

## 2. Libraries Used

**Core Stack:**

- Spring Boot 4.0.3 with Spring Security for authentication
- Spring Data JPA for database interaction
- PostgreSQL as the relational database
- Spring Boot GraphQL starter for GraphQL endpoint setup
- GraphQL Java Extended Scalars for custom types (Date, URL, UUID, Instant)

**Authentication & Security:**

- JJWT (JSON Web Token) library for JWT generation and validation
- BCrypt for password hashing

**Database Management:**

- Flyway Core for schema versioning and migrations

**Development Tools:**

- Lombok for reducing boilerplate code
- Spring DevTools for live reload

**Testing:**

- Spring Boot GraphQL Test starter with GraphQlTester
- Spring Security Test for authentication testing
- Mockito for mocking service dependencies
- JUnit 5 for test execution

---

## 3. Source of Database Content

I manually curated the database by reviewing my YouTube Music playlist and selecting songs I had on repeat during the past two months. Rather than bulk-importing or generating synthetic data, this approach ensures the dataset reflects my genuine listening patterns and personal music preferences. Each entry includes standardized metadata: song title, artist name, album information, duration, genres, and YouTube URLs for audio assets.

---

## 4. Overview of API Endpoints

The application exposes a single GraphQL endpoint (`/graphql`) with the following core operations:

**Authentication Mutations (Public):**

- `signup(name, email, password)` — Creates user account with BCrypt-hashed password
- `login(email, password)` — Validates credentials and returns 24-hour JWT token

**User Query (Protected):**

- `me` — Returns current authenticated user profile

**Music Queries (Protected, Require JWT):**

- `songs` — Returns all songs with nested artist and album data
- `song(id)` — Retrieves individual song by UUID
- `artists` — Returns all artists
- `artist(id)` — Returns artist with all associated songs
- `albums` — Returns all albums
- `album(id)` — Returns album with all contained songs
- `genres` — Returns all genres
- `genre(id)` — Returns genre with all categorized songs

All responses follow a consistent structure with `success`, `message`, and `data` fields. Protected queries throw `UnauthorizedException` if JWT is missing or invalid. The JWT token includes the user ID and session ID, verified on each request via the `JwtAuthFilter`.

---

## 5. Overview of Tests

I implemented six focused test suites using GraphQlTester to validate core functionality:

**AuthGraphQLTest**

- Tests user signup with success and failure scenarios (duplicate email detection)
- Validates login mutation and token generation
- Uses Mockito to isolate UserService from the test

**SongGraphQLTest**

- Tests song query endpoint with authentication checks
- Verifies nested field resolution (artist and album within song)
- Mocks repository layer to avoid database dependencies

**UserGraphQLTest**

- Tests the `me` query endpoint for authenticated user retrieval
- Sets up Spring Security context with mock user directly
- Validates unauthorized access is properly rejected

**ArtistGraphQLTest**

- Tests artist list and individual artist retrieval by ID
- Verifies authentication is required for all artist queries
- Mocks ArtistRepository and SongRepository to test nested song aggregation

**AlbumGraphQLTest**

- Tests album list and individual album retrieval with all contained songs
- Validates authentication on protected album endpoints
- Verifies nested artist relationship resolution within albums

**GenreGraphQLTest**

- Tests genre list and individual genre retrieval with categorized songs
- Ensures authentication is enforced for genre queries
- Validates many-to-many relationship between genres and songs

All tests execute via `./gradlew test` and generate HTML reports in `build/reports/tests/test/`. Each test uses the `@GraphQlTest` annotation to slice only the GraphQL layer, providing fast, focused unit tests with clear pass/fail feedback.

---

## Additional Infrastructure

**Database:** PostgreSQL with 8 tables (users, sessions, artists, albums, songs, genres, song_genres). Flyway manages migrations—V1.0 initialized the schema, V1.1 renamed the songs.url column.

**Security:** Stateless JWT authentication with BCrypt password hashing. SecurityConfig permits public access to `/graphql`, `/graphiql`, and `/` endpoints only; all others require valid JWT.

**Development:** GraphiQL IDE enables interactive query testing at `http://localhost:8080/graphiql`. Docker Compose provides containerized PostgreSQL for local development consistency.

Enables GraphQL schema to use typed scalars with automatic validation and serialization.

### Database Configuration

**Docker Compose Setup** (`docker-compose.yml`):

```yaml
name: musicx
services:
  db-postgres:
    image: postgres:16
    container_name: musicx_postgres
    restart: always
    environment:
      - POSTGRES_USER=pguser
      - POSTGRES_PASSWORD=pgpassword
      - POSTGRES_DB=musicx_db
    volumes:
      - postgresdbdata:/var/lib/postgresql/data
    ports:
      - "5432:5432"

volumes:
  postgresdbdata:
```

Provides containerized PostgreSQL for local development, ensuring consistency across environments.

---

## 9. Build & Deployment

### Build System (Gradle Kotlin DSL)

**Key Plugins**:

- `jacoco`: Code coverage reporting
- `io.spring.dependency-management`: Consistent Spring library versions

**Tasks**:

```bash
./gradlew clean build          # Full build with tests
./gradlew bootRun              # Start development server
./gradlew bootJar              # Package production JAR
./gradlew test                 # Execute test suite
```

### Development Workflow

**Setup & First Run**:

```bash
# Start PostgreSQL (if using Docker)
docker-compose up -d

# Run migrations
./gradlew bootRun --args="migrate"

# (Optional) Seed with example data
./gradlew bootRun --args="migrate seed"

# Start server in development mode
./gradlew bootRun
```

**GraphQL IDE Access**:

- Navigate to `http://localhost:8080/graphiql`
- Interactive query builder and documentation explorer
- Real-time query/mutation testing against live server

---

## Conclusion

MusicX demonstrates modern full-stack backend development practices through:

- **Type-Safe Architecture**: Java's compile-time safety with GraphQL's query validation
- **Clean Code**: Domain-driven structure with clear separation of concerns
- **Production Readiness**: Security, testing, and error handling from inception
- **Scalability**: Stateless authentication suitable for horizontal scaling
- **Developer Experience**: GraphQL IDE for exploration and Spring Boot's convention-over-configuration

The project successfully achieves its dual objectives: delivering a functional music metadata management system while providing valuable hands-on experience with enterprise-grade technologies aligned with high-demand industry standards.
