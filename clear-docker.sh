#!/bin/bash

echo "🗑️  Очистка Docker..."

# 1. Остановить все контейнеры
echo "Останавливаем контейнеры..."
docker stop $(docker ps -aq) 2>/dev/null

# 2. Удалить все контейнеры
echo "Удаляем контейнеры..."
docker rm $(docker ps -aq) 2>/dev/null

# 3. Удалить все образы
echo "Удаляем образы..."
docker rmi -f $(docker images -q) 2>/dev/null

# 4. Очистить кэш
echo "Очищаем кэш..."
docker system prune -f

echo "✅ Готово! Docker очищен."
echo ""
echo "📦 Соберите образы заново:"
echo "  ./build-images.sh"