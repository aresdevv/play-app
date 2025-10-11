# 🌟 Sistema de Calificaciones y Reviews - Implementación Completa

## ✅ ¿Qué se ha implementado?

Se ha implementado un **sistema completo de calificaciones y reviews** para películas que incluye:

### 1. **Entidades y Base de Datos**
- ✅ `ReviewEntity` - Entidad JPA con relaciones a Usuario y Película
- ✅ Tabla `reviews` con constraints y índices optimizados
- ✅ Calificaciones de 1 a 5 estrellas
- ✅ Comentarios opcionales
- ✅ Timestamps de creación y actualización
- ✅ Restricción única: un usuario solo puede hacer una review por película

### 2. **DTOs (Data Transfer Objects)**
- ✅ `ReviewDto` - Para lectura de reviews
- ✅ `CreateReviewDto` - Para crear nuevas reviews
- ✅ `UpdateReviewDto` - Para actualizar reviews existentes

### 3. **Capa de Persistencia**
- ✅ `CrudReviewEntity` - Repositorio JPA con queries personalizadas
- ✅ `ReviewEntityRepository` - Implementación con lógica de negocio
- ✅ Cálculo automático de rating promedio
- ✅ Conteo de reviews por película

### 4. **Capa de Negocio**
- ✅ `ReviewService` - Servicio con toda la lógica de reviews
- ✅ Validación de permisos (solo el dueño puede modificar/eliminar su review)
- ✅ Validación de duplicados (no se puede hacer dos reviews a la misma película)

### 5. **Capa de Presentación (API REST)**
- ✅ `ReviewController` - 10 endpoints REST documentados con Swagger
- ✅ Seguridad integrada con JWT
- ✅ Validación de datos con Bean Validation

### 6. **Seguridad y Autenticación**
- ✅ `JwtAuthenticationFilter` - Filtro para validar tokens JWT
- ✅ Endpoints protegidos según roles (ADMIN/USER)
- ✅ CRUD de películas: **solo ADMIN**
- ✅ Reviews: **usuarios autenticados**
- ✅ Lectura de películas y reviews: **público**

### 7. **Excepciones Personalizadas**
- ✅ `ReviewNotFoundException`
- ✅ `ReviewAlreadyExistsException`
- ✅ `UnauthorizedReviewAccessException`
- ✅ Manejo global en `RestExceptionHandler`

### 8. **Integración con MovieDto**
- ✅ `averageUserRating` - Calificación promedio basada en reviews
- ✅ `reviewCount` - Cantidad total de reviews
- ✅ Cálculo automático al obtener películas

### 9. **Documentación OpenAPI/Swagger**
- ✅ Configuración de Bearer Token JWT
- ✅ Todos los endpoints documentados
- ✅ Ejemplos y descripciones

## 📊 Endpoints Disponibles

### Reviews

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| POST | `/reviews` | Crear una review | 🔒 USER |
| PUT | `/reviews/{id}` | Actualizar propia review | 🔒 USER |
| DELETE | `/reviews/{id}` | Eliminar propia review | 🔒 USER |
| GET | `/reviews/{id}` | Obtener review por ID | 🌐 Público |
| GET | `/reviews/movie/{movieId}` | Obtener todas las reviews de una película | 🌐 Público |
| GET | `/reviews/user/{userId}` | Obtener todas las reviews de un usuario | 🌐 Público |
| GET | `/reviews/user/{userId}/movie/{movieId}` | Obtener review específica | 🌐 Público |
| GET | `/reviews/movie/{movieId}/average` | Obtener rating promedio de película | 🌐 Público |
| GET | `/reviews/movie/{movieId}/count` | Obtener cantidad de reviews | 🌐 Público |

### Películas (Actualizado con seguridad)

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| GET | `/movies` | Listar todas las películas | 🌐 Público |
| GET | `/movies/{id}` | Obtener película por ID | 🌐 Público |
| POST | `/movies` | Crear nueva película | 🔒 ADMIN |
| PUT | `/movies/{id}` | Actualizar película | 🔒 ADMIN |
| DELETE | `/movies/{id}` | Eliminar película | 🔒 ADMIN |
| POST | `/movies/suggest` | Sugerencias con IA | 🔒 USER |

## 🚀 Cómo Usar

### 1. **Crear la tabla de reviews**

Ejecutar el script SQL:
```bash
psql -U tu_usuario -d play_app -f src/main/resources/review-schema.sql
```

O dejarlo que JPA lo cree automáticamente (ya está configurado con `@Entity`).

### 2. **Compilar el proyecto**

```bash
./gradlew clean build
```

Esto generará los mappers de MapStruct automáticamente.

### 3. **Ejecutar la aplicación**

```bash
./gradlew bootRun
```

### 4. **Acceder a Swagger UI**

```
http://localhost:8090/swagger-ui.html
```

## 📝 Ejemplos de Uso

### 1. **Registrar un usuario**

```bash
POST /auth/register
{
  "username": "juan",
  "email": "juan@example.com",
  "password": "password123",
  "fullName": "Juan Pérez"
}
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userInfo": {
    "id": 1,
    "username": "juan",
    "email": "juan@example.com",
    "fullName": "Juan Pérez",
    "role": "USER"
  }
}
```

### 2. **Crear una review (autenticado)**

```bash
POST /reviews
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

{
  "movieId": 1,
  "rating": 5,
  "comment": "¡Excelente película! Me encantó la trama y las actuaciones."
}
```

**Respuesta:**
```json
{
  "id": 1,
  "userId": 1,
  "username": "juan",
  "movieId": 1,
  "movieTitle": "El Padrino",
  "rating": 5,
  "comment": "¡Excelente película! Me encantó la trama y las actuaciones.",
  "createdAt": "2025-10-10T15:30:00",
  "updatedAt": "2025-10-10T15:30:00"
}
```

### 3. **Obtener reviews de una película**

```bash
GET /reviews/movie/1
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "userId": 1,
    "username": "juan",
    "movieId": 1,
    "movieTitle": "El Padrino",
    "rating": 5,
    "comment": "¡Excelente película!",
    "createdAt": "2025-10-10T15:30:00",
    "updatedAt": "2025-10-10T15:30:00"
  },
  {
    "id": 2,
    "userId": 2,
    "username": "maria",
    "movieId": 1,
    "movieTitle": "El Padrino",
    "rating": 4,
    "comment": "Muy buena, aunque un poco larga.",
    "createdAt": "2025-10-10T16:00:00",
    "updatedAt": "2025-10-10T16:00:00"
  }
]
```

### 4. **Obtener película con ratings**

```bash
GET /movies/1
```

**Respuesta:**
```json
{
  "id": 1,
  "title": "El Padrino",
  "duration": 175,
  "genre": "DRAMA",
  "releaseDate": "1972-03-24",
  "rating": 9.2,
  "available": true,
  "averageUserRating": 4.5,
  "reviewCount": 2
}
```

### 5. **Actualizar una review (solo el dueño)**

```bash
PUT /reviews/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

{
  "rating": 4,
  "comment": "Después de verla de nuevo, le bajo un punto."
}
```

### 6. **Eliminar una review (solo el dueño)**

```bash
DELETE /reviews/1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## 🔒 Seguridad Implementada

### Roles y Permisos

1. **Público (sin autenticación)**
   - Ver catálogo de películas
   - Ver detalles de películas (incluyendo ratings)
   - Ver todas las reviews
   - Ver ratings promedios

2. **USER (autenticado)**
   - Todo lo anterior +
   - Crear reviews
   - Actualizar **sus propias** reviews
   - Eliminar **sus propias** reviews
   - Pedir sugerencias con IA

3. **ADMIN (rol especial)**
   - Todo lo anterior +
   - Crear películas
   - Actualizar películas
   - Eliminar películas

### Validaciones de Seguridad

- ✅ Un usuario solo puede modificar/eliminar sus propias reviews
- ✅ Un usuario no puede hacer dos reviews a la misma película
- ✅ Los endpoints de CRUD de películas están protegidos por rol ADMIN
- ✅ Todas las operaciones de escritura requieren JWT válido

## 🎯 Flujo Completo de Usuario

```
1. Usuario se registra → Recibe JWT token + role: USER
2. Usuario hace login → Recibe JWT token actualizado
3. Usuario ve el catálogo de películas (público)
4. Usuario ve los detalles de una película (incluyendo rating promedio y cantidad de reviews)
5. Usuario ve las reviews de otros usuarios
6. Usuario autenticado crea su propia review (1-5 estrellas + comentario)
7. El rating promedio de la película se actualiza automáticamente
8. Usuario puede editar o eliminar solo su propia review
9. Usuario puede ver todas sus reviews en /reviews/user/{userId}
```

## 📋 Archivos Creados/Modificados

### Nuevos Archivos
- `src/main/java/com/platzi/play/persistence/entity/ReviewEntity.java`
- `src/main/java/com/platzi/play/persistence/crud/CrudReviewEntity.java`
- `src/main/java/com/platzi/play/domain/dto/ReviewDto.java`
- `src/main/java/com/platzi/play/domain/dto/CreateReviewDto.java`
- `src/main/java/com/platzi/play/domain/dto/UpdateReviewDto.java`
- `src/main/java/com/platzi/play/domain/exception/ReviewNotFoundException.java`
- `src/main/java/com/platzi/play/domain/exception/ReviewAlreadyExistsException.java`
- `src/main/java/com/platzi/play/domain/exception/UnauthorizedReviewAccessException.java`
- `src/main/java/com/platzi/play/persistence/mapper/ReviewMapper.java`
- `src/main/java/com/platzi/play/domain/repository/ReviewRepository.java`
- `src/main/java/com/platzi/play/persistence/ReviewEntityRepository.java`
- `src/main/java/com/platzi/play/domain/service/ReviewService.java`
- `src/main/java/com/platzi/play/web/controller/ReviewController.java`
- `src/main/java/com/platzi/play/web/config/JwtAuthenticationFilter.java`
- `src/main/resources/review-schema.sql`

### Archivos Modificados
- `src/main/java/com/platzi/play/domain/dto/MovieDto.java` - Agregados campos averageUserRating y reviewCount
- `src/main/java/com/platzi/play/persistence/MovieEntityRepository.java` - Enriquecimiento con datos de reviews
- `src/main/java/com/platzi/play/web/config/SecurityConfig.java` - Configuración de permisos por rol
- `src/main/java/com/platzi/play/web/config/OpenApiConfig.java` - Configuración JWT en Swagger
- `src/main/java/com/platzi/play/web/exception/RestExceptionHandler.java` - Manejo de excepciones de reviews

## 🧪 Testing Sugerido

### Casos de Prueba

1. ✅ Usuario puede crear una review para una película
2. ✅ Usuario no puede crear dos reviews para la misma película (409 Conflict)
3. ✅ Usuario puede actualizar su propia review
4. ✅ Usuario NO puede actualizar review de otro usuario (403 Forbidden)
5. ✅ Usuario puede eliminar su propia review
6. ✅ Usuario NO puede eliminar review de otro usuario (403 Forbidden)
7. ✅ Usuario sin autenticar NO puede crear reviews (401 Unauthorized)
8. ✅ Usuario con rol USER NO puede crear películas (403 Forbidden)
9. ✅ Usuario con rol ADMIN puede crear películas
10. ✅ El rating promedio se calcula correctamente al agregar/eliminar reviews

## 🚀 Próximos Pasos

Para completar el sistema, podrías implementar:

1. **Paginación** en las listas de reviews
2. **Ordenamiento** (más recientes, mejor calificadas, etc.)
3. **Búsqueda y filtrado** de reviews
4. **Reacciones** a reviews (útil/no útil)
5. **Reportes** de reviews inapropiadas
6. **Notificaciones** cuando alguien comenta en una película favorita
7. **Estadísticas** de reviews por usuario
8. **Trending movies** basadas en reviews recientes

## 📚 Documentación API

Accede a la documentación completa en Swagger UI:
```
http://localhost:8090/swagger-ui.html
```

Ahí podrás:
- Ver todos los endpoints
- Probar los endpoints directamente
- Ver ejemplos de request/response
- Configurar el token JWT para endpoints protegidos

---

## ✨ Resumen

Has implementado un **sistema completo y robusto de calificaciones y reviews** que incluye:
- ✅ Autenticación JWT
- ✅ Autorización por roles (USER/ADMIN)
- ✅ CRUD completo de reviews
- ✅ Validaciones de negocio y seguridad
- ✅ Cálculo automático de ratings
- ✅ Integración con el sistema de películas
- ✅ API REST bien documentada
- ✅ Manejo de errores robusto

**El sistema está listo para usar** una vez que compiles el proyecto y ejecutes los scripts de base de datos. 🎉

