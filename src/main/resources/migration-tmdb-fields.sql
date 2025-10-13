-- Migración: Agregar campos de integración con TMDB
-- Fecha: 2025-10-13
-- Descripción: Agrega campos para almacenar información de películas importadas desde TMDB

-- NOTA: Este script usa el nombre antiguo de tabla (platzi_play_peliculas)
-- Si ya renombraste tu tabla a "movies", usa: migration-tmdb-fields-movies.sql

-- Agregar columnas de TMDB a la tabla platzi_play_peliculas
ALTER TABLE platzi_play_peliculas
    ADD COLUMN IF NOT EXISTS tmdb_id BIGINT UNIQUE,
    ADD COLUMN IF NOT EXISTS poster_url VARCHAR(500),
    ADD COLUMN IF NOT EXISTS backdrop_url VARCHAR(500),
    ADD COLUMN IF NOT EXISTS overview TEXT,
    ADD COLUMN IF NOT EXISTS original_title VARCHAR(200),
    ADD COLUMN IF NOT EXISTS vote_average DOUBLE PRECISION,
    ADD COLUMN IF NOT EXISTS vote_count INTEGER,
    ADD COLUMN IF NOT EXISTS popularity DOUBLE PRECISION,
    ADD COLUMN IF NOT EXISTS original_language VARCHAR(10);

-- Crear índice en tmdb_id para búsquedas rápidas
CREATE INDEX IF NOT EXISTS idx_tmdb_id ON platzi_play_peliculas(tmdb_id);

-- Comentarios para documentar las columnas
COMMENT ON COLUMN platzi_play_peliculas.tmdb_id IS 'ID de la película en The Movie Database (TMDB)';
COMMENT ON COLUMN platzi_play_peliculas.poster_url IS 'URL completa del poster de la película desde TMDB';
COMMENT ON COLUMN platzi_play_peliculas.backdrop_url IS 'URL completa del backdrop de la película desde TMDB';
COMMENT ON COLUMN platzi_play_peliculas.overview IS 'Sinopsis de la película desde TMDB';
COMMENT ON COLUMN platzi_play_peliculas.original_title IS 'Título original de la película';
COMMENT ON COLUMN platzi_play_peliculas.vote_average IS 'Calificación promedio en TMDB (0-10)';
COMMENT ON COLUMN platzi_play_peliculas.vote_count IS 'Cantidad de votos en TMDB';
COMMENT ON COLUMN platzi_play_peliculas.popularity IS 'Índice de popularidad en TMDB';
COMMENT ON COLUMN platzi_play_peliculas.original_language IS 'Código de idioma original de la película';

-- Remover constraint UNIQUE del título para permitir variaciones
-- (Una película puede tener diferentes traducciones del título)
ALTER TABLE platzi_play_peliculas DROP CONSTRAINT IF EXISTS platzi_play_peliculas_titulo_key;

