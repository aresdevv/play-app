# 🧪 Guía Completa: Probar PlayApp en Postman

## 📋 Índice
1. [Configuración inicial](#configuración-inicial)
2. [Registrar un usuario](#1-registrar-un-usuario)
3. [Login (obtener token)](#2-login-obtener-token)
4. [Configurar token en Postman](#3-configurar-el-token-en-postman)
5. [Ver películas (público)](#4-ver-películas-público)
6. [Crear una review (autenticado)](#5-crear-una-review-autenticado)
7. [Ver reviews de una película](#6-ver-reviews-de-una-película)
8. [Editar tu review](#7-editar-tu-propia-review)
9. [Intentar editar review de otro (403 Forbidden)](#8-intentar-editar-review-de-otro-usuario)
10. [Crear película (solo ADMIN)](#9-crear-película-solo-admin)

---

## 🚀 Configuración Inicial

### Prerequisitos:
1. Tener la aplicación corriendo: `./gradlew bootRun`
2. Base de datos PostgreSQL corriendo
3. Postman instalado

### URL Base:
```
http://localhost:8090
```

---

## 1️⃣ Registrar un Usuario

### Request:
```
POST http://localhost:8090/auth/register
```

### Headers:
```
Content-Type: application/json
```

### Body (JSON):
```json
{
  "username": "juan",
  "email": "juan@example.com",
  "password": "password123",
  "confirmPassword": "password123",
  "nombreCompleto": "Juan Pérez"
}
```

### Response Esperada (201 Created):
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqdWFuIiwiaWF0IjoxNzI4NTg0NDAwLCJleHAiOjE3Mjg1ODgwMDB9.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
  "type": "Bearer",
  "expiresIn": 3600,
  "userInfo": {
    "id": 1,
    "username": "juan",
    "email": "juan@example.com",
    "nombreCompleto": "Juan Pérez",
    "role": "USER",
    "ultimoAcceso": "2025-10-10T14:00:00"
  }
}
```

### ✅ Qué hacer con la respuesta:
**¡IMPORTANTE!** Copia el valor de `token`, lo necesitarás para los siguientes requests.

---

## 2️⃣ Login (Obtener Token)

Si ya te registraste antes, puedes hacer login directamente.

### Request:
```
POST http://localhost:8090/auth/login
```

### Headers:
```
Content-Type: application/json
```

### Body (JSON):
```json
{
  "usernameOrEmail": "juan",
  "password": "password123"
}
```

**O con email:**
```json
{
  "usernameOrEmail": "juan@example.com",
  "password": "password123"
}
```

### Response Esperada (200 OK):
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiresIn": 3600,
  "userInfo": {
    "id": 1,
    "username": "juan",
    "email": "juan@example.com",
    "role": "USER"
  }
}
```

---

## 3️⃣ Configurar el Token en Postman

### Opción 1: Usar Variables de Entorno (RECOMENDADO) ⭐

1. **Crear Environment:**
   - Click en el ícono de "Environments" (esquina superior derecha)
   - Click en "+" para crear nuevo environment
   - Nombre: `PlayApp - Dev`

2. **Agregar variables:**
   ```
   Variable:    base_url
   Initial:     http://localhost:8090
   Current:     http://localhost:8090
   
   Variable:    token
   Initial:     (vacío)
   Current:     (pegar tu token aquí)
   ```

3. **Activar el environment:**
   - Selecciona "PlayApp - Dev" en el dropdown de environments

4. **Usar en requests:**
   - URL: `{{base_url}}/reviews`
   - Authorization → Type: Bearer Token → Token: `{{token}}`

### Opción 2: Manual (para cada request)

En cada request que requiera autenticación:

1. Ve a la pestaña **Authorization**
2. Type: `Bearer Token`
3. Token: pega tu token completo (sin "Bearer")
   ```
   eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   ```

---

## 4️⃣ Ver Películas (Público)

Este endpoint NO requiere autenticación.

### Request:
```
GET http://localhost:8090/movies
```

### Headers:
```
(ninguno necesario)
```

### Response Esperada (200 OK):
```json
[
  {
    "id": 1,
    "title": "Inception",
    "duration": 148,
    "genre": "CIENCIA_FICCION",
    "releaseDate": "2010-07-16",
    "rating": null,
    "available": true,
    "averageUserRating": 4.5,  // ← Promedio de reviews
    "reviewCount": 15           // ← Cantidad de reviews
  },
  {
    "id": 2,
    "title": "Titanic",
    "duration": 195,
    "genre": "DRAMA",
    "releaseDate": "1997-12-19",
    "rating": 4.6,
    "available": true,
    "averageUserRating": 4.8,
    "reviewCount": 23
  }
]
```

### Ver una película específica:
```
GET http://localhost:8090/movies/1
```

---

## 5️⃣ Crear una Review (Autenticado)

**⚠️ IMPORTANTE:** Este endpoint REQUIERE token de autenticación.

### Request:
```
POST http://localhost:8090/reviews
```

### Headers:
```
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Body (JSON):
```json
{
  "movieId": 1,
  "rating": 5,
  "comment": "¡Excelente película! Me encantó la trama y las actuaciones. Totalmente recomendada."
}
```

### Response Esperada (201 Created):
```json
{
  "id": 1,
  "userId": 1,
  "username": "juan",
  "movieId": 1,
  "movieTitle": "Inception",
  "rating": 5,
  "comment": "¡Excelente película! Me encantó la trama y las actuaciones.",
  "createdAt": "2025-10-10T14:30:00",
  "updatedAt": "2025-10-10T14:30:00"
}
```

### ❌ Si no envías el token:
```json
{
  "code": "unauthorized",
  "message": "Full authentication is required to access this resource"
}
```
Status: 401 Unauthorized

### ❌ Si intentas crear otra review para la misma película:
```json
{
  "code": "review-already-exists",
  "message": "El usuario 1 ya hizo una review de la película 1"
}
```
Status: 409 Conflict

---

## 6️⃣ Ver Reviews de una Película

Este endpoint NO requiere autenticación (público).

### Request:
```
GET http://localhost:8090/reviews/movie/1
```

### Response Esperada (200 OK):
```json
[
  {
    "id": 1,
    "userId": 1,
    "username": "juan",
    "movieId": 1,
    "movieTitle": "Inception",
    "rating": 5,
    "comment": "¡Excelente película!",
    "createdAt": "2025-10-10T14:30:00",
    "updatedAt": "2025-10-10T14:30:00"
  },
  {
    "id": 2,
    "userId": 2,
    "username": "maria",
    "movieId": 1,
    "movieTitle": "Inception",
    "rating": 4,
    "comment": "Muy buena, aunque un poco compleja.",
    "createdAt": "2025-10-10T15:00:00",
    "updatedAt": "2025-10-10T15:00:00"
  }
]
```

### Otros endpoints útiles:

**Ver reviews de un usuario:**
```
GET http://localhost:8090/reviews/user/1
```

**Ver rating promedio de una película:**
```
GET http://localhost:8090/reviews/movie/1/average
```
Response: `4.5`

**Ver cantidad de reviews:**
```
GET http://localhost:8090/reviews/movie/1/count
```
Response: `15`

---

## 7️⃣ Editar tu Propia Review

**⚠️ Solo puedes editar TUS propias reviews.**

### Request:
```
PUT http://localhost:8090/reviews/1
```

### Headers:
```
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Body (JSON):
```json
{
  "rating": 4,
  "comment": "Después de verla de nuevo, le bajo un punto. Sigue siendo muy buena."
}
```

**Nota:** Puedes enviar solo el campo que quieres actualizar:
```json
{
  "rating": 4
}
```

### Response Esperada (200 OK):
```json
{
  "id": 1,
  "userId": 1,
  "username": "juan",
  "movieId": 1,
  "movieTitle": "Inception",
  "rating": 4,
  "comment": "Después de verla de nuevo, le bajo un punto. Sigue siendo muy buena.",
  "createdAt": "2025-10-10T14:30:00",
  "updatedAt": "2025-10-10T16:00:00"  // ← Fecha actualizada
}
```

---

## 8️⃣ Intentar Editar Review de Otro Usuario

Esto va a FALLAR (propósito educativo para ver seguridad).

### Escenario:
- Estás logueado como "juan" (user_id=1)
- Intentas editar review_id=2 que pertenece a "maria" (user_id=2)

### Request:
```
PUT http://localhost:8090/reviews/2
```

### Headers:
```
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...  (token de juan)
```

### Body (JSON):
```json
{
  "rating": 1,
  "comment": "Intentando hackear la review de maria"
}
```

### Response Esperada (403 Forbidden):
```json
{
  "code": "unauthorized-review-access",
  "message": "No tienes permisos para modificar esta review"
}
```

**✅ Seguridad funcionando correctamente!**

---

## 9️⃣ Crear Película (Solo ADMIN)

### Caso 1: Usuario normal intenta crear película (FALLARÁ)

**Estás logueado como "juan" (role=USER)**

### Request:
```
POST http://localhost:8090/movies
```

### Headers:
```
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...  (token de juan)
```

### Body (JSON):
```json
{
  "title": "Matrix",
  "duration": 136,
  "genre": "CIENCIA_FICCION",
  "releaseDate": "1999-03-31",
  "rating": 8.7,
  "available": true
}
```

### Response Esperada (403 Forbidden):
```json
{
  "code": "access-denied",
  "message": "Access Denied"
}
```

**✅ Seguridad funcionando! Solo ADMIN puede crear películas.**

---

### Caso 2: Usuario ADMIN crea película (ÉXITO)

Para probar esto, necesitas un usuario con rol ADMIN.

**Opción 1: Crear usuario ADMIN directamente en BD:**
```sql
UPDATE usuarios 
SET role = 'ADMIN' 
WHERE username = 'juan';
```

**Opción 2: Modificar UserEntity.java para crear ADMIN en registro:**
```java
// Temporalmente cambiar en AuthService.java línea 72
newUser.setRole(UserEntity.UserRole.ADMIN);  // En vez de USER
```

Luego haz login de nuevo para obtener un nuevo token con role ADMIN:
```
POST http://localhost:8090/auth/login
Body: { "usernameOrEmail": "juan", "password": "password123" }
```

Ahora usa ese token y repite el request de crear película. Debería funcionar.

---

## 🔟 Eliminar una Review

### Request:
```
DELETE http://localhost:8090/reviews/1
```

### Headers:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Response Esperada (204 No Content):
```
(sin body, solo status 204)
```

---

## 🎯 Colección Completa de Postman

### Crear colección organizada:

```
PlayApp API
├── 📁 Auth
│   ├── Register
│   └── Login
├── 📁 Movies (Public)
│   ├── Get All Movies
│   └── Get Movie by ID
├── 📁 Reviews
│   ├── Create Review (Auth)
│   ├── Get Reviews by Movie
│   ├── Get Reviews by User
│   ├── Get My Review for Movie
│   ├── Update My Review (Auth)
│   ├── Delete My Review (Auth)
│   ├── Get Average Rating
│   └── Get Review Count
└── 📁 Movies (Admin)
    ├── Create Movie (Admin)
    ├── Update Movie (Admin)
    └── Delete Movie (Admin)
```

---

## 🧪 Test de Flujo Completo

### Escenario: Usuario completo

```bash
# 1. Registrarse
POST /auth/register → Guarda el token

# 2. Ver catálogo de películas
GET /movies → Ve películas con ratings

# 3. Crear una review
POST /reviews → Califica película 5 estrellas

# 4. Ver la review en la película
GET /reviews/movie/1 → Ve tu review

# 5. Ver película actualizada
GET /movies/1 → Ve el nuevo promedio de rating

# 6. Editar tu review
PUT /reviews/1 → Cambias a 4 estrellas

# 7. Ver película de nuevo
GET /movies/1 → Rating promedio actualizado

# 8. Intentar crear película (fallará)
POST /movies → 403 Forbidden (no eres ADMIN)

# 9. Ver tus reviews
GET /reviews/user/1 → Todas tus reviews
```

---

## 🔍 Debugging en Postman

### Ver el token decodificado:

1. Copia tu token
2. Ve a https://jwt.io/
3. Pega el token en "Encoded"
4. Verás:
   ```json
   {
     "sub": "juan",
     "iat": 1728584400,
     "exp": 1728588000
   }
   ```

### Verificar headers en Postman:

En la pestaña **Headers** del request, verifica que tengas:
```
Content-Type: application/json
Authorization: Bearer eyJhbGci...
```

---

## ⚠️ Errores Comunes y Soluciones

### Error 401 Unauthorized
**Causa:** Token no enviado o inválido
**Solución:** 
- Verifica que el header Authorization esté presente
- Asegúrate de poner "Bearer " antes del token
- Verifica que el token no haya expirado (1 hora)

### Error 403 Forbidden
**Causa:** No tienes permisos para esta acción
**Solución:**
- Verifica tu rol (USER vs ADMIN)
- Verifica que seas el dueño de la review

### Error 409 Conflict
**Causa:** Ya existe una review tuya para esa película
**Solución:** 
- Usa PUT para actualizar en vez de crear otra
- O elimina la review anterior primero

### Error 404 Not Found
**Causa:** El recurso no existe
**Solución:**
- Verifica que el ID de la película/review exista
- Usa GET /movies para ver IDs disponibles

---

## 📊 Verificar en Swagger UI

También puedes probar en la interfaz web:

```
http://localhost:8090/swagger-ui.html
```

1. Click en "Authorize" (esquina superior derecha)
2. Ingresa tu token: `eyJhbGci...` (sin "Bearer")
3. Click "Authorize"
4. Ahora puedes probar todos los endpoints desde la interfaz

---

## ✅ Checklist de Testing

- [ ] Registrar usuario
- [ ] Login con username
- [ ] Login con email
- [ ] Ver todas las películas
- [ ] Ver película específica
- [ ] Crear review para película
- [ ] Ver reviews de película
- [ ] Ver mis reviews
- [ ] Editar mi review
- [ ] Intentar editar review de otro (debe fallar)
- [ ] Eliminar mi review
- [ ] Intentar crear película sin ser admin (debe fallar)
- [ ] Ver rating promedio actualizado
- [ ] Login después de 1 hora (token expirado)

---

## 🎓 Tips Profesionales

1. **Usa variables de entorno** para cambiar fácilmente entre dev/prod
2. **Guarda la colección** para reutilizar
3. **Usa Tests en Postman** para validar responses automáticamente:
   ```javascript
   pm.test("Status code is 200", function () {
       pm.response.to.have.status(200);
   });
   
   pm.test("Token is present", function () {
       var jsonData = pm.response.json();
       pm.expect(jsonData.token).to.exist;
   });
   ```
4. **Usa Pre-request Scripts** para auto-generar datos:
   ```javascript
   pm.environment.set("timestamp", new Date().getTime());
   ```

---

¡Listo! Con esta guía puedes probar TODO el sistema completo. 🚀

