-- Script para renombrar tablas (sin limpiar datos)
-- Si quieres mantener los datos pero cambiar nombres

-- Renombrar tabla principal
ALTER TABLE platzi_play_peliculas RENAME TO movies;

-- Renombrar secuencia
ALTER SEQUENCE IF EXISTS platzi_play_peliculas_id_seq RENAME TO movies_id_seq;

-- Renombrar columnas a inglés (opcional)
ALTER TABLE movies RENAME COLUMN titulo TO title;
ALTER TABLE movies RENAME COLUMN duracion TO duration;
ALTER TABLE movies RENAME COLUMN genero TO genre;
ALTER TABLE movies RENAME COLUMN fecha_estreno TO release_date;
ALTER TABLE movies RENAME COLUMN clasificacion TO rating;
ALTER TABLE movies RENAME COLUMN estado TO status;

-- Verificar
\d movies;
SELECT 'Tabla renombrada correctamente' as resultado;

