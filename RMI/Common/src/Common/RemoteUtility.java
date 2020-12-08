package Common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.ZonedDateTime;
import java.util.List;

public interface RemoteUtility extends Remote {
    String echo(String msg) throws RemoteException;
    ZonedDateTime getServerTime() throws RemoteException;
    List<Double> getSquare(List<Double> list) throws RemoteException;
    List<Double> getRoots(List<Double> list) throws RemoteException;
}
