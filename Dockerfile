# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory to /app
WORKDIR /app

# Copy the packaged JAR file into the container's /app directory
# The `mvn clean install` command that Render runs will put the JAR in target/
COPY target/*.jar app.jar

# Expose the port that your Spring Boot application runs on (default is 8080)
EXPOSE 8080

# Define the command to run your Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]