NEW_IMAGE="mescalera2787/msdwhtoros:latestV1"
OLD_IMAGE="apache/services-dwhtoros"
APP_NAME="dwhtoros"
APP_PORT="8080"

# Parar por nombre (si ya existe el contenedor estable)
docker ps -q -f name=^/${APP_NAME}$ | xargs -r docker stop
docker ps -aq -f name=^/${APP_NAME}$ | xargs -r docker rm

# Parar por imagen vieja (si existiera)
docker ps -q -f ancestor="${OLD_IMAGE}" | xargs -r docker stop
docker ps -aq -f ancestor="${OLD_IMAGE}" | xargs -r docker rm

# Parar lo que tenga el puerto 8080 (último recurso)
CID=$(docker ps -q --filter "publish=8080")
[ -n "$CID" ] && docker stop "$CID" && docker rm "$CID"

docker pull "${NEW_IMAGE}"
docker run -d --name "${APP_NAME}" \
  --restart unless-stopped \
  -p 8080:"${APP_PORT}" \
  -e JAVA_OPTS="-Xms256m -Xmx512m" \
  "${NEW_IMAGE}"

docker ps --format "table {{.Names}}\t{{.Image}}\t{{.Status}}\t{{.Ports}}"