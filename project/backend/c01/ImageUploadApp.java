import io.javalin.Javalin;
import io.javalin.http.UploadedFile;
import javax.jms.*;
import javax.naming.InitialContext;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageUploadApp {

    private static ConnectionFactory connectionFactory;
    private static Queue queue;

    public static void main(String[] args) throws Exception {
        Javalin app = Javalin.create().start(8080);

        // Set up JMS
        InitialContext context = new InitialContext();
        connectionFactory = (ConnectionFactory) context.lookup("java:/ConnectionFactory");
        queue = (Queue) context.lookup("java:/jms/queue/MyQueue");

        app.post("/upload", ctx -> {
            UploadedFile uploadedFile = ctx.uploadedFile("image");
            if (uploadedFile != null) {
                Path path = Paths.get("uploads", uploadedFile.getFilename());
                Files.createDirectories(path.getParent());
                Files.copy(uploadedFile.getContent(), path);
                // Send the uploaded file to the JMS topic
                sendToJmsTopic(uploadedFile);
                ctx.result("Image uploaded successfully.");
            } else {
                ctx.result("No file uploaded.");
            }
        });
    }

    private static void sendToJmsTopic(UploadedFile uploadedFile) throws Exception {
        // Create JMS connection and session
        try (Connection connection = connectionFactory.createConnection();
             Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            // Create a message producer to send the image
            MessageProducer producer = session.createProducer(queue);
            byte[] fileData = uploadedFile.getContent().readAllBytes();
            BytesMessage message = session.createBytesMessage();
            message.writeBytes(fileData);
            producer.send(message);
        }
    }
}
