import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.BytesMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.naming.InitialContext;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;

@MessageDriven(
    activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/topic/MyTopic")
    })
public class ImageMessageListener implements MessageListener {

    private static ConnectionFactory connectionFactory;

    public void onMessage(Message message) {
        try {
            if (message instanceof BytesMessage) {
                BytesMessage byteMessage = (BytesMessage) message;
                byte[] data = new byte[(int) byteMessage.getBodyLength()];
                byteMessage.readBytes(data);
                // Process the image (e.g., save it to DB, send it to RMI servers, etc.)
                // Here we simulate image processing and then pass the job along
                System.out.println("Received image, processing...");
                processImage(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processImage(byte[] imageData) {
        // Further image processing logic (resize, store, etc.)
        System.out.println("Processing image...");
    }
}
