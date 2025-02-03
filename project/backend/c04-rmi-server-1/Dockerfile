# Use the openjdk:17-jdk-slim image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the entire project into the container
COPY . .
# Copy the jndi.properties file into the resources directory
RUN mkdir -p /app/resources
COPY ./resources/jndi.properties /app/resources/jndi.properties

# Set the classpath to include the resources folder and target folder (where the compiled classes are)
ENV CLASSPATH="/app:/app/resources:/app/target/*"

# Command to run the JmsListener class
CMD ["java", "JmsListener"]
