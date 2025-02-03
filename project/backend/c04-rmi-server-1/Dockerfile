FROM openjdk:17-jdk-slim
WORKDIR /app
COPY . .
COPY libs/activemq-client-5.15.9.jar libs/
RUN javac -cp ".:libs/activemq-client-5.15.9.jar" JmsListener.java
CMD ["java", "-cp", ".:libs/activemq-client-5.15.9.jar", "JmsListener"]
