# 🚀 Guía Rápida: Sistema Híbrido

## ⚡ Inicio Rápido en 5 Minutos

### 1. Configuración Inicial

Ya tienes configurado:
- ✅ API Key de TMDB en `application-dev.properties`
- ✅ API Key de Google Gemini
- ✅ Base de datos PostgreSQL

### 2. Migrar Base de Datos (Solo si ya tienes BD existente)

```bash
# Opción A: Desde terminal
psql -U aresdevv -d platzi_play_db -f src/main/resources/migration-tmdb-fields.sql

# Opción B: Desde psql
\i src/main/resources/migration-tmdb-fields.sql
```

Si es una BD nueva, Hibernate creará las columnas automáticamente. ✅

### 3. Iniciar la Aplicación

```bash
./gradlew bootRun
```

Espera a ver:
```
Started PlatziPlayApplication in X seconds
```

### 4. Probar el Sistema Híbrido

#### Paso 1: Registrar usuario (PRIMERO - necesario para reviews)

**⚠️ IMPORTANTE:** Los endpoints de reviews están protegidos, así que necesitas autenticarte PRIMERO.

```bash
curl -X POST "http://localhost:8090/play-app/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userInfo": {
    "id": 1,
    "username": "testuser",
    "email": "test@example.com"
  }
}
```

#### Paso 2: Buscar película en TMDB

Ahora que estás autenticado, busca películas:

```bash
curl "http://localhost:8090/play-app/api/tmdb/search?query=matrix"
```

**Respuesta:**
```json
{
  "results": [
    {
      "id": 603,
      "title": "Matrix",
      "posterUrl": "https://image.tmdb.org/t/p/w500/...",
      "overview": "..."
    }
  ]
}
```

#### Paso 3: Importar película a BD local

Antes de hacer review, importa la película:

```bash
curl -X POST "http://localhost:8090/play-app/api/movies/import-from-tmdb/603"
```

**Respuesta:**
```json
{
  "id": 1,           // ← ID en TU base de datos
  "title": "Matrix",
  "tmdbId": 603,     // ← ID en TMDB
  "posterUrl": "https://...",
  "duration": 136,
  "genre": "SCI_FI",
  "available": true
}
```

**💡 TIP:** Si otro usuario ya importó esta película, recibirás la misma película existente (id=1). No se crea duplicado.

#### Paso 4: Hacer review de la película

```bash
curl -X POST "http://localhost:8090/play-app/api/reviews" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TU_TOKEN_AQUI" \
  -d '{
    "movieId": 1,
    "rating": 5,
    "comment": "¡Obra maestra del cine!"
  }'
```

**Respuesta:**
```json
{
  "id": 1,
  "movieId": 1,
  "userId": 1,
  "rating": 5,
  "comment": "¡Obra maestra del cine!",
  "createdAt": "2025-10-13T10:30:00"
}
```

#### Paso 5: Ver película con reviews

```bash
curl "http://localhost:8090/play-app/api/movies/1"
```

**Respuesta:**
```json
{
  "id": 1,
  "title": "Matrix",
  "tmdbId": 603,
  "voteAverage": 8.2,          // Calificación TMDB
  "averageUserRating": 5.0,    // Calificación de TUS usuarios
  "reviewCount": 1,
  "posterUrl": "https://...",
  "overview": "..."
}
```

## 🎮 Probar con Swagger UI

**Abre tu navegador:**
```
http://localhost:8090/play-app/api/swagger-ui.html
```

### Flujo en Swagger:

1. **Buscar película**
   - Endpoint: `GET /tmdb/search`
   - Parámetro: `query = matrix`

2. **Importar película**
   - Endpoint: `POST /movies/import-from-tmdb/{tmdbId}`
   - Path: `tmdbId = 603`

3. **Registrar usuario**
   - Endpoint: `POST /auth/register`

4. **Autenticarse (copiar token)**
   - Endpoint: `POST /auth/login`
   - Click en "Authorize" 🔒
   - Pegar token

5. **Crear review**
   - Endpoint: `POST /reviews`
   - Body: `{ "movieId": 1, "rating": 5, "comment": "..." }`

## 📊 Verificar en Base de Datos

```sql
-- Ver películas importadas
SELECT id, titulo, tmdb_id, poster_url 
FROM platzi_play_peliculas;

-- Ver reviews con información de película
SELECT 
  m.titulo,
  m.tmdb_id,
  r.rating,
  r.comment,
  r.created_at
FROM reviews r
JOIN platzi_play_peliculas m ON r.movie_id = m.id;

-- Estadísticas
SELECT 
  m.titulo,
  COUNT(r.id) as total_reviews,
  AVG(r.rating) as avg_rating
FROM platzi_play_peliculas m
LEFT JOIN reviews r ON m.id = r.movie_id
GROUP BY m.id;
```

## 🎯 Casos de Uso Completos

### Caso 1: Usuario busca y hace review de película

```javascript
// 1. Buscar
const searchResults = await fetch('/tmdb/search?query=inception');
const movies = await searchResults.json();
// movies.results[0].id = 27205 (TMDB ID)

// 2. Importar
const imported = await fetch('/movies/import-from-tmdb/27205', { 
  method: 'POST' 
});
const movie = await imported.json();
// movie.id = 2 (TU BD ID)

// 3. Hacer review
const review = await fetch('/reviews', {
  method: 'POST',
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    movieId: movie.id,  // Usar ID de tu BD
    rating: 5,
    comment: 'Increíble'
  })
});
```

### Caso 2: Importar películas populares automáticamente

```javascript
// 1. Obtener populares de TMDB
const popular = await fetch('/tmdb/popular');
const { results } = await popular.json();

// 2. Importar las top 10
const imported = await Promise.all(
  results.slice(0, 10).map(movie =>
    fetch(`/movies/import-from-tmdb/${movie.id}`, { method: 'POST' })
      .then(r => r.json())
  )
);

console.log(`Importadas ${imported.length} películas populares`);
```

## ❗ Troubleshooting

### Error: "Column tmdb_id does not exist"

**Solución:** Ejecuta la migración SQL
```bash
psql -U aresdevv -d platzi_play_db -f src/main/resources/migration-tmdb-fields.sql
```

### Error: "TMDB API key invalid"

**Solución:** Verifica `application-dev.properties`
```properties
tmdb.api.key=ec241bad722d50d9ad3d282a347e1ac9
```

### Error: "Movie already exists with title"

**Solución:** La restricción UNIQUE en título fue removida. Si persiste:
```sql
ALTER TABLE platzi_play_peliculas DROP CONSTRAINT IF EXISTS platzi_play_peliculas_titulo_key;
```

## 📝 Endpoints Resumidos

| Acción | Método | Endpoint | Auth |
|--------|--------|----------|------|
| Buscar en TMDB | GET | `/tmdb/search?query={q}` | No |
| Importar película | POST | `/movies/import-from-tmdb/{tmdbId}` | No |
| Registrar usuario | POST | `/auth/register` | No |
| Login | POST | `/auth/login` | No |
| Crear review | POST | `/reviews` | Sí |
| Ver películas | GET | `/movies` | No |
| Ver película | GET | `/movies/{id}` | No |

## 🎉 ¡Listo!

Tu sistema híbrido está funcionando. Ahora puedes:

✅ Buscar en millones de películas (TMDB)  
✅ Importar solo las que necesitas (BD local)  
✅ Hacer reviews con integridad referencial  
✅ Combinar datos de TMDB con datos de usuarios  

---

**¿Preguntas?** Lee la guía completa: [SISTEMA_HIBRIDO.md](SISTEMA_HIBRIDO.md)

