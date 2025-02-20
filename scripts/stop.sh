#!/bin/bash

set -e

CONTAINER_NAME="medilux-server"

# 현재 실행 중인 컨테이너가 있는지 확인
RUNNING_CONTAINER=$(docker ps -q -f name=$CONTAINER_NAME)

if [ -n "$RUNNING_CONTAINER" ]; then
  echo "Stopping and removing existing container: $CONTAINER_NAME"
  docker stop $CONTAINER_NAME
  docker rm $CONTAINER_NAME
else
  echo "No running container named $CONTAINER_NAME."
fi
