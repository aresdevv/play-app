#!/bin/bash
# Script para migrar la base de datos existente en Arch Linux
# Fecha: 2025-10-13

echo "🔄 Migrando Base de Datos PlayApp..."
echo ""

# Configuración
DB_NAME="platzi_play_db"
DB_USER="aresdevv"
DB_PASSWORD="root"
DB_PORT="5433"
MIGRATION_FILE="src/main/resources/migration-tmdb-fields.sql"

echo "ℹ️  Esto agregará campos de TMDB a las tablas existentes"
echo "⚠️  Se conservarán todos los datos existentes"
echo ""
read -p "¿Continuar? (s/n): " confirm

if [ "$confirm" != "s" ]; then
    echo "❌ Operación cancelada"
    exit 1
fi

echo ""
echo "1️⃣  Verificando archivo de migración..."
if [ ! -f "$MIGRATION_FILE" ]; then
    echo "❌ Error: No se encontró $MIGRATION_FILE"
    exit 1
fi
echo "   ✅ Archivo encontrado"

echo ""
echo "2️⃣  Ejecutando migración..."
PGPASSWORD=$DB_PASSWORD psql -h localhost -p $DB_PORT -U $DB_USER -d $DB_NAME -f $MIGRATION_FILE

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Migración completada exitosamente"
    echo ""
    echo "📊 Verificando columnas agregadas..."
    PGPASSWORD=$DB_PASSWORD psql -h localhost -p $DB_PORT -U $DB_USER -d $DB_NAME -c "\d platzi_play_peliculas" | grep -E "(tmdb_id|poster_url|backdrop_url)"
    echo ""
    echo "🚀 Ya puedes ejecutar la aplicación:"
    echo "   ./gradlew bootRun"
else
    echo ""
    echo "❌ Error en la migración"
    exit 1
fi

