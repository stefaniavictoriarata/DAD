FROM rmohr/activemq:5.15.9
WORKDIR /app

# Copy all files from the current directory to /app in the container
COPY . .

# Compile the Java class (replace MainClass.java with your actual file)
RUN javac ImageMessageListener.java

# Run the compiled Java class (replace MainClass with your actual class name)
CMD ["java", "ImageMessageListener"]

EXPOSE 61616
EXPOSE 8161