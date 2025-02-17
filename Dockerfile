# Usa una imagen base de OpenJDK 17 optimizada para Alpine Linux
FROM eclipse-temurin:17-jdk-alpine

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR generado por Gradle al contenedor
COPY build/libs/prueba-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto 8080 (o el que uses en tu aplicación)
EXPOSE 8080

# Comando para ejecutar la aplicación Spring Boot
CMD ["java", "-jar", "app.jar"]
