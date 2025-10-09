# 🎬 PlayApp - Sistema de Gestión de Películas con IA

PlayApp es una aplicación Spring Boot que permite gestionar películas y obtener recomendaciones personalizadas utilizando inteligencia artificial (Google Gemini).

## 🚀 Características Actuales

- ✅ **CRUD completo de películas** (Crear, Leer, Actualizar, Eliminar)
- ✅ **Sistema de usuarios** con registro y autenticación
- ✅ **Autenticación JWT** para seguridad de la API
- ✅ **Recomendaciones con IA** usando Google Gemini
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
- **Documentación**: OpenAPI 3 / Swagger UI
- **Mapeo**: MapStruct
- **Validación**: Bean Validation
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
langchain4j.google-ai-gemini.chat-model.api-key=TU_API_KEY_AQUI
```

4. **Ejecutar la aplicación**
```bash
./gradlew bootRun
```

### Producción (Render)

La aplicación está configurada para desplegarse automáticamente en Render cuando se hace push al repositorio.

## 📚 API Endpoints

### Películas
- `GET /play-app/api/movies` - Obtener todas las películas
- `GET /play-app/api/movies/{id}` - Obtener película por ID
- `POST /play-app/api/movies` - Crear nueva película
- `PUT /play-app/api/movies/{id}` - Actualizar película
- `DELETE /play-app/api/movies/{id}` - Eliminar película
- `POST /play-app/api/movies/suggest` - Generar recomendaciones con IA

### Autenticación
- `POST /play-app/api/auth/register` - Registrar nuevo usuario
- `POST /play-app/api/auth/login` - Iniciar sesión
- `GET /play-app/api/auth/me` - Obtener información del usuario actual

### Utilidades
- `GET /play-app/api/hello` - Endpoint de prueba

### Documentación
- `GET /play-app/api/swagger-ui.html` - Interfaz Swagger UI

## 🎯 Próximas Mejoras

### 🎬 Funcionalidades Core de Películas

#### Gestión Avanzada de Películas
- [ ] **Sistema de calificaciones y reviews** (1-5 estrellas + comentarios)
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
- [ ] **TMDB (The Movie Database)** - Información completa de películas
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
3. [ ] **Sistema de calificaciones** (1-5 estrellas)
4. [ ] **Búsqueda básica** (por título, género)
5. [ ] **Filtros simples** (género, año)

### Fase 2 (Próximas 2-4 semanas)
1. **Sistema de favoritos** y listas personalizadas
2. **Recomendaciones mejoradas** basadas en usuario
3. **Frontend básico** (HTML/CSS/JS o React simple)
4. **Integración con TMDB**

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
