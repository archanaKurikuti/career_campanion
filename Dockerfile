# Multi-stage Dockerfile for Career Companion Spring Boot Backend

# Stage 1: Build JAR
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Package application executable JAR
RUN mvn clean package -DskipTests

# Stage 2: Production Execution Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy packaged JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run Spring Boot Application
ENTRYPOINT ["java", "-jar", "app.jar"]
