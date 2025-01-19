# Use the OpenJDK image
FROM openjdk:17

# Set the working directory
WORKDIR /usr/src/app

# Copy the JAR file
COPY target/backend-app.jar backend-app.jar

# Run the application
CMD ["java", "-jar", "backend-app.jar"]

# Expose port
EXPOSE 8080
