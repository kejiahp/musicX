CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


-- Create user table
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    password_reset_code VARCHAR(20) NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Create session table
CREATE TABLE IF NOT EXISTS sessions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    is_valid BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);


-- Create artists table
CREATE TABLE IF NOT EXISTS artists (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name TEXT NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);


-- Create albums table
CREATE TABLE IF NOT EXISTS albums (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title TEXT NOT NULL,
    cover_image TEXT NULL,
    artist_id UUID NOT NULL REFERENCES artists(id) ON DELETE CASCADE,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);


-- Create songs table
CREATE TABLE IF NOT EXISTS songs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title TEXT NOT NULL,
    cover_image TEXT NULL,
    artist_id UUID NOT NULL REFERENCES artists(id) ON DELETE CASCADE,
    album_id UUID REFERENCES albums(id) ON DELETE SET NULL,
    duration_seconds INT,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);


-- Create genres table
CREATE TABLE IF NOT EXISTS genres (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name TEXT NOT NULL UNIQUE,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);


-- Create song_genres many-to-many table
CREATE TABLE IF NOT EXISTS song_genres (
    song_id UUID NOT NULL REFERENCES songs(id) ON DELETE CASCADE,
    genre_id UUID NOT NULL REFERENCES genres(id) ON DELETE CASCADE,

    PRIMARY KEY (song_id, genre_id)
);


-- Create function to update the `updated_at` field 
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create trigger on the tables
CREATE TRIGGER update_users_timestamp
BEFORE UPDATE ON users
FOR EACH ROW EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER update_artists_timestamp
BEFORE UPDATE ON artists
FOR EACH ROW EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER update_albums_timestamp
BEFORE UPDATE ON albums
FOR EACH ROW EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER update_songs_timestamp
BEFORE UPDATE ON songs
FOR EACH ROW EXECUTE FUNCTION update_timestamp();

CREATE TRIGGER update_genres_timestamp
BEFORE UPDATE ON genres
FOR EACH ROW EXECUTE FUNCTION update_timestamp();
