#!/bin/bash

echo "🏗️  Сборка DEV образа..."

# DEV конфигурация
cat > src/main/resources/application.properties << EOF
server.port=8080
netology.profile.dev=true
EOF

mvn clean package

# ✅ Исправленный Dockerfile
cat > Dockerfile.dev << 'EOF'
FROM eclipse-temurin:21-jdk-alpine
EXPOSE 8080
ADD target/springBoot-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EOF

docker build -f Dockerfile.dev -t devapp .

echo "✅ DEV образ собран"

echo "🏗️  Сборка PROD образа..."

# PROD конфигурация
cat > src/main/resources/application.properties << EOF
server.port=8081
netology.profile.dev=false
EOF

mvn clean package

# ✅ Исправленный Dockerfile
cat > Dockerfile.prod << 'EOF'
FROM eclipse-temurin:21-jdk-alpine
EXPOSE 8081
ADD target/springBoot-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EOF

docker build -f Dockerfile.prod -t prodapp .

echo "✅ PROD образ собран"

# Очищаем временные файлы
rm -f Dockerfile.dev Dockerfile.prod

echo ""
echo "📦 Проверка образов:"
docker images | grep app

echo ""
echo "🚀 Запуск тестов..."

mvn clean test