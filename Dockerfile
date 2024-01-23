#local application packaging, need to run mvn clean package first

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY ./target/*.jar .
ENTRYPOINT ["java","-jar","patient-demo.jar"]


#run version below to build application inside the container

#FROM maven:3.8.5-openjdk-17 AS build
#WORKDIR /app
#COPY . .
#RUN mvn clean package -DskipTests
#
#FROM eclipse-temurin:17-jdk-alpine
#WORKDIR /app
#COPY --from=build /app/target/*.jar .
#ENTRYPOINT ["java","-jar","weather-demo.jar"]