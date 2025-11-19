docker compose down --volumes --remove-orphans
docker system prune -af
docker volume prune -f