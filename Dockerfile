# ===============================
# ETAPA 1: COMPILAR EL PROYECTO
# ===============================
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -DskipTests clean package


# ===============================
# ETAPA 2: IMAGEN LIGERA PARA EJECUTAR
# ===============================
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8000

ENV SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/clinica \
    SPRING_DATASOURCE_USERNAME=root \
    SPRING_DATASOURCE_PASSWORD=1234

ENTRYPOINT ["java","-jar","app.jar"]

