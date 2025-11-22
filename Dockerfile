FROM maven:3.9-eclipse-temurin-21-jammy AS build
WORKDIR /app

RUN apt-get update && apt-get install -y --no-install-recommends nodejs npm git && rm -rf /var/lib/apt/lists/*

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn -e clean vaadin:build-frontend package -Pproduction -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]