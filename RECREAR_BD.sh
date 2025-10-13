#!/bin/bash
# Script para recrear la base de datos en Arch Linux
# Fecha: 2025-10-13

echo "🗑️  Recreando Base de Datos PlayApp..."
echo ""

# Configuración
DB_NAME="platzi_play_db"
DB_USER="aresdevv"
DB_PASSWORD="root"
DB_PORT="5433"

echo "⚠️  ADVERTENCIA: Esto eliminará TODOS los datos existentes"
read -p "¿Estás seguro? (s/n): " confirm

if [ "$confirm" != "s" ]; then
    echo "❌ Operación cancelada"
    exit 1
fi

echo ""
echo "1️⃣  Deteniendo la aplicación (si está corriendo)..."
# pkill -f "play-app" 2>/dev/null || true

echo "2️⃣  Eliminando base de datos antigua..."
PGPASSWORD=$DB_PASSWORD psql -h localhost -p $DB_PORT -U $DB_USER -d postgres -c "DROP DATABASE IF EXISTS $DB_NAME;"

echo "3️⃣  Creando base de datos nueva..."
PGPASSWORD=$DB_PASSWORD psql -h localhost -p $DB_PORT -U $DB_USER -d postgres -c "CREATE DATABASE $DB_NAME;"

echo "4️⃣  Base de datos recreada exitosamente ✅"
echo ""
echo "📝 Próximo paso: Ejecutar la aplicación"
echo "   ./gradlew bootRun"
echo ""
echo "ℹ️  Hibernate creará automáticamente las tablas con los campos de TMDB"
echo ""

