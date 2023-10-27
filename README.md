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
3. To build the service run `mvn package`
4. To run the build `java -jar target/movie-library-service-1.0.jar`
5. 
