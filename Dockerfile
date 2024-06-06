# Utilizar la imagen oficial de Maven para compilar la aplicación
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Utilizar la imagen oficial de OpenJDK para ejecutar la aplicación
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/SCommerce-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto que usa Spring Boot
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
