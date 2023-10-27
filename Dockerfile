FROM openjdk:17-jdk-slim
WORKDIR /app

COPY target/movie-library-service-1.0.jar app.jar
COPY config-prod.yml config.yml
COPY src/main/java/com/movielibrary/db/migration /app/migration

EXPOSE 8080
CMD ["java", "-jar", "app.jar", "server", "config.yml"]