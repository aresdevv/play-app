# 🎬 PlayApp - Sistema de Gestión de Películas con IA

PlayApp es una aplicación Spring Boot que permite gestionar películas y obtener recomendaciones personalizadas utilizando inteligencia artificial (Google Gemini).

## 🚀 Características Actuales

- ✅ **CRUD completo de películas** (Crear, Leer, Actualizar, Eliminar)
- ✅ **Sistema híbrido** con TMDB + Base de datos local
- ✅ **Importación automática** de películas desde TMDB
- ✅ **Sistema de usuarios** con registro y autenticación
- ✅ **Sistema de reviews** con calificaciones (1-5 estrellas)
- ✅ **Autenticación JWT** para seguridad de la API
- ✅ **Recomendaciones con IA** usando Google Gemini
- ✅ **Integración con TMDB API** para información de películas
- ✅ **API REST** con documentación OpenAPI/Swagger
- ✅ **Base de datos PostgreSQL** con inicialización automática
- ✅ **Despliegue en Render** con configuración de producción
- ✅ **Docker Compose** para desarrollo local
- ✅ **Validación de datos** con Bean Validation
- ✅ **Spring Security** para autorización y autenticación

## 🛠️ Tecnologías Utilizadas

- **Backend**: Spring Boot 3.5.5, Java 17
- **Base de datos**: PostgreSQL
- **Seguridad**: Spring Security, JWT
- **IA**: Google Gemini (LangChain4j)
- **APIs Externas**: TMDB (The Movie Database)
- **Documentación**: OpenAPI 3 / Swagger UI
- **Mapeo**: MapStruct
- **Validación**: Bean Validation
- **Arquitectura**: Sistema Híbrido (API + BD Local)
- **Despliegue**: Docker, Render
- **Build**: Gradle

## 📋 Requisitos

- Java 17+
- PostgreSQL
- Docker (opcional, para desarrollo)
- Cuenta de Google Cloud (para Gemini API)

## 🚀 Instalación y Configuración

### Desarrollo Local

1. **Clonar el repositorio**
```bash
git clone https://github.com/aresdevv/play-app.git
cd play-app
```

2. **Configurar base de datos**
```bash
# Con Docker Compose
docker-compose up -d

# O configurar PostgreSQL manualmente
```

3. **Configurar variables de entorno**
```bash
# En application-dev.properties

# API Key de Google Gemini
langchain4j.google-ai-gemini.chat-model.api-key=TU_API_KEY_AQUI

# API Key de TMDB
tmdb.api.key=TU_TMDB_API_KEY_AQUI
```

4. **Ejecutar la aplicación**
```bash
./gradlew bootRun
```

5. **Ejecutar migración de BD (solo si ya tienes BD existente)**
```sql
-- Desde psql o tu cliente PostgreSQL
\i src/main/resources/migration-tmdb-fields.sql
```

### Producción (Render)

La aplicación está configurada para desplegarse automáticamente en Render cuando se hace push al repositorio.

## 📚 API Endpoints

### Películas (Base de Datos Local)
- `GET /play-app/api/movies` - Obtener todas las películas guardadas
- `GET /play-app/api/movies/{id}` - Obtener película por ID
- `POST /play-app/api/movies` - Crear nueva película manualmente
- `PUT /play-app/api/movies/{id}` - Actualizar película
- `DELETE /play-app/api/movies/{id}` - Eliminar película
- `POST /play-app/api/movies/suggest` - Generar recomendaciones con IA
- `POST /play-app/api/movies/import-from-tmdb/{tmdbId}` - **NUEVO:** Importar película desde TMDB

### TMDB (The Movie Database) 🎬
- `GET /play-app/api/tmdb/search?query={titulo}&page={page}` - Buscar películas por título
- `GET /play-app/api/tmdb/movie/{tmdbId}` - Obtener detalles completos de una película
- `GET /play-app/api/tmdb/popular?page={page}` - Obtener películas populares
- `GET /play-app/api/tmdb/top-rated?page={page}` - Obtener películas mejor calificadas
- `GET /play-app/api/tmdb/now-playing?page={page}` - Obtener películas en cines
- `GET /play-app/api/tmdb/upcoming?page={page}` - Obtener próximos estrenos

### Autenticación
- `POST /play-app/api/auth/register` - Registrar nuevo usuario
- `POST /play-app/api/auth/login` - Iniciar sesión
- `GET /play-app/api/auth/me` - Obtener información del usuario actual


### Usuarios (Gestión de Perfil) 👤
- `GET /play-app/api/users/me` - **NUEVO:** Obtener perfil del usuario actual
- `PUT /play-app/api/users/me` - **NUEVO:** Actualizar perfil (nombre completo, email)
- `GET /play-app/api/users/me/reviews` - **NUEVO:** Obtener todas las reviews del usuario actual

### Reviews (Calificaciones y Reseñas) ⭐
- `POST /play-app/api/reviews` - Crear nueva review (requiere autenticación)
- `PUT /play-app/api/reviews/{id}` - Actualizar review propia
- `DELETE /play-app/api/reviews/{id}` - Eliminar review propia
- `GET /play-app/api/reviews/{id}` - Obtener review por ID
- `GET /play-app/api/reviews/movie/{movieId}` - Obtener todas las reviews de una película
- `GET /play-app/api/reviews/user/{userId}` - Obtener todas las reviews de un usuario
- `GET /play-app/api/reviews/user/{userId}/movie/{movieId}` - Obtener review específica de usuario para película
- `GET /play-app/api/reviews/movie/{movieId}/average` - Obtener calificación promedio de película
- `GET /play-app/api/reviews/movie/{movieId}/count` - Obtener cantidad de reviews de película
### Utilidades
- `GET /play-app/api/hello` - Endpoint de prueba

### Documentación
- `GET /play-app/api/swagger-ui.html` - Interfaz Swagger UI

## 🔄 Sistema Híbrido TMDB + BD Local

PlayApp implementa un **sistema híbrido** que combina:
- **TMDB**: Exploración de millones de películas
- **BD Local**: Almacenamiento selectivo de películas para reviews

### Flujo de Uso

```bash
# 1. Usuario busca película en TMDB
GET /play-app/api/tmdb/search?query=matrix

# 2. Usuario quiere hacer review → Importa película automáticamente
POST /play-app/api/movies/import-from-tmdb/603

# 3. Sistema guarda película en BD local (si no existe)
# 4. Usuario puede hacer review con FK válida
POST /play-app/api/reviews
{
  "movieId": 123,  // ID de BD local (no TMDB ID)
  "rating": 5,
  "comment": "¡Excelente!"
}
```

### Ventajas

✅ Acceso a millones de películas (TMDB)  
✅ Solo guardas lo que necesitas (BD local)  
✅ Reviews con integridad referencial  
✅ Más rápido (BD local > API externa)  
✅ Funciona offline con películas importadas  

📖 **Ver guía completa**: [SISTEMA_HIBRIDO.md](SISTEMA_HIBRIDO.md)

## 🎯 Próximas Mejoras

### 🎬 Funcionalidades Core de Películas

#### Gestión Avanzada de Películas
- ✅ **Sistema de calificaciones y reviews** (1-5 estrellas + comentarios)
- [ ] **Favoritos y listas personalizadas** (Watchlist, "Vistas", "Por ver")
- [ ] **Búsqueda avanzada** (por género, año, director, actor, calificación)
- [ ] **Filtros y ordenamiento** (más populares, mejor calificadas, más recientes)
- [ ] **Sistema de tags/categorías** (Netflix, Disney+, HBO, etc.)
- [ ] **Trailers y enlaces** (YouTube, IMDB, etc.)

#### Sistema de Usuarios
- [ ] **Autenticación y autorización** (JWT, OAuth2)
- [ ] **Perfiles de usuario** (preferencias, historial)
- [ ] **Sistema de amigos** (compartir listas, ver qué ven tus amigos)
- [ ] **Notificaciones** (nuevas películas, recomendaciones)

### 🤖 IA y Recomendaciones Avanzadas

#### Motor de Recomendaciones
- [ ] **Recomendaciones basadas en historial** (qué has visto)
- [ ] **Recomendaciones colaborativas** (usuarios similares)
- [ ] **Recomendaciones por estado de ánimo** ("Quiero algo divertido", "Algo para llorar")
- [ ] **Recomendaciones por ocasión** ("Para ver en pareja", "Para niños")
- [ ] **Análisis de sentimientos** en reviews para mejorar recomendaciones

#### IA Conversacional
- [ ] **Chatbot de películas** ("¿Qué película me recomiendas para el fin de semana?")
- [ ] **Búsqueda por descripción** ("Una película de robots que se enamoran")
- [ ] **Comparación de películas** ("¿Cuál es mejor: Inception o Interstellar?")

### 📊 Analytics y Reportes

#### Dashboard de Administración
- [ ] **Métricas de uso** (películas más populares, usuarios activos)
- [ ] **Reportes de recomendaciones** (qué tan efectivas son)
- [ ] **Análisis de tendencias** (géneros populares por temporada)
- [ ] **Estadísticas de usuarios** (tiempo promedio de visualización)

#### Reportes para Usuarios
- [ ] **Estadísticas personales** (cuántas películas has visto, género favorito)
- [ ] **Resumen anual** (tus películas del año)
- [ ] **Comparación con amigos** (quién ha visto más películas)

### 🌐 Integraciones Externas

#### APIs de Películas
- ✅ **TMDB (The Movie Database)** - Información completa de películas
- [ ] **OMDb API** - Metadatos adicionales
- [ ] **JustWatch API** - Dónde ver cada película
- [ ] **Rotten Tomatoes** - Críticas y calificaciones

#### Streaming Services
- [ ] **Integración con Netflix, Disney+, HBO** (qué está disponible)
- [ ] **Alertas de disponibilidad** (cuando una película llega a tu plataforma)
- [ ] **Precios de alquiler/compra** (Amazon Prime, Google Play, etc.)

### 📱 Experiencia de Usuario

#### Frontend Moderno
- [ ] **SPA con React/Vue/Angular** (interfaz moderna)
- [ ] **PWA (Progressive Web App)** (funciona offline)
- [ ] **App móvil** (React Native/Flutter)
- [ ] **Modo oscuro/claro**

#### Funcionalidades Sociales
- [ ] **Sistema de reviews y comentarios**
- [ ] **Foros de discusión** por película
- [ ] **Eventos virtuales** (maratones de películas)
- [ ] **Sistema de desafíos** ("Ve 10 películas de terror este mes")

### 🔧 Mejoras Técnicas

#### Performance y Escalabilidad
- [ ] **Cache con Redis** (recomendaciones, búsquedas)
- [ ] **CDN para imágenes** (posters, trailers)
- [ ] **Microservicios** (separar recomendaciones, usuarios, películas)
- [ ] **Message queues** (procesamiento asíncrono de IA)

#### Monitoreo y Observabilidad
- [ ] **Logs estructurados** (ELK Stack)
- [ ] **Métricas con Prometheus/Grafana**
- [ ] **Tracing distribuido** (Jaeger)
- [ ] **Health checks** y alertas

## 🎯 Roadmap de Implementación

### Fase 1 (Completada ✅)
1. ✅ **Sistema de usuarios** con registro y autenticación JWT
2. ✅ **Autenticación de usuarios** con Spring Security
3. ✅ **Sistema de calificaciones y reviews** (1-5 estrellas + comentarios)
4. ✅ **Integración con TMDB** (búsqueda, importación)
5. ✅ **Sistema híbrido** (TMDB + BD Local)

### Fase 2 (Próximas 2-4 semanas)
1. **Sistema de favoritos** y listas personalizadas
2. **Recomendaciones mejoradas** basadas en usuario
3. **Frontend básico** (HTML/CSS/JS o React simple)
4. ✅ **Integración con TMDB** (Completado)

### Fase 3 (2-3 meses)
1. **Chatbot de IA**
2. **Sistema social** (amigos, compartir)
3. **Analytics dashboard**
4. **App móvil**

## 💡 Ideas Creativas Únicas

- [ ] **"Película del día"** con trivia y curiosidades
- [ ] **Sistema de "desafíos cinematográficos"** (maratones temáticos)
- [ ] **"Match de películas"** (como Tinder pero para películas)
- [ ] **Integración con calendario** (planificar qué ver cada día)
- [ ] **Sistema de "spoiler alerts"** inteligente
- [ ] **Recomendaciones por clima** ("Llueve, perfecto para una película de terror")

## 🏗️ Arquitectura del Proyecto

```
src/main/java/com/platzi/play/
├── domain/                 # Lógica de negocio
│   ├── dto/               # Data Transfer Objects
│   ├── exception/         # Excepciones personalizadas
│   ├── repository/        # Interfaces de repositorio
│   └── service/           # Servicios de negocio
├── persistence/           # Capa de persistencia
│   ├── crud/             # Repositorios CRUD
│   ├── entity/           # Entidades JPA
│   └── mapper/           # Mappers con MapStruct
├── web/                  # Capa web
│   ├── config/           # Configuraciones
│   ├── controller/       # Controladores REST
│   └── exception/        # Manejo de excepciones
└── PlatziPlayApplication.java
```

## 🐳 Docker

### Desarrollo
```bash
docker-compose up -d
```

### Producción
```bash
docker build -t play-app .
docker run -p 8080:8080 play-app
```

## 📝 Contribuir

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📚 Documentación Adicional

- 📖 [SISTEMA_HIBRIDO.md](SISTEMA_HIBRIDO.md) - Guía completa del sistema híbrido TMDB + BD Local
- 🚀 [QUICK_START.md](QUICK_START.md) - Guía rápida de inicio en 5 minutos
- 🔄 [COMO_FUNCIONA_IMPORTACION.md](COMO_FUNCIONA_IMPORTACION.md) - Cómo funciona el sistema de importación sin duplicados
- 🎬 [TMDB_GUIA.md](TMDB_GUIA.md) - Guía de integración con TMDB API
- 🐧 [GUIA_ARCH_LINUX.md](GUIA_ARCH_LINUX.md) - Guía específica para Arch Linux
- 📋 [CHANGELOG_SISTEMA_HIBRIDO.md](CHANGELOG_SISTEMA_HIBRIDO.md) - Registro de cambios del sistema híbrido
- 📝 [GUIA_POSTMAN.md](GUIA_POSTMAN.md) - Guía de uso con Postman
- 🔐 [JWT_EXPLICACION_DETALLADA.md](JWT_EXPLICACION_DETALLADA.md) - Explicación del sistema JWT
- 📊 [SISTEMA_REVIEWS_IMPLEMENTACION.md](SISTEMA_REVIEWS_IMPLEMENTACION.md) - Sistema de reviews implementado

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.

## 👨‍💻 Autor

**Ares** - [@aresdevv](https://github.com/aresdevv)

## 🙏 Agradecimientos

- Spring Boot Team
- Google Gemini AI
- Render por el hosting
- La comunidad de desarrolladores

---

⭐ **¡Si te gusta este proyecto, dale una estrella!** ⭐
