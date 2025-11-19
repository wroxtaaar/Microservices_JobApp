#!/usr/bin/env bash
set -euo pipefail

echo "🔎 Detecting Codespaces environment..."

if [[ -z "${CODESPACE_NAME:-}" ]]; then
  echo "❌ Not running in GitHub Codespaces."
  exit 1
fi

DOMAIN="${GITHUB_CODESPACES_PORT_FORWARDING_DOMAIN:-app.github.dev}"
CSPACE="$CODESPACE_NAME"

echo "🌐 Codespace: $CSPACE"
echo "🌍 Domain:   $DOMAIN"


# Ports your services use
declare -A PORT_MAP=(
  [gateway]=8080
  [company]=8081
  [job]=8082
  [review]=8083
  [eureka]=8761
  [config]=8888
  [zipkin]=9411
  [rabbit]=15672
)

ENV_FILE=".codespaces-urls.env"
: > "$ENV_FILE"

echo "📡 Generating URLs..."
for KEY in "${!PORT_MAP[@]}"; do
  PORT="${PORT_MAP[$KEY]}"
  URL="https://${CSPACE}-${PORT}.${DOMAIN}"
  echo "URL_${KEY}=${URL}" | tee -a "$ENV_FILE"
done

echo "✅ Saved URLs to $ENV_FILE"


# Update docker-compose.yml
echo "⚙️ Updating docker-compose.yml..."

COMPOSE="docker-compose.yml"
TMP="$(mktemp)"

cp "$COMPOSE" "$TMP"

for KEY in "${!PORT_MAP[@]}"; do
  PORT="${PORT_MAP[$KEY]}"
  URL="https://${CSPACE}-${PORT}.${DOMAIN}"
  
  SERVICE=""
  case "$KEY" in
    gateway) SERVICE="gateway" ;;
    company) SERVICE="company-service" ;;
    job) SERVICE="job-service" ;;
    review) SERVICE="review-service" ;;
    eureka) SERVICE="eureka" ;;
    config) SERVICE="config-server" ;;
    zipkin) SERVICE="zipkin" ;; # UI only
    rabbit) SERVICE="rabbitmq" ;; # Management UI only
  esac

  if grep -q "$SERVICE:" "$TMP"; then
    echo "🔧 Injecting PUBLIC_URL for $SERVICE → $URL"
    awk -v svc="$SERVICE" -v url="$URL" '
      $0 ~ svc":" {
        print
        getline
        print "      environment:"
        print "        PUBLIC_URL: \"" url "\""
        next
      }
      { print }
    ' "$TMP" > "$TMP.new"
    mv "$TMP.new" "$TMP"
  fi
done

mv "$TMP" "$COMPOSE"
echo "✅ docker-compose.yml updated."


# Update Spring Boot application-docker.properties
update_properties() {
  PROP_FILE="$1"
  URL_VAR="$2"
  
  if [[ -f "$PROP_FILE" ]]; then
    echo "🔧 Updating $PROP_FILE..."
    sed -i "/management.endpoints.web.exposure.include/d" "$PROP_FILE"
    sed -i "/management.endpoint.health.show-details/d" "$PROP_FILE"
    sed -i "/server.forward-headers-strategy/d" "$PROP_FILE"
    sed -i "/management.endpoints.web.base-url/d" "$PROP_FILE"
    
    {
      echo "management.endpoints.web.exposure.include=*"
      echo "management.endpoint.health.show-details=always"
      echo "server.forward-headers-strategy=native"
      echo "management.endpoints.web.base-url=\${PUBLIC_URL}/actuator"
    } >> "$PROP_FILE"
  fi
}

update_properties "./companyms/src/main/resources/application-docker.properties" "$URL_company"
update_properties "./jobms/src/main/resources/application-docker.properties" "$URL_job"
update_properties "./reviewms/src/main/resources/application-docker.properties" "$URL_review"
update_properties "./gateway/src/main/resources/application-docker.properties" "$URL_gateway"
update_properties "./config-server/src/main/resources/application-docker.properties" "$URL_config"
update_properties "./service-reg/src/main/resources/application-docker.properties" "$URL_eureka"

echo "🚀 Restarting updated containers..."
docker compose down
docker compose up -d --build

echo "🎉 Done!"
echo "All services now use real Codespaces URLs!"
