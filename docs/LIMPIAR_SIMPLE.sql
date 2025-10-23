-- Script simple: Solo limpiar datos (sin renombrar)
-- Si solo quieres borrar los datos y mantener nombres actuales

-- Eliminar reviews primero (tienen FK a movies)
TRUNCATE TABLE reviews RESTART IDENTITY CASCADE;

-- Eliminar películas
TRUNCATE TABLE platzi_play_peliculas RESTART IDENTITY CASCADE;

-- Verificar
SELECT COUNT(*) FROM platzi_play_peliculas; -- Debe ser 0
SELECT COUNT(*) FROM reviews;              -- Debe ser 0

SELECT 'Tablas limpiadas correctamente' as resultado;

