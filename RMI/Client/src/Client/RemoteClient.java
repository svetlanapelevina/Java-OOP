package Client;

import Common.RemoteUtility;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.List;

public class RemoteClient {

    public static void main(String[] args) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            RemoteUtility remoteUtilityStub = (RemoteUtility) registry.lookup("MyRemoteUtility");

            String reply = remoteUtilityStub.echo("Сообщение от RMI-клиента эхо-серверу");
            System.out.println(reply);

            Double[] test = new Double[] {1., 2., 3., 4., 16.};

            List<Double> squares = remoteUtilityStub.getSquare(Arrays.asList(test));
            List<Double> roots = remoteUtilityStub.getRoots(Arrays.asList(test));

            System.out.println("\nSquares: ");
            squares.forEach((element) -> System.out.print(String.format("%.1f", element) + " \t"));
            System.out.println("\nRoots: ");
            roots.forEach((element) -> System.out.print(String.format("%.1f", element) + " \t"));

        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

}
