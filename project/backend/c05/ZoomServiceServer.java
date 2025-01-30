import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ZoomServiceServer {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099); // Start RMI registry
            ZoomService service = new ZoomServiceImpl();
            Naming.rebind("rmi://localhost:1099/ZoomService1", service);
            Naming.rebind("rmi://localhost:1099/ZoomService2", service);
            System.out.println("RMI services are running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
