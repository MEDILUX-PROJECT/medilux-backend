# === Stage 1: Build ===
# 베이스 이미지: OpenJDK 17 (필요에 따라 slim 버전 사용)
FROM gradle:8.10.2-jdk17 AS builder
# 작업 디렉토리 생성 및 설정
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# === Stage 2: Run ===
FROM openjdk:17-jdk-slim
WORKDIR /app
# 실행 가능한 JAR 파일은 빌드 산출물 경로에 맞게 지정 (예: moasis-0.0.1-SNAPSHOT.jar)
COPY --from=builder /app/build/libs/moasis-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
