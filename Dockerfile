# Dockerfile
FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /app/target/bankingapp-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 3000

CMD ["java", "-jar", "app.jar"]


