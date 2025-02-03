# Use Maven and OpenJDK base image
FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /app

# Copy pom.xml and source files to the container
COPY pom.xml .
COPY . .

# Build the application using Maven
RUN mvn clean package -DskipTests

# Use OpenJDK for the runtime environment
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/image-upload-app-1.0-SNAPSHOT.jar /app/image-upload-app.jar

# Run the application
CMD ["java", "-jar", "image-upload-app.jar"]

EXPOSE 8080
