# --- STAGE 1: Build the application ---
# Use a Maven image that includes JDK for compiling
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

# Set the working directory inside the builder container
WORKDIR /app

# Copy the Maven pom.xml and source code
COPY pom.xml .
COPY src ./src

# Package the application into a JAR file
# -DskipTests is good practice for builds, assuming tests run separately
RUN mvn clean install -DskipTests

# --- STAGE 2: Create the final runtime image ---
# Use a lightweight JRE (Java Runtime Environment) image for the final application
FROM eclipse-temurin:21-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port your Spring Boot application runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]