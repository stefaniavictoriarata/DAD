FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy all files from the current directory to /app in the container
COPY . .

# Compile the Java class (replace MainClass.java with your actual file)
RUN javac ImageUploadApp.java

# Run the compiled Java class (replace MainClass with your actual class name)
CMD ["java", "ImageUploadApp"]

# Expose port 8080
EXPOSE 8080