import io.javalin.Javalin;
import io.javalin.http.UploadedFile;
import javax.jms.*;
import javax.naming.InitialContext;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageUploadApp {

    private static Topic topic; // Use Topic instead of Queue
    private static ConnectionFactory connectionFactory;

    public static void main(String[] args) throws Exception {
        Javalin app = Javalin.create().start(8080);

        String brokerUrl = "tcp://activemq:61616";  // Container link name
        connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        topic = session.createTopic("ImageProcessingTopic");

        app.post("/upload", ctx -> {
            UploadedFile uploadedFile = ctx.uploadedFile("image");
            if (uploadedFile != null) {
                Path path = Paths.get("uploads", uploadedFile.getFilename());
                Files.createDirectories(path.getParent());
                Files.copy(uploadedFile.getContent(), path);
                sendToJmsTopic(uploadedFile);
                ctx.result("Image uploaded successfully.");
            } else {
                ctx.result("No file uploaded.");
            }
        });

        // Endpoint to receive notification from C03 that image processing is done
        app.post("/notify", ctx -> {
            String downloadUrl = ctx.formParam("downloadUrl");
            if (downloadUrl != null) {
                // Redirect front-end to download URL served by C06
                ctx.redirect(downloadUrl);
            } else {
                ctx.status(400).result("No download URL provided.");
            }
        });
    }

    private static void sendToJmsTopic(UploadedFile uploadedFile) throws Exception {
        try (Connection connection = connectionFactory.createConnection();
             Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)) {

            MessageProducer producer = session.createProducer(topic);
            byte[] fileData = uploadedFile.getContent().readAllBytes();
            BytesMessage message = session.createBytesMessage();
            message.writeBytes(fileData);
            message.setStringProperty("fileName", uploadedFile.getFilename());
            producer.send(message);
            System.out.println("Image published to JMS Topic.");
        }
    }
}