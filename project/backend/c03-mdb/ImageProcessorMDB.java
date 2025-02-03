import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.*;
import java.rmi.Naming;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
    @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/topic/MyTopic")
})
public class ImageProcessorMDB implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof BytesMessage) {
                BytesMessage bytesMessage = (BytesMessage) message;
                byte[] imageData = new byte[(int) bytesMessage.getBodyLength()];
                bytesMessage.readBytes(imageData);

                String rmiServer1 = "rmi://rmi-server1:1099/ZoomService1";
                String rmiServer2 = "rmi://rmi-server2:1099/ZoomService2";

                ZoomService zoomService1 = (ZoomService) Naming.lookup(rmiServer1);
                ZoomService zoomService2 = (ZoomService) Naming.lookup(rmiServer2);

                // Perform processing
                byte[] zoomedImage1 = zoomService1.processImage(imageData);
                byte[] zoomedImage2 = zoomService2.processImage(zoomedImage1);

                // Publish processed image to the final JMS topic
                publishToFinalTopic(zoomedImage2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void publishToFinalTopic(byte[] imageData) {
        try {
            // Set up JMS connection
            ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory(); // Replace with your provider
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create topic and producer
            Topic topic = session.createTopic("java:/jms/topic/FinalTopic");
            MessageProducer producer = session.createProducer(topic);

            // Create BytesMessage
            BytesMessage bytesMessage = session.createBytesMessage();
            bytesMessage.writeBytes(imageData);

            // Send message
            producer.send(bytesMessage);

            // Clean up
            producer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
