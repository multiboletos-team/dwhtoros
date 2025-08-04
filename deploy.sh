#!/bin/bash
start=$(date +"%s")

# Crear clave temporal para SSH
echo "${SERVER_KEY}" > key.pem
chmod 600 key.pem

ssh -i key.pem -p ${SERVER_PORT} ${SERVER_USER}@${SERVER_HOST} -o StrictHostKeyChecking=no << 'ENDSSH'
    echo "Conectado a EC2. Desplegando contenedor..."

    CONTAINER_NAME=dhwtoros
    IMAGE_NAME=mescalera2787/msdwhtoros:latest

    docker pull $IMAGE_NAME

    if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
        echo "Deteniendo contenedor anterior..."
        docker stop $CONTAINER_NAME
    fi

    docker run -d --rm -p 8080:8080 --name $CONTAINER_NAME $IMAGE_NAME
ENDSSH

result=$?
rm -f key.pem

end=$(date +"%s")
diff=$(($end - $start))

if [ $result -eq 0 ]; then
  echo "✅ Despliegue completado en ${diff}s"
  exit 0
else
  echo "❌ Error en el despliegue"
  exit 1
fi