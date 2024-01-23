# Patient-demo application

## To Start:

### Local Build (using local jdk)

First run `mvn clean package -DskipTests`<br>
Then run `docker-compose up -d --build`<br>

### Full container build (if you don't have proper jdk)

For fully containerized build checkout the `container-build` branch<br>
Then run `docker-compose up -d --build`<br>
It is done through a multi-build process inside the containers, you don't need to have a compiler installed
___
### Swagger-UI
Service listens on port `8181`<br>
You can follow `localhost:8181` to access swagger-ui and test the API
___

**Postgres DB** is instantiated with some test data<br>
You can disable it in the application.yaml by changing `spring.sql.init.mode` to *never*