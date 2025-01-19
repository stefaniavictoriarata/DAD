import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class RmiServer extends UnicastRemoteObject implements RmiServerInterface {

    public RmiServer() throws RemoteException {
        super();
    }

    @Override
    public void processImage(byte[] imageData) throws RemoteException {
        System.out.println("RMI Server processing image...");
        // Actual image processing logic (e.g., zoom, resize, etc.)
    }
}
