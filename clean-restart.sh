#!/bin/bash

echo "🛑 Stopping Docker Compose..."
docker compose down

echo "🧹 Removing leftover containers..."
docker compose rm -f

echo "🧼 Pruning Docker system (images, networks, cache)..."
docker system prune -af

# echo "🗑  Removing dangling volumes..."
# docker volume prune -f

echo "🔨 Rebuilding all services without cache..."
docker compose build --no-cache

echo "🚀 Starting services..."
docker compose up -d

echo "✅ Clean restart complete!"
docker compose ps
