# 🔄 Sistema Híbrido: TMDB + Base de Datos Local

## 📖 Concepto

PlayApp implementa un **sistema híbrido** que combina lo mejor de dos mundos:

1. **TMDB (The Movie Database)** - Exploración de millones de películas
2. **Base de Datos Local** - Almacenamiento selectivo de películas que los usuarios necesitan

## 🎯 ¿Por Qué un Sistema Híbrido?

### Problema Sin Sistema Híbrido

**Opción A: Solo TMDB**
- ❌ No puedes hacer reviews (falta FK en BD)
- ❌ Dependencia total de servicio externo
- ❌ Sin personalización o datos propios

**Opción B: Solo BD Local**
- ❌ Crear cada película manualmente
- ❌ No acceso a millones de películas
- ❌ Trabajo repetitivo e innecesario

### ✅ Solución: Sistema Híbrido

```
📱 Usuario busca "Matrix" en TMDB
    ↓
🔍 Ve resultados de TMDB (millones de películas)
    ↓
⭐ Usuario quiere hacer review de "Matrix"
    ↓
💾 Sistema importa "Matrix" a BD local (automático)
    ↓
📝 Review se guarda con FK válida a BD local
    ↓
🚀 Próxima vez, "Matrix" ya está en BD (más rápido)
```

## 🏗️ Arquitectura del Sistema

### Flujo de Importación

```
┌─────────────────────────────────────────────────────────────┐
│                    FRONTEND / CLIENTE                        │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│  1. Usuario busca película                                   │
│     GET /tmdb/search?query=matrix                           │
│                                                              │
│  2. TMDB retorna resultados (no toca BD local)              │
│     { id: 603, title: "Matrix", posterUrl: "..." }          │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│  3. Usuario quiere hacer review                              │
│     POST /movies/import-from-tmdb/603                       │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│  BACKEND: MovieService.importFromTmdb(603)                  │
│                                                              │
│  4a. ¿Existe película con tmdbId=603 en BD?                 │
│      SI → Retorna película existente (rápido)               │
│      NO → Continúa al paso 4b                               │
│                                                              │
│  4b. Llama a TmdbService.getMovieDetails(603)               │
│      ↓                                                       │
│  4c. Mapea TmdbMovieDetailsDto → MovieDto                   │
│      ↓                                                       │
│  4d. Guarda en BD local (MovieRepository.save)              │
│      ↓                                                       │
│  4e. Retorna MovieDto con id de BD local                    │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│  5. Usuario crea review                                      │
│     POST /reviews                                            │
│     { movieId: 123, rating: 5, comment: "Excelente!" }     │
│                                                              │
│     movieId = ID de nuestra BD local (no TMDB ID)           │
└─────────────────────────────────────────────────────────────┘
```

## 📊 Modelo de Datos

### MovieEntity (BD Local)

```java
@Entity
@Table(name = "platzi_play_peliculas")
public class MovieEntity {
    // Campos originales
    private Long id;                    // PK de nuestra BD
    private String titulo;
    private Integer duracion;
    private String genero;
    private LocalDate fechaEstreno;
    
    // Campos de integración con TMDB
    private Long tmdbId;                // ID en TMDB (UNIQUE)
    private String posterUrl;           // URL completa del poster
    private String backdropUrl;         // URL completa del backdrop
    private String overview;            // Sinopsis
    private String originalTitle;       // Título original
    private Double voteAverage;         // Calificación TMDB (0-10)
    private Integer voteCount;          // Votos en TMDB
    private Double popularity;          // Popularidad TMDB
}
```

### ReviewEntity (BD Local)

```java
@Entity
@Table(name = "reviews")
public class ReviewEntity {
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    
    @ManyToOne
    @JoinColumn(name = "movie_id")  // FK a nuestra BD local
    private MovieEntity movie;
    
    private Integer rating;              // 1-5 estrellas
    private String comment;
}
```

## 🔧 Endpoints del Sistema

### 1. Explorar Películas (TMDB)

```bash
# Buscar películas (no toca BD local)
GET /play-app/api/tmdb/search?query=matrix

# Ver películas populares (no toca BD local)
GET /play-app/api/tmdb/popular

# Ver detalles de película en TMDB (no toca BD local)
GET /play-app/api/tmdb/movie/603
```

**Respuesta:**
```json
{
  "id": 603,
  "title": "Matrix",
  "overview": "Thomas Anderson lleva una doble vida...",
  "posterUrl": "https://image.tmdb.org/t/p/w500/...",
  "voteAverage": 8.2
}
```

### 2. Importar Película a BD Local

```bash
# Importa película de TMDB a nuestra BD
POST /play-app/api/movies/import-from-tmdb/603
```

**Respuesta:**
```json
{
  "id": 123,                    // ID en NUESTRA BD
  "title": "Matrix",
  "tmdbId": 603,                // ID en TMDB
  "posterUrl": "https://...",
  "overview": "...",
  "available": true
}
```

### 3. Ver Películas Locales

```bash
# Solo películas que hemos importado
GET /play-app/api/movies
```

**Respuesta:**
```json
[
  {
    "id": 123,
    "title": "Matrix",
    "tmdbId": 603,
    "averageUserRating": 4.5,  // Calculado de reviews locales
    "reviewCount": 10
  }
]
```

### 4. Hacer Review

```bash
# Review sobre película en BD local
POST /play-app/api/reviews
{
  "movieId": 123,              // ID de nuestra BD (no TMDB)
  "rating": 5,
  "comment": "¡Obra maestra!"
}
```

## 💡 Casos de Uso

### Caso 1: Usuario Nuevo - Primera Review

```
1. Usuario busca "Inception"
   GET /tmdb/search?query=inception
   → Encuentra tmdbId: 27205

2. Usuario quiere hacer review
   POST /movies/import-from-tmdb/27205
   → Sistema importa película (primera vez)
   → Retorna { id: 124, tmdbId: 27205, title: "Inception" }

3. Usuario crea review
   POST /reviews
   { movieId: 124, rating: 5, comment: "Increíble" }
   → Review guardada con FK válida
```

### Caso 2: Usuario Quiere Hacer Review de Película Ya Importada

```
1. Usuario busca "Matrix"
   GET /tmdb/search?query=matrix
   → Encuentra tmdbId: 603

2. Usuario quiere hacer review
   POST /movies/import-from-tmdb/603
   → Sistema detecta que ya existe (tmdbId=603)
   → Retorna película existente { id: 123, ... }
   → NO hace llamada a TMDB (más rápido)

3. Usuario crea review
   POST /reviews
   { movieId: 123, rating: 4, comment: "Muy buena" }
```

### Caso 3: Importar Múltiples Películas

```javascript
// Frontend: Usuario marca 5 películas para agregar
const tmdbIds = [603, 27205, 155, 550, 238];

// Importar todas en paralelo
await Promise.all(
  tmdbIds.map(tmdbId => 
    fetch(`/movies/import-from-tmdb/${tmdbId}`, { method: 'POST' })
  )
);

// Ahora todas están en BD local para reviews
```

## 🎨 Ventajas del Sistema Híbrido

### 1. **Eficiencia**
- Solo guardas lo que necesitas
- No duplicas todo el catálogo de TMDB
- BD local es más rápida que API externa

### 2. **Integridad de Datos**
- Foreign keys válidas entre reviews y películas
- Base de datos relacional consistente
- Transacciones ACID

### 3. **Flexibilidad**
```sql
-- Puedes agregar películas indie o personalizadas
INSERT INTO platzi_play_peliculas (titulo, duracion, genero, tmdb_id)
VALUES ('Mi Película Indie', 90, 'DRAMA', NULL);
-- tmdb_id es NULL porque no está en TMDB
```

### 4. **Enriquecimiento de Datos**
```sql
-- Combinas datos de TMDB con datos locales
SELECT 
  p.title,
  p.vote_average AS tmdb_rating,        -- De TMDB
  AVG(r.rating) AS user_rating,         -- De tus usuarios
  COUNT(r.id) AS review_count           -- De tu BD
FROM platzi_play_peliculas p
LEFT JOIN reviews r ON p.id = r.movie_id
GROUP BY p.id;
```

### 5. **Escalabilidad**
- Si TMDB cae, tu app sigue funcionando con películas importadas
- Puedes cachear búsquedas frecuentes
- Reduces llamadas a API externa (ahorro de costos)

## 🚀 Flujo Completo Ejemplo

### Usuario: "Quiero hacer review de Matrix"

**Paso 1: Buscar**
```bash
GET /tmdb/search?query=matrix
```

**Respuesta:**
```json
{
  "results": [
    { "id": 603, "title": "Matrix", "posterUrl": "..." }
  ]
}
```

**Paso 2: Importar (Frontend hace automáticamente)**
```bash
POST /movies/import-from-tmdb/603
```

**Respuesta:**
```json
{
  "id": 123,           // ID en BD local
  "tmdbId": 603,       // ID en TMDB
  "title": "Matrix",
  "posterUrl": "...",
  "overview": "..."
}
```

**Paso 3: Crear Review**
```bash
POST /reviews
Authorization: Bearer {token}
{
  "movieId": 123,    // Usamos ID de BD local
  "rating": 5,
  "comment": "¡Obra maestra del cine!"
}
```

**Paso 4: Ver Película con Reviews**
```bash
GET /movies/123
```

**Respuesta:**
```json
{
  "id": 123,
  "title": "Matrix",
  "tmdbId": 603,
  "posterUrl": "...",
  "voteAverage": 8.2,          // Calificación TMDB
  "averageUserRating": 4.7,    // Calificación de TUS usuarios
  "reviewCount": 25            // Reviews en tu plataforma
}
```

## 📈 Estadísticas Útiles

```sql
-- Películas más populares en tu plataforma
SELECT 
  p.title,
  p.tmdb_id,
  COUNT(r.id) as review_count,
  AVG(r.rating) as avg_rating
FROM platzi_play_peliculas p
LEFT JOIN reviews r ON p.id = r.movie_id
GROUP BY p.id
ORDER BY review_count DESC
LIMIT 10;

-- Películas importadas vs creadas manualmente
SELECT 
  CASE 
    WHEN tmdb_id IS NOT NULL THEN 'Importadas de TMDB'
    ELSE 'Creadas manualmente'
  END as source,
  COUNT(*) as count
FROM platzi_play_peliculas
GROUP BY source;
```

## 🎓 Para tu CV

Este sistema híbrido demuestra:

✅ **Arquitectura avanzada** - Integración de sistemas externos con BD local  
✅ **Optimización** - Importación selectiva vs carga completa  
✅ **Diseño de APIs** - RESTful, bien documentado  
✅ **Integridad de datos** - Foreign keys, transacciones  
✅ **Escalabilidad** - Sistema preparado para crecer  
✅ **Experiencia de usuario** - Acceso a millones de películas sin cargar todo  

---

**¡Sistema híbrido en producción!** 🎬✨

