# 🔄 Guía de Renombramiento y Limpieza

## 📋 Resumen

Se han creado **3 opciones** para limpiar y renombrar tu base de datos:

---

## 🎯 Opción 1: Limpiar + Renombrar (RECOMENDADO)

### ¿Qué hace?
- ✅ Limpia todos los datos de películas y reviews
- ✅ Renombra tabla `platzi_play_peliculas` → `movies`
- ✅ Renombra columnas a inglés (`titulo` → `title`, etc.)
- ✅ Actualiza código Java para usar nuevos nombres

### Ejecutar

```bash
# Desde psql
psql -U aresdevv -d platzi_play_db -f LIMPIAR_Y_RENOMBRAR.sql

# O desde terminal directamente
psql -U aresdevv -d platzi_play_db < LIMPIAR_Y_RENOMBRAR.sql
```

---

## 🎯 Opción 2: Solo Limpiar (Sin renombrar)

### ¿Qué hace?
- ✅ Limpia todos los datos
- ❌ NO renombra tablas
- ⚠️ PROBLEMA: El código Java YA fue actualizado para usar nuevos nombres

**⚠️ NO RECOMENDADO** - El código espera tabla `movies` pero BD tiene `platzi_play_peliculas`

### Ejecutar (solo si sabes lo que haces)

```bash
psql -U aresdevv -d platzi_play_db -f LIMPIAR_SIMPLE.sql
```

---

## 🎯 Opción 3: Solo Renombrar (Sin limpiar)

### ¿Qué hace?
- ✅ Renombra tabla y columnas
- ❌ NO borra datos
- ✅ Mantiene películas y reviews existentes

### Ejecutar

```bash
psql -U aresdevv -d platzi_play_db -f RENOMBRAR_TABLAS.sql
```

---

## 🚀 Pasos Recomendados (Opción 1)

### 1. Backup (Opcional pero recomendado)

```bash
# Hacer backup antes de cambios
pg_dump -U aresdevv platzi_play_db > backup_antes_renombrar_$(date +%Y%m%d).sql
```

### 2. Detener Aplicación

```bash
# Si está corriendo, detenerla
pkill -f "play-app"
```

### 3. Ejecutar Script

```bash
# Conectar y ejecutar
psql -U aresdevv -d platzi_play_db -f LIMPIAR_Y_RENOMBRAR.sql
```

**Resultado esperado:**
```
TRUNCATE TABLE
TRUNCATE TABLE
ALTER TABLE
ALTER SEQUENCE
ALTER TABLE
ALTER TABLE
ALTER TABLE
ALTER TABLE
ALTER TABLE
ALTER TABLE
```

### 4. Verificar Cambios

```bash
# Conectar a BD
psql -U aresdevv -d platzi_play_db

# Verificar tabla renombrada
\d movies;

# Verificar estructura
SELECT column_name, data_type 
FROM information_schema.columns 
WHERE table_name = 'movies';

# Verificar que no hay datos
SELECT COUNT(*) FROM movies;     -- Debe ser 0
SELECT COUNT(*) FROM reviews;    -- Debe ser 0

# Salir
\q
```

### 5. Iniciar Aplicación

```bash
# El código Java ya está actualizado ✅
./gradlew bootRun
```

### 6. Probar

```bash
# Importar primera película
curl -X POST "http://localhost:8090/play-app/api/movies/import-from-tmdb/603"

# Verificar en BD
psql -U aresdevv -d platzi_play_db -c "SELECT id, title, tmdb_id FROM movies;"
```

---

## 📊 Cambios en Base de Datos

### Antes

```sql
platzi_play_peliculas:
  - titulo          VARCHAR(150)
  - duracion        INTEGER
  - genero          VARCHAR(40)
  - fecha_estreno   DATE
  - clasificacion   DECIMAL(3,2)
  - estado          VARCHAR(1)
  - tmdb_id         BIGINT
  - poster_url      VARCHAR(500)
  - ...
```

### Después

```sql
movies:
  - title           VARCHAR(150)
  - duration        INTEGER
  - genre           VARCHAR(40)
  - release_date    DATE
  - rating          DECIMAL(3,2)
  - status          VARCHAR(1)
  - tmdb_id         BIGINT
  - poster_url      VARCHAR(500)
  - ...
```

---

## 🔧 Cambios en Código Java

Ya están aplicados automáticamente:

### MovieEntity.java
```java
// Antes
@Table(name = "platzi_play_peliculas")
private String titulo;
private Integer duracion;
private String genero;

// Después
@Table(name = "movies")
private String title;
private Integer duration;
private String genre;
```

### CrudMovieEntity.java
```java
// Antes
MovieEntity findFirstByTitulo(String titulo);

// Después
MovieEntity findFirstByTitle(String title);
```

### MovieMapper.java
```java
// Antes
@Mapping(source = "titulo", target = "title")

// Después
@Mapping(source = "title", target = "title")
```

---

## ⚠️ Importante

### Si solo limpias sin renombrar (Opción 2):

Tendrás que **revertir** los cambios en el código Java o **ejecutar también** el script de renombramiento.

### Si solo renombras sin limpiar (Opción 3):

Tus películas existentes se mantendrán, pero ahora la tabla se llama `movies` con columnas en inglés.

---

## 🐛 Troubleshooting

### Error: "relation platzi_play_peliculas does not exist"

**Solución:** Ya renombraste la tabla. Está bien, el código ya usa `movies`.

### Error: "column titulo does not exist"

**Solución:** Ya renombraste las columnas. Está bien, el código ya usa `title`.

### Error: "relation movies does not exist"

**Solución:** No has ejecutado el script de renombramiento. Ejecuta:
```bash
psql -U aresdevv -d platzi_play_db -f RENOMBRAR_TABLAS.sql
```

---

## ✅ Verificación Final

Después de ejecutar los scripts:

```bash
# 1. Verificar tablas
psql -U aresdevv -d platzi_play_db -c "\dt"

# Deberías ver:
#  Schema | Name    | Type  | Owner
# --------+---------+-------+----------
#  public | movies  | table | aresdevv  ✅
#  public | reviews | table | aresdevv
#  public | users   | table | aresdevv

# 2. Verificar columnas de movies
psql -U aresdevv -d platzi_play_db -c "\d movies"

# Deberías ver columnas en inglés:
#  title, duration, genre, release_date, rating, status, tmdb_id, etc.

# 3. Verificar datos
psql -U aresdevv -d platzi_play_db -c "SELECT COUNT(*) FROM movies;"

# Debe ser 0 si ejecutaste LIMPIAR_Y_RENOMBRAR.sql
```

---

## 📝 Resumen de Scripts

| Script | Limpia Datos | Renombra Tabla | Renombra Columnas |
|--------|--------------|----------------|-------------------|
| `LIMPIAR_Y_RENOMBRAR.sql` | ✅ | ✅ | ✅ | ← **RECOMENDADO**
| `LIMPIAR_SIMPLE.sql` | ✅ | ❌ | ❌ |
| `RENOMBRAR_TABLAS.sql` | ❌ | ✅ | ✅ |

---

## 🎉 ¡Listo!

Después de ejecutar `LIMPIAR_Y_RENOMBRAR.sql`:

✅ Base de datos limpia (tablas vacías)  
✅ Tabla renombrada a `movies`  
✅ Columnas en inglés  
✅ Código Java actualizado  
✅ Listo para importar desde TMDB  

**Siguiente paso:** Iniciar aplicación y probar importación de películas 🚀

