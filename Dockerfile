FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY ./target/*.jar .
ENTRYPOINT ["java","-jar","patient-demo.jar"]