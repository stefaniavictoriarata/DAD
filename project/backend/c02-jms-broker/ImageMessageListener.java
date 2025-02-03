import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.BytesMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

@MessageDriven(
    activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/topic/MyTopic")
    }
)
public class ImageMessageListener implements MessageListener {

    public void onMessage(Message message) {
        try {
            if (message instanceof BytesMessage) {
                BytesMessage bytesMessage = (BytesMessage) message;
                byte[] fileData = new byte[(int) bytesMessage.getBodyLength()];
                bytesMessage.readBytes(fileData);
                String filename = bytesMessage.getStringProperty("fileName");

                System.out.println("Received image from JMS: " + filename);

                byte[] processedImage = processImageWithRmi(filename, fileData);

                System.out.println("Processed image and sending to DB.");
                // Add DB interaction here to store processedImage
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] processImageWithRmi(String filename, byte[] fileData) {
        try {
            String rmiHost = System.getenv("RMI_HOST"); // Make this dynamic
            Registry registry = LocateRegistry.getRegistry(rmiHost, 1099);
            ImageProcessor stub = (ImageProcessor) registry.lookup("ImageProcessorService");
            return stub.processImage(fileData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
