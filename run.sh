
#!/bin/bash

echo "Configurando o xhost..."

xhost +local:docker

echo "Iniciando o ambiente WS3D em background..."
docker run --rm -d --name coppelia-sim \
 -e DISPLAY \
 --net=host \
 --privileged \
 brgsil/ws3d-coppelia

echo "Aguardando 1 segundo para o ambiente 3D carregar..."
sleep 1

echo "Iniciando o agente CST..."
./gradlew run
