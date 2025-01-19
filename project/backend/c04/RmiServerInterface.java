import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiServerInterface extends Remote {
    void processImage(byte[] imageData) throws RemoteException;
}
