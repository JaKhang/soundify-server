# Use OpenJDK as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy JAR file (Make sure your JAR is built)
COPY build/libs/soundify.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]