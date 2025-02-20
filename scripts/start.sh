#!/bin/bash

# 스크립트 실행 중 에러 발생 시 중단
set -e

# [1] Docker 이미지 정보 (ECR)
ECR_REGISTRY="203918878480.dkr.ecr.ap-northeast-2.amazonaws.com"
ECR_REPOSITORY="medilux_project/moasis"
IMAGE_TAG="latest"   # 혹은 CodeDeploy에서 전달받은 태그, 또는 환경변수로 지정

# [2] 컨테이너 설정
CONTAINER_NAME="medilux-server"
HOST_PORT=8080
CONTAINER_PORT=8080

# [3] Docker 로그인 (CodeDeploy 스크립트에서 필요하다면)
# 이미 GitHub Actions에서 로그인 후 푸시했더라도, EC2에서는 새로 pull해야 하므로 로그인 필요
aws ecr get-login-password --region ap-northeast-2 \
| docker login --username AWS --password-stdin $ECR_REGISTRY

# [4] 최신 이미지 pull
echo "Pulling the Docker image: $ECR_REGISTRY/$ECR_REPOSITORY:$IMAGE_TAG"
docker pull $ECR_REGISTRY/$ECR_REPOSITORY:$IMAGE_TAG

# [5] 새 컨테이너 실행
echo "Starting container: $CONTAINER_NAME"
docker run -d --name $CONTAINER_NAME -p $HOST_PORT:$CONTAINER_PORT \
  $ECR_REGISTRY/$ECR_REPOSITORY:$IMAGE_TAG

echo "Container $CONTAINER_NAME is now running."
