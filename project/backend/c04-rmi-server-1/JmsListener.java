import javax.jms.*;
import javax.naming.InitialContext;

public class JmsListener {

    public static void main(String[] args) {
        try {
            // Set up JMS
            InitialContext context = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("java:/ConnectionFactory");
            Queue queue = (Queue) context.lookup("java:/jms/queue/MyQueue");
            Topic topic = (Topic) context.lookup("java:/jms/topic/MyTopic");

            try (Connection connection = connectionFactory.createConnection();
                 Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
                MessageConsumer consumer = session.createConsumer(queue);
                MessageProducer producer = session.createProducer(topic);

                connection.start();

                System.out.println("Listening for messages...");
                consumer.setMessageListener(message -> {
                    try {
                        if (message instanceof BytesMessage) {
                            BytesMessage bytesMessage = (BytesMessage) message;
                            byte[] imageData = new byte[(int) bytesMessage.getBodyLength()];
                            bytesMessage.readBytes(imageData);

                            // Forward message to topic
                            BytesMessage forwardMessage = session.createBytesMessage();
                            forwardMessage.writeBytes(imageData);
                            producer.send(forwardMessage);
                            System.out.println("Message forwarded to topic.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
