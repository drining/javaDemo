# ==================== Stage 1: 构建 ====================
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /build
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests -B

# ==================== Stage 2: 运行 ====================
FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=builder /build/target/*.jar app.jar

EXPOSE 9002

ENTRYPOINT ["java", "-jar", "app.jar"]
