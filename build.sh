#!/bin/bash

echo "🔧 Iniciando build da aplicação..."

# Defina o nome da imagem e a tag
IMAGE_NAME="luizalabs-app"
IMAGE_TAG="latest"

# Limpa builds anteriores (opcional)
echo "🧹 Removendo imagens antigas..."
docker rmi ${IMAGE_NAME}:${IMAGE_TAG} --force 2> /dev/null

# Build da imagem Docker
echo "🐳 Construindo imagem Docker..."
docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .

# Opcional: executar container automaticamente
echo "🚀 Executando container..."
docker run -d -p 8080:8080 --name ${IMAGE_NAME}-container ${IMAGE_NAME}:${IMAGE_TAG}

echo "✅ Build concluído com sucesso!"
