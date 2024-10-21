# Usar una imagen oficial de Java 21
FROM eclipse-temurin:21-jdk-alpine

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el JAR de la aplicación
COPY target/crm-service-0.0.1-SNAPSHOT.jar crm-service.jar

# Exponer el puerto de la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "crm-service.jar"]