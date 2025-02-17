# Usa una imagen base de OpenJDK 17 optimizada para Alpine Linux
FROM eclipse-temurin:17-jdk-alpine

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia todos los archivos del proyecto al contenedor
COPY . .

# Da permisos de ejecución al wrapper de Gradle
RUN chmod +x ./gradlew

# Construye la aplicación con Gradle
RUN ./gradlew build --no-daemon -x test

# Copia el archivo JAR generado al contenedor
RUN cp build/libs/prueba-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto 8080
EXPOSE 8080

# Ejecutar la aplicación
CMD ["java", "-jar", "app.jar"]
