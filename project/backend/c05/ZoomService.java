import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ZoomService extends Remote {
    byte[] processImage(byte[] imageData) throws RemoteException;
}
