FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/ZoomServiceImpl.jar app.jar
EXPOSE 1099
ENTRYPOINT ["java", "-cp", "app.jar", "com.example.ZoomServiceServer"]
