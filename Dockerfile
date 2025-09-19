# Etapa de construcción
FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app

# Copiamos el descriptor de dependencias y resolvemos
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copiamos todo el código fuente
COPY . ./

# Construimos el JAR sin ejecutar tests
RUN mvn clean package -DskipTests

# Etapa de ejecución (más liviana)
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copiamos solo el JAR desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto que usa Spring Boot por defecto
EXPOSE 8080

# Comando para ejecutar la app
CMD ["java", "-jar", "app.jar"]