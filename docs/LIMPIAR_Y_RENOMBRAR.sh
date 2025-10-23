#!/bin/bash
# Script para limpiar datos y renombrar tablas
# Fecha: 2025-10-12
# Autor: @aresdevv

echo "🧹 Limpiando y Renombrando Base de Datos..."
echo ""

# Configuración
DB_NAME="platzi_play_db"
DB_USER="aresdevv"
DB_PASSWORD="root"
DB_PORT="5433"

echo "⚠️  ADVERTENCIA: Este script va a:"
echo "   • Eliminar todos los datos de movies y reviews"
echo "   • Renombrar la tabla platzi_play_peliculas a movies"
echo "   • Renombrar la tabla platzi_play_usuarios a users"
echo "   • Renombrar columnas de español a inglés"
echo ""
read -p "¿Continuar? (s/n): " confirm

if [ "$confirm" != "s" ]; then
    echo "❌ Operación cancelada"
    exit 1
fi

echo ""
echo "1️⃣  Ejecutando script SQL de limpieza y renombramiento..."

PGPASSWORD=$DB_PASSWORD psql -h localhost -p $DB_PORT -U $DB_USER -d $DB_NAME -f LIMPIAR_Y_RENOMBRAR.sql

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Proceso completado exitosamente"
    echo ""
    echo "📊 Resumen de cambios:"
    echo "   • Tabla: platzi_play_peliculas → movies"
    echo "   • Tabla: platzi_play_usuarios → users"
    echo "   • Columnas renombradas a inglés"
    echo "   • Datos de movies y reviews eliminados"
    echo "   • Secuencias reiniciadas"
    echo ""
else
    echo ""
    echo "❌ Error durante la ejecución"
    exit 1
fi
