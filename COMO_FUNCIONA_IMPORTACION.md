# 🔄 Cómo Funciona la Importación de Películas

## 🎯 Preguntas Frecuentes

### ❓ ¿Qué pasa si dos usuarios quieren hacer review de la misma película?

**Respuesta:** Solo se importa UNA VEZ. La segunda vez retorna la película existente.

---

## 📝 Escenario Paso a Paso

### Usuario 1: Ana (Primera vez)

```bash
# 1. Ana busca "Matrix" en TMDB
GET /tmdb/search?query=matrix
# Respuesta: tmdbId = 603

# 2. Ana quiere hacer review → Importa
POST /movies/import-from-tmdb/603
```

**¿Qué hace el sistema?**

```java
public MovieDto importFromTmdb(Long tmdbId) {
    // 1. Verificar si ya existe película con tmdbId=603
    Optional<MovieDto> existingMovie = findByTmdbId(tmdbId);
    
    if (existingMovie.isPresent()) {
        // NO ENTRA AQUÍ (es la primera vez)
        return existingMovie.get();
    }

    // 2. Llama a TMDB API
    TmdbMovieDetailsDto tmdbDetails = tmdbService.getMovieDetails(603);
    
    // 3. Mapea datos
    MovieDto movieToSave = mapTmdbToMovieDto(tmdbDetails);
    
    // 4. Guarda en BD ✅
    return this.movieRepository.save(movieToSave);
    // Retorna: { id: 1, tmdbId: 603, title: "Matrix", ... }
}
```

**Resultado:**
```json
{
  "id": 1,        // ← ID en BD local (NUEVO)
  "tmdbId": 603,  // ← ID en TMDB
  "title": "Matrix"
}
```

**Base de datos:**
```sql
SELECT id, titulo, tmdb_id FROM platzi_play_peliculas;

 id |  titulo | tmdb_id
----|---------|--------
  1 | Matrix  |   603
```

### Usuario 2: Carlos (Misma película)

```bash
# 1. Carlos busca "Matrix" en TMDB
GET /tmdb/search?query=matrix
# Respuesta: tmdbId = 603 (el mismo)

# 2. Carlos quiere hacer review → Importa
POST /movies/import-from-tmdb/603
```

**¿Qué hace el sistema?**

```java
public MovieDto importFromTmdb(Long tmdbId) {
    // 1. Verificar si ya existe película con tmdbId=603
    Optional<MovieDto> existingMovie = findByTmdbId(603);
    
    if (existingMovie.isPresent()) {
        // ✅ SÍ EXISTE! (Ana ya la importó)
        return existingMovie.get();  // ← Retorna la existente
        // NO llama a TMDB
        // NO guarda en BD
        // NO crea duplicado
    }
    
    // NO llega aquí
}
```

**Resultado:**
```json
{
  "id": 1,        // ← MISMO ID que Ana recibió
  "tmdbId": 603,
  "title": "Matrix"
}
```

**Base de datos (NO cambia):**
```sql
SELECT id, titulo, tmdb_id FROM platzi_play_peliculas;

 id |  titulo | tmdb_id
----|---------|--------
  1 | Matrix  |   603    ← Sigue siendo UNA sola fila
```

---

## 🔍 Método de Verificación

El código verifica duplicados usando `tmdbId`:

```java
private Optional<MovieDto> findByTmdbId(Long tmdbId) {
    return this.movieRepository.getAll().stream()
            .filter(movie -> movie.tmdbId() != null && movie.tmdbId().equals(tmdbId))
            .findFirst();
}
```

**Flujo:**
1. Obtiene TODAS las películas de BD local
2. Filtra por `tmdbId` == 603
3. Si encuentra una → Retorna `Optional<MovieDto>` con la película
4. Si no encuentra → Retorna `Optional.empty()`

---

## 📊 Ejemplo con 3 Usuarios

### Situación: 3 usuarios quieren hacer review de "Matrix" (tmdbId=603)

```
Usuario Ana (10:00 AM):
  POST /movies/import-from-tmdb/603
  → BD vacía
  → Llama a TMDB ✅
  → Guarda película (id=1)
  → Retorna: { id: 1, tmdbId: 603 }

Usuario Carlos (10:05 AM):
  POST /movies/import-from-tmdb/603
  → Encuentra tmdbId=603 en BD ✅
  → NO llama a TMDB ⚡ (más rápido)
  → Retorna película existente: { id: 1, tmdbId: 603 }

Usuario Laura (10:10 AM):
  POST /movies/import-from-tmdb/603
  → Encuentra tmdbId=603 en BD ✅
  → NO llama a TMDB ⚡
  → Retorna película existente: { id: 1, tmdbId: 603 }
```

**Base de datos final:**
```sql
SELECT * FROM platzi_play_peliculas;

 id |  titulo | tmdb_id | ...
----|---------|---------|----
  1 | Matrix  |   603   | ...   ← UNA sola película
```

**Reviews de los 3 usuarios:**
```sql
SELECT * FROM reviews;

 id | user_id | movie_id | rating | comment
----|---------|----------|--------|--------------------
  1 |    1    |    1     |   5    | Excelente! (Ana)
  2 |    2    |    1     |   4    | Muy buena (Carlos)
  3 |    3    |    1     |   5    | Obra maestra (Laura)
                 ↑
          TODOS apuntan al MISMO movie_id
```

---

## 🔐 Constraint de Base de Datos

La columna `tmdb_id` tiene un constraint UNIQUE:

```sql
ALTER TABLE platzi_play_peliculas
    ADD COLUMN tmdb_id BIGINT UNIQUE;
                              ↑
                          UNIQUE = no duplicados
```

**¿Qué pasa si intentas insertar duplicado?**

```sql
-- Primera inserción ✅
INSERT INTO platzi_play_peliculas (titulo, tmdb_id)
VALUES ('Matrix', 603);

-- Segunda inserción ❌
INSERT INTO platzi_play_peliculas (titulo, tmdb_id)
VALUES ('Matrix', 603);
-- ERROR: duplicate key value violates unique constraint
```

**Por eso el código verifica ANTES de insertar** ✅

---

## 💡 Ventajas de Este Diseño

### 1. **Evita Duplicados**
```sql
-- Solo UNA película por tmdbId
SELECT COUNT(*) FROM platzi_play_peliculas WHERE tmdb_id = 603;
-- Resultado: 1 (siempre)
```

### 2. **Más Rápido**
```
Primera importación:  ~500ms (llama a TMDB)
Segunda importación:  ~10ms  (solo BD local) ⚡
```

### 3. **Ahorra Llamadas a API**
```
Sin verificación:
  - 1000 usuarios → 1000 llamadas a TMDB 💸

Con verificación:
  - 1000 usuarios → 1 llamada a TMDB ✅
  - Las otras 999 usan BD local ⚡
```

### 4. **Datos Consistentes**
```
Todos los usuarios ven la MISMA información base:
- Mismo poster
- Misma sinopsis
- Misma duración
- Mismo ID (para reviews)
```

### 5. **Reviews Relacionadas**
```sql
-- Ver TODAS las reviews de Matrix
SELECT u.username, r.rating, r.comment
FROM reviews r
JOIN users u ON r.user_id = u.id
JOIN platzi_play_peliculas m ON r.movie_id = m.id
WHERE m.tmdb_id = 603;

-- Resultado:
-- Ana      | 5 | Excelente!
-- Carlos   | 4 | Muy buena
-- Laura    | 5 | Obra maestra
-- (todos apuntan a la MISMA película)
```

---

## 🧪 Prueba Práctica

### Prueba el Sistema de Verificación

```bash
# Terminal 1: Importar por primera vez
curl -X POST "http://localhost:8090/play-app/api/movies/import-from-tmdb/603"
# Respuesta: { "id": 1, "tmdbId": 603, "title": "Matrix" }
# Tiempo: ~500ms

# Terminal 2: Importar la misma (inmediatamente)
curl -X POST "http://localhost:8090/play-app/api/movies/import-from-tmdb/603"
# Respuesta: { "id": 1, "tmdbId": 603, "title": "Matrix" }
# Tiempo: ~10ms ⚡ (mucho más rápido)

# Verificar en BD
psql -U aresdevv -d platzi_play_db -c "SELECT id, titulo, tmdb_id FROM platzi_play_peliculas WHERE tmdb_id = 603;"

#  id |  titulo | tmdb_id
# ----|---------|--------
#   1 | Matrix  |   603     ← Solo UNA fila
```

---

## 🎯 Flujo Visual

```
┌─────────────────────────────────────────────────────────┐
│  Usuario 1 quiere hacer review de "Matrix" (tmdbId=603) │
└─────────────────┬───────────────────────────────────────┘
                  │
                  ▼
    ┌─────────────────────────────────┐
    │ POST /movies/import-from-tmdb/603│
    └─────────────────┬───────────────┘
                      │
                      ▼
    ┌─────────────────────────────────────┐
    │ ¿Existe película con tmdbId=603?    │
    │ SELECT * WHERE tmdb_id = 603        │
    └─────────┬─────────────────┬─────────┘
              │                 │
         NO ✗ │                 │ SÍ ✓
              │                 │
              ▼                 ▼
    ┌─────────────────┐  ┌──────────────────┐
    │ Llamar TMDB API │  │ Retornar película│
    │ Guardar en BD   │  │ existente        │
    │ Retornar nueva  │  │ (más rápido ⚡)  │
    └─────────────────┘  └──────────────────┘
              │                 │
              ▼                 ▼
    ┌─────────────────────────────────────┐
    │ Usuario recibe: { id: 1, ... }      │
    │ Puede hacer review con movieId=1    │
    └─────────────────────────────────────┘
```

---

## 📝 Código Completo del Flujo

```java
// MovieService.java - líneas 55-70
public MovieDto importFromTmdb(Long tmdbId) {
    // PASO 1: Verificar si existe
    Optional<MovieDto> existingMovie = findByTmdbId(tmdbId);
    
    if (existingMovie.isPresent()) {
        // ✅ Ya existe → Retornar
        System.out.println("Película ya importada, retornando existente");
        return existingMovie.get();
    }

    // PASO 2: No existe → Importar de TMDB
    System.out.println("Película nueva, importando de TMDB");
    TmdbMovieDetailsDto tmdbDetails = tmdbService.getMovieDetails(tmdbId);

    // PASO 3: Mapear y guardar
    MovieDto movieToSave = mapTmdbToMovieDto(tmdbDetails);
    return this.movieRepository.save(movieToSave);
}

// Método auxiliar de búsqueda
private Optional<MovieDto> findByTmdbId(Long tmdbId) {
    return this.movieRepository.getAll().stream()
            .filter(movie -> movie.tmdbId() != null && 
                           movie.tmdbId().equals(tmdbId))
            .findFirst();
}
```

---

## ✅ Resumen

**Pregunta:** ¿Qué pasa cuando dos usuarios quieren hacer review de la misma película?

**Respuesta:**
1. **Usuario 1** importa → Película se guarda en BD (id=1, tmdbId=603)
2. **Usuario 2** importa → Sistema detecta que tmdbId=603 ya existe
3. **Usuario 2** recibe la MISMA película (id=1) sin crear duplicado
4. **Ambos** pueden hacer reviews sobre `movieId=1`
5. **Resultado:** UNA película, MÚLTIPLES reviews ✅

**Ventajas:**
- ✅ Sin duplicados
- ✅ Más rápido (no llama TMDB cada vez)
- ✅ Datos consistentes
- ✅ Reviews relacionadas correctamente
- ✅ Ahorra llamadas a API

---

**¡Exactamente como pensabas!** 🎯

