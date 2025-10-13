-- Migración: Agregar campos de integración con TMDB (Tabla renombrada "movies")
-- Fecha: 2025-10-13
-- Descripción: Agrega campos para almacenar información de películas importadas desde TMDB
-- NOTA: Este script es para la tabla YA RENOMBRADA a "movies"

-- Agregar columnas de TMDB a la tabla movies
ALTER TABLE movies
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
CREATE INDEX IF NOT EXISTS idx_tmdb_id ON movies(tmdb_id);

-- Comentarios para documentar las columnas
COMMENT ON COLUMN movies.tmdb_id IS 'ID de la película en The Movie Database (TMDB)';
COMMENT ON COLUMN movies.poster_url IS 'URL completa del poster de la película desde TMDB';
COMMENT ON COLUMN movies.backdrop_url IS 'URL completa del backdrop de la película desde TMDB';
COMMENT ON COLUMN movies.overview IS 'Sinopsis de la película desde TMDB';
COMMENT ON COLUMN movies.original_title IS 'Título original de la película';
COMMENT ON COLUMN movies.vote_average IS 'Calificación promedio en TMDB (0-10)';
COMMENT ON COLUMN movies.vote_count IS 'Cantidad de votos en TMDB';
COMMENT ON COLUMN movies.popularity IS 'Índice de popularidad en TMDB';
COMMENT ON COLUMN movies.original_language IS 'Código de idioma original de la película';

-- Remover constraint UNIQUE del título para permitir variaciones
ALTER TABLE movies DROP CONSTRAINT IF EXISTS movies_title_key;

