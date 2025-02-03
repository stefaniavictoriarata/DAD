FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /app

# Copy the pom.xml and other necessary files first to cache dependencies
COPY pom.xml .
COPY . .

# Run the compiled class (replace with your main class name)
CMD ["java", "ImageProcessorMDB"]

# Expose necessary ports
EXPOSE 1099
