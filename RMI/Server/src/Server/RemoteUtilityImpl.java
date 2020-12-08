package Server;

import Common.RemoteUtility;

import java.rmi.RemoteException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class RemoteUtilityImpl implements RemoteUtility {
    public RemoteUtilityImpl() {}

    @Override
    public String echo(String s) throws RemoteException {
        System.out.println(s);
        return s;
    }

    @Override
    public ZonedDateTime getServerTime() throws RemoteException {
        return ZonedDateTime.now();
    }

    @Override
    public List<Double> getSquare(List<Double> list) throws RemoteException {
        printList(list);

        return list.stream()
                .map((number) -> (number*number))
                .collect(Collectors.toList());
    }

    @Override
    public List<Double> getRoots(List<Double> list) throws RemoteException {
        printList(list);

        return list.stream()
                .map(Math::sqrt)
                .collect(Collectors.toList());
    }

    private void printList(List<Double> list) {
        System.out.println("\nServer receive: ");
        list.forEach((element) -> System.out.print(String.valueOf(element) + ' '));
    }
}
