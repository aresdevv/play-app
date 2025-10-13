# 🎬 Guía de Integración con TMDB API

## Descripción

PlayApp ahora cuenta con integración completa con The Movie Database (TMDB), una de las bases de datos de películas más completas del mundo. Esto permite acceder a información detallada de millones de películas, incluyendo sinopsis, posters, calificaciones, y más.

## 🔑 Configuración

La API Key de TMDB ya está configurada en el archivo `application-dev.properties`:

```properties
tmdb.api.key=ec241bad722d50d9ad3d282a347e1ac9
tmdb.api.base-url=https://api.themoviedb.org/3
tmdb.api.image-base-url=https://image.tmdb.org/t/p
```

## 📡 Endpoints Disponibles

Todos los endpoints están bajo la ruta base: `/play-app/api/tmdb`

### 1. Buscar Películas por Título

**Endpoint:** `GET /tmdb/search`

**Parámetros:**
- `query` (requerido): Título de la película a buscar
- `page` (opcional): Número de página (por defecto 1)

**Ejemplo:**
```bash
GET /play-app/api/tmdb/search?query=matrix&page=1
```

**Respuesta:**
```json
{
  "page": 1,
  "totalResults": 20,
  "totalPages": 1,
  "results": [
    {
      "id": 603,
      "title": "Matrix",
      "originalTitle": "The Matrix",
      "overview": "Thomas Anderson lleva una doble vida...",
      "posterPath": "/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg",
      "posterUrl": "https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg",
      "backdropPath": "/fNG7i7RqMErkcqhohV2a6cV1Ehy.jpg",
      "backdropUrl": "https://image.tmdb.org/t/p/original/fNG7i7RqMErkcqhohV2a6cV1Ehy.jpg",
      "releaseDate": "1999-03-30",
      "voteAverage": 8.2,
      "voteCount": 25000,
      "popularity": 65.23,
      "genreIds": [28, 878],
      "adult": false,
      "originalLanguage": "en"
    }
  ]
}
```

### 2. Obtener Detalles de una Película

**Endpoint:** `GET /tmdb/movie/{tmdbId}`

**Parámetros:**
- `tmdbId` (requerido): ID de la película en TMDB

**Ejemplo:**
```bash
GET /play-app/api/tmdb/movie/603
```

**Respuesta:**
```json
{
  "id": 603,
  "title": "Matrix",
  "originalTitle": "The Matrix",
  "overview": "Thomas Anderson lleva una doble vida...",
  "tagline": "La realidad es solo una ilusión",
  "posterPath": "/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg",
  "posterUrl": "https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg",
  "backdropPath": "/fNG7i7RqMErkcqhohV2a6cV1Ehy.jpg",
  "backdropUrl": "https://image.tmdb.org/t/p/original/fNG7i7RqMErkcqhohV2a6cV1Ehy.jpg",
  "releaseDate": "1999-03-30",
  "voteAverage": 8.2,
  "voteCount": 25000,
  "popularity": 65.23,
  "runtime": 136,
  "budget": 63000000,
  "revenue": 463517383,
  "status": "Released",
  "genres": [
    {"id": 28, "name": "Acción"},
    {"id": 878, "name": "Ciencia ficción"}
  ],
  "productionCompanies": [
    {
      "id": 79,
      "name": "Village Roadshow Pictures",
      "logoPath": "/tpFpsqbleCzEE2p5EgvUq6ozfCA.png",
      "originCountry": "US"
    }
  ],
  "homepage": "http://www.warnerbros.com/matrix",
  "imdbId": "tt0133093"
}
```

### 3. Películas Populares

**Endpoint:** `GET /tmdb/popular`

**Parámetros:**
- `page` (opcional): Número de página (por defecto 1)

**Ejemplo:**
```bash
GET /play-app/api/tmdb/popular?page=1
```

**Descripción:** Obtiene las películas más populares del momento según TMDB.

### 4. Películas Mejor Calificadas

**Endpoint:** `GET /tmdb/top-rated`

**Parámetros:**
- `page` (opcional): Número de página (por defecto 1)

**Ejemplo:**
```bash
GET /play-app/api/tmdb/top-rated?page=1
```

**Descripción:** Obtiene las películas mejor calificadas de todos los tiempos.

### 5. Películas en Cines

**Endpoint:** `GET /tmdb/now-playing`

**Parámetros:**
- `page` (opcional): Número de página (por defecto 1)

**Ejemplo:**
```bash
GET /play-app/api/tmdb/now-playing?page=1
```

**Descripción:** Obtiene películas que están actualmente en cines (región México).

### 6. Próximos Estrenos

**Endpoint:** `GET /tmdb/upcoming`

**Parámetros:**
- `page` (opcional): Número de página (por defecto 1)

**Ejemplo:**
```bash
GET /play-app/api/tmdb/upcoming?page=1
```

**Descripción:** Obtiene películas que se estrenarán próximamente (región México).

## 🖼️ URLs de Imágenes

TMDB proporciona imágenes en diferentes tamaños. El servicio ya construye URLs completas para ti:

### Posters
- **Tamaño usado:** `w500` (500px de ancho)
- **URL completa:** `https://image.tmdb.org/t/p/w500/{posterPath}`

### Backdrops (fondos)
- **Tamaño usado:** `original` (resolución completa)
- **URL completa:** `https://image.tmdb.org/t/p/original/{backdropPath}`

### Otros tamaños disponibles
Si necesitas otros tamaños, puedes construir las URLs manualmente:

**Posters:** `w92`, `w154`, `w185`, `w342`, `w500`, `w780`, `original`

**Backdrops:** `w300`, `w780`, `w1280`, `original`

## 🌐 Idioma

Todos los endpoints están configurados para devolver información en **español (es-MX)**, incluyendo:
- Títulos traducidos
- Sinopsis en español
- Nombres de géneros en español

## 🚀 Casos de Uso

### 1. Enriquecer tu base de datos local
Puedes buscar una película en TMDB y guardar su información en tu base de datos local:

```bash
# 1. Buscar película
GET /play-app/api/tmdb/search?query=inception

# 2. Obtener detalles completos
GET /play-app/api/tmdb/movie/27205

# 3. Guardar en tu base de datos
POST /play-app/api/movies
{
  "title": "Inception",
  "genre": "SCIFI",
  "director": "Christopher Nolan",
  "releaseYear": 2010,
  "description": "Dom Cobb es un ladrón experto...",
  "posterUrl": "https://image.tmdb.org/t/p/w500/..."
}
```

### 2. Mostrar películas populares en tu frontend
```bash
GET /play-app/api/tmdb/popular?page=1
```

### 3. Crear una sección de "En Cines Ahora"
```bash
GET /play-app/api/tmdb/now-playing?page=1
```

### 4. Permitir a usuarios buscar películas
```bash
GET /play-app/api/tmdb/search?query={input_del_usuario}
```

## 📊 Respuestas Paginadas

Todos los endpoints que devuelven listas incluyen información de paginación:

```json
{
  "page": 1,           // Página actual
  "totalResults": 100, // Total de resultados
  "totalPages": 5,     // Total de páginas
  "results": [...]     // Resultados de la página actual
}
```

## 🔒 Seguridad

- Los endpoints de TMDB son **públicos** (no requieren autenticación JWT)
- Si deseas protegerlos, puedes actualizar `SecurityConfig.java`
- La API Key se mantiene segura en el backend y no se expone al frontend

## 🎯 Próximos Pasos Recomendados

1. **Crear endpoint para importar películas:** Agregar un endpoint que tome un ID de TMDB y cree automáticamente una película en tu base de datos
2. **Sistema de caché:** Implementar Redis para cachear respuestas de TMDB y reducir llamadas a la API
3. **Sincronización automática:** Crear un job que actualice información de películas populares diariamente
4. **Búsqueda híbrida:** Combinar búsquedas en tu base de datos local con búsquedas en TMDB

## 📝 Notas

- TMDB tiene un límite de **40 requests por segundo**
- Para desarrollo personal, este límite es más que suficiente
- Los datos de TMDB están en constante actualización
- Todas las respuestas incluyen URLs completas de imágenes para fácil uso en frontend

## 🔗 Recursos

- [Documentación oficial TMDB API](https://developer.themoviedb.org/docs)
- [Swagger UI de tu aplicación](http://localhost:8090/play-app/api/swagger-ui.html) - Prueba los endpoints interactivamente

---

**¡Disfruta de tu integración con TMDB!** 🎬✨

