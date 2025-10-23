-- Script para limpiar datos y renombrar tablas
-- Fecha: 2025-10-13
-- Autor: @aresdevv

-- ========================================
-- PARTE 1: LIMPIAR DATOS
-- ========================================

-- Eliminar todos los reviews primero (tienen FK a películas y usuarios)
TRUNCATE TABLE reviews CASCADE;

-- Eliminar todas las películas
TRUNCATE TABLE platzi_play_peliculas RESTART IDENTITY CASCADE;

-- Eliminar todos los usuarios (opcional - descomenta si quieres)
-- TRUNCATE TABLE platzi_play_usuarios RESTART IDENTITY CASCADE;

-- ========================================
-- PARTE 2: RENOMBRAR TABLAS
-- ========================================

-- Renombrar tabla de películas
ALTER TABLE platzi_play_peliculas RENAME TO movies;

-- Renombrar secuencia de películas (si existe)
ALTER SEQUENCE IF EXISTS platzi_play_peliculas_id_seq RENAME TO movies_id_seq;

-- Renombrar tabla de usuarios
ALTER TABLE platzi_play_usuarios RENAME TO users;

-- Renombrar secuencia de usuarios (si existe)
ALTER SEQUENCE IF EXISTS platzi_play_usuarios_id_seq RENAME TO users_id_seq;

-- ========================================
-- PARTE 3: ACTUALIZAR COLUMNAS DE MOVIES
-- ========================================

-- Renombrar columnas en español a inglés
ALTER TABLE movies RENAME COLUMN titulo TO title;
ALTER TABLE movies RENAME COLUMN duracion TO duration;
ALTER TABLE movies RENAME COLUMN genero TO genre;
ALTER TABLE movies RENAME COLUMN fecha_estreno TO release_date;
ALTER TABLE movies RENAME COLUMN clasificacion TO rating;
ALTER TABLE movies RENAME COLUMN estado TO status;

-- ========================================
-- PARTE 4: ACTUALIZAR COLUMNAS DE USERS
-- ========================================

-- Renombrar columnas en español a inglés
ALTER TABLE users RENAME COLUMN nombre_completo TO full_name;
ALTER TABLE users RENAME COLUMN fecha_creacion TO created_at;
ALTER TABLE users RENAME COLUMN ultimo_acceso TO last_access;
ALTER TABLE users RENAME COLUMN activo TO active;
ALTER TABLE users RENAME COLUMN email_verificado TO email_verified;

-- ========================================
-- PARTE 5: VERIFICACIÓN
-- ========================================

-- Ver estructura de tabla movies
\d movies;

-- Ver estructura de tabla users
\d users;

-- Verificar que no hay datos
SELECT COUNT(*) as total_movies FROM movies;
SELECT COUNT(*) as total_reviews FROM reviews;
SELECT COUNT(*) as total_users FROM users;

-- Ver todas las tablas
\dt;

-- ========================================
-- RESULTADO ESPERADO
-- ========================================

-- Antes:
--   platzi_play_peliculas (con columnas en español)
--   platzi_play_usuarios (con columnas en español)
--   reviews
--
-- Después:
--   movies (con columnas en inglés)
--   users (con columnas en inglés)
--   reviews
--
-- Todos los datos de movies y reviews eliminados
-- IDs reiniciados a 1
-- Usuarios conservados (a menos que se descomente la limpieza)

