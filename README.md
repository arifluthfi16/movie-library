# MovieLibraryService

How to start the MovieLibraryService application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/movie-library-service-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

# Building The App

1. Set version on
    ```
    <groupId>com.movielibrary</groupId>
    <artifactId>movie-library-service</artifactId>
    <version>1.0</version>
    <packaging>jar</packaging>  
    ```
2. To build the service run `mvn package`
3. To run the build `java -jar target/movie-library-service-1.0.jar server config.yml`

# Running Dockerfile

1. Set the target into existing build target
2. Build the docker image `docker build -t auth-service:1.0 .`
3. Run the dockerfile using `docker run -d -p 8080:8082 auth-service:1.0`
