FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/movie-library-service-1.0.jar app.jar
COPY config.yml config.yml
EXPOSE 8082
CMD ["java", "-jar", "app.jar", "server", "config.yml"]