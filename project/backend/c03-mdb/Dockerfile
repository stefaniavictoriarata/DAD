# c03-mdb/Dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy all files from the current directory to /app in the container
COPY . .

# Compile the Java class (replace MainClass.java with your actual file)
RUN javac ImageProcessorMDB.java

# Run the compiled Java class (replace MainClass with your actual class name)
CMD ["java", "ImageProcessorMDB"]
EXPOSE 1099
