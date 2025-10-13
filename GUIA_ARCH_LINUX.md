# 🐧 Guía para Arch Linux (Omarchy)

## 🎯 Migración de Base de Datos

Tienes **2 opciones** según tu situación:

---

## ✅ Opción A: Recrear BD (RECOMENDADO)

### ¿Cuándo usar?
- ✅ Estás en **desarrollo**
- ✅ No tienes datos importantes
- ✅ Quieres empezar limpio
- ✅ Es más **rápido y simple**

### Pasos

#### 1. Ejecutar script de recreación

```bash
./RECREAR_BD.sh
```

O manualmente:

```bash
# 1. Conectar a PostgreSQL
psql -h localhost -p 5433 -U aresdevv -d postgres

# 2. Eliminar BD antigua
DROP DATABASE IF EXISTS platzi_play_db;

# 3. Crear BD nueva
CREATE DATABASE platzi_play_db;

# 4. Salir
\q
```

#### 2. Ejecutar aplicación

```bash
./gradlew bootRun
```

**Hibernate creará automáticamente las tablas con los campos de TMDB** ✅

#### 3. Verificar

```bash
# Conectar a la BD
psql -h localhost -p 5433 -U aresdevv -d platzi_play_db

# Ver estructura de tabla
\d platzi_play_peliculas

# Deberías ver las columnas:
# - tmdb_id
# - poster_url
# - backdrop_url
# - overview
# - original_title
# - vote_average
# - vote_count
# - popularity
# - original_language
```

---

## ⚙️ Opción B: Migrar BD (Conserva datos)

### ¿Cuándo usar?
- ✅ Tienes **datos de prueba importantes**
- ✅ Quieres conservar películas existentes
- ✅ Tienes reviews que no quieres perder

### Pasos

#### 1. Ejecutar script de migración

```bash
./MIGRAR_BD.sh
```

O manualmente:

```bash
# Ejecutar migración
psql -h localhost -p 5433 -U aresdevv -d platzi_play_db -f src/main/resources/migration-tmdb-fields.sql
```

#### 2. Verificar migración

```bash
# Conectar
psql -h localhost -p 5433 -U aresdevv -d platzi_play_db

# Ver columnas nuevas
\d platzi_play_peliculas

# Ver datos existentes (deberían estar intactos)
SELECT id, titulo, tmdb_id FROM platzi_play_peliculas LIMIT 5;
```

#### 3. Ejecutar aplicación

```bash
./gradlew bootRun
```

---

## 🔧 Solución de Problemas (Arch Linux)

### Error: "psql: command not found"

```bash
# Instalar PostgreSQL
sudo pacman -S postgresql

# Iniciar servicio
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

### Error: "FATAL: role 'aresdevv' does not exist"

```bash
# Crear usuario
sudo -u postgres createuser -s aresdevv

# Establecer contraseña
sudo -u postgres psql -c "ALTER USER aresdevv WITH PASSWORD 'root';"
```

### Error: "FATAL: database 'platzi_play_db' does not exist"

```bash
# Crear base de datos
createdb -h localhost -p 5433 -U aresdevv platzi_play_db
```

### PostgreSQL no responde en puerto 5433

```bash
# Verificar puerto configurado
sudo netstat -tlnp | grep postgres

# Si está en 5432, actualiza application-dev.properties:
# spring.datasource.url=jdbc:postgresql://localhost:5432/platzi_play_db
```

### Docker Compose (Alternativa más fácil)

Si tienes problemas con PostgreSQL local, usa Docker:

```bash
# Iniciar PostgreSQL con Docker Compose
docker-compose up -d

# Verificar que está corriendo
docker-compose ps

# Ver logs
docker-compose logs postgres

# Conectar
docker-compose exec postgres psql -U aresdevv -d platzi_play_db
```

---

## 📊 Verificación Post-Migración

### Opción 1: Con psql

```bash
psql -h localhost -p 5433 -U aresdevv -d platzi_play_db

-- Ver estructura completa
\d+ platzi_play_peliculas

-- Verificar índice en tmdb_id
\di idx_tmdb_id

-- Ver columnas específicas
SELECT column_name, data_type, is_nullable 
FROM information_schema.columns 
WHERE table_name = 'platzi_play_peliculas' 
  AND column_name LIKE '%tmdb%';

-- Salir
\q
```

### Opción 2: Con la aplicación

```bash
# Iniciar aplicación
./gradlew bootRun

# En otro terminal, probar endpoint
curl http://localhost:8090/play-app/api/tmdb/search?query=matrix

# Importar película
curl -X POST http://localhost:8090/play-app/api/movies/import-from-tmdb/603

# Ver películas (debería mostrar campos de TMDB)
curl http://localhost:8090/play-app/api/movies
```

---

## 🎯 Mi Recomendación para Ti

Basado en que estás en **desarrollo**:

### 🏆 Opción A: Recrear BD

```bash
# 1. Recrear (30 segundos)
./RECREAR_BD.sh

# 2. Iniciar app (Hibernate crea todo)
./gradlew bootRun

# 3. Probar
curl -X POST http://localhost:8090/play-app/api/movies/import-from-tmdb/603
```

**Ventajas:**
- ✅ Más rápido (30 seg vs 5 min)
- ✅ Sin posibles errores de migración
- ✅ Base de datos limpia
- ✅ Hibernate crea todo automáticamente
- ✅ Perfecto para desarrollo

**Desventajas:**
- ❌ Pierdes datos existentes (pero en desarrollo no importa)

---

## 📝 Comandos Útiles en Arch Linux

### PostgreSQL

```bash
# Estado del servicio
sudo systemctl status postgresql

# Iniciar
sudo systemctl start postgresql

# Detener
sudo systemctl stop postgresql

# Reiniciar
sudo systemctl restart postgresql

# Logs
sudo journalctl -u postgresql -f
```

### Backup (opcional, antes de recrear)

```bash
# Hacer backup
pg_dump -h localhost -p 5433 -U aresdevv platzi_play_db > backup_$(date +%Y%m%d).sql

# Restaurar (si necesitas)
psql -h localhost -p 5433 -U aresdevv platzi_play_db < backup_20251013.sql
```

### Limpiar todo y empezar de cero

```bash
# 1. Detener aplicación
pkill -f "play-app"

# 2. Eliminar BD
psql -h localhost -p 5433 -U aresdevv -d postgres -c "DROP DATABASE IF EXISTS platzi_play_db;"

# 3. Crear BD
psql -h localhost -p 5433 -U aresdevv -d postgres -c "CREATE DATABASE platzi_play_db;"

# 4. Ejecutar aplicación (Hibernate crea tablas)
./gradlew bootRun
```

---

## 🚀 Inicio Rápido (Todo en Uno)

Para empezar desde cero en Arch Linux:

```bash
# 1. Asegurar PostgreSQL corriendo
sudo systemctl start postgresql

# 2. Recrear BD
./RECREAR_BD.sh

# 3. Ejecutar aplicación
./gradlew bootRun

# 4. En otro terminal, probar
curl -X POST http://localhost:8090/play-app/api/movies/import-from-tmdb/603

# 5. Ver resultado
curl http://localhost:8090/play-app/api/movies
```

**Tiempo total: ~2 minutos** ⚡

---

## 📚 Recursos Adicionales

- 🐧 [ArchWiki - PostgreSQL](https://wiki.archlinux.org/title/PostgreSQL)
- 🔧 [Configuración de PostgreSQL en Arch](https://wiki.archlinux.org/title/PostgreSQL#Installation)
- 🐳 [Docker en Arch Linux](https://wiki.archlinux.org/title/Docker)

---

**¡Listo para Arch Linux con Omarchy!** 🐧✨

