# Use a base image that contains JDK 21
FROM openjdk:21

# Set the working directory inside the container
WORKDIR /app
VOLUME /app
# Copy the jar file into the container
COPY build/libs/soundify.jar soundify.jar
# Expose the port your application runs on
EXPOSE 8080

# Command to run the jar file
ENTRYPOINT ["java", "-jar", "soundify.jar"]
