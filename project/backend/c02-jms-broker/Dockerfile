# Use Maven and OpenJDK base image to compile the code
FROM maven:3.8.4-openjdk-17 as build

# Set the working directory
WORKDIR /app

# Copy the pom.xml to the container
COPY pom.xml .

# Download dependencies (offline mode)
RUN mvn dependency:go-offline

# Copy the source code (including ImageMessageListener.java)
COPY . .

# Compile the Java source code and package the application into a JAR
RUN mvn clean package

# Expose any necessary ports (for JMS, etc.)
EXPOSE 61616 8161

# Run the compiled Java class (ensure your packaged JAR is in the target folder)
CMD ["java", "-cp", "target/image-processor-1.0-SNAPSHOT.jar", "com.example.ImageMessageListener"]
