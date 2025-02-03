FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy all files to /app in the container
COPY . .

# Compile all Java files
RUN javac ZoomService.java ZoomServiceImpl.java ZoomServiceServer.java

# Run the RMI Server
CMD ["java", "ZoomServiceServer"]

# Expose the RMI Registry Port
EXPOSE 1099