import javax.ejb.MessageDriven;
import javax.ejb.ActivationConfigProperty;
import javax.jms.Message;
import javax.jms.BytesMessage;
import javax.jms.MessageListener;
import java.rmi.Naming;

@MessageDriven(
    activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/topic/MyTopic")
    })
public class ImageProcessorMDB implements MessageListener {

    public void onMessage(Message message) {
        try {
            if (message instanceof BytesMessage) {
                BytesMessage byteMessage = (BytesMessage) message;
                byte[] imageData = new byte[(int) byteMessage.getBodyLength()];
                byteMessage.readBytes(imageData);

                // Call RMI servers in C04 and C05
                callRmiServer(imageData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callRmiServer(byte[] imageData) {
        try {
            // Connect to RMI Server in C04 or C05
            RmiServerInterface server = (RmiServerInterface) Naming.lookup("//localhost:1099/RmiServer");
            server.processImage(imageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
