import io.javalin.Javalin;
import io.javalin.http.UploadedFile;
import javax.jms.*;
import javax.naming.InitialContext;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageUploadApp {

    private static Topic topic; // Use Topic instead of Queue

    public static void main(String[] args) throws Exception {
        Javalin app = Javalin.create().start(8080);

        // Set up JMS
        InitialContext context = new InitialContext();
        connectionFactory = (ConnectionFactory) context.lookup("java:/ConnectionFactory");
        topic = (Topic) context.lookup("java:/jms/topic/MyTopic"); // Change to Topic

        app.post("/upload", ctx -> {
            UploadedFile uploadedFile = ctx.uploadedFile("image");
            if (uploadedFile != null) {
                Path path = Paths.get("uploads", uploadedFile.getFilename());
                Files.createDirectories(path.getParent());
                Files.copy(uploadedFile.getContent(), path);
                sendToJmsTopic(uploadedFile); // Fix function to publish to a Topic
                ctx.result("Image uploaded successfully.");
            } else {
                ctx.result("No file uploaded.");
            }
        });
    }

    private static void sendToJmsTopic(UploadedFile uploadedFile) throws Exception {
        try (Connection connection = connectionFactory.createConnection();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            MessageProducer producer = session.createProducer(topic); // Publish to Topic
            byte[] fileData = uploadedFile.getContent().readAllBytes();
            BytesMessage message = session.createBytesMessage();
            message.writeBytes(fileData);
            message.setStringProperty("fileName", uploadedFile.getFilename());
            producer.send(message);
            System.out.println("Image published to JMS Topic.");
        }
    }

}
