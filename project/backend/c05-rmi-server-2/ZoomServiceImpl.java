import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.imageio.ImageIO;

public class ZoomServiceImpl extends UnicastRemoteObject implements ZoomService {

    protected ZoomServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public byte[] processImage(byte[] imageData, double zoomFactor) throws RemoteException {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
            BufferedImage originalImage = ImageIO.read(bais);

            // Calculate new dimensions
            int newWidth = (int) (originalImage.getWidth() * zoomFactor);
            int newHeight = (int) (originalImage.getHeight() * zoomFactor);

            // Create new scaled BufferedImage
            BufferedImage zoomedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
            Graphics2D g = zoomedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g.dispose();

            // Convert BufferedImage back to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(zoomedImage, "bmp", baos);

            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error processing image", e);
        }
    }
}
