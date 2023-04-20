package client;

import contract.ICallback;
import contract.IPrimesService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Client RMI for the primes service.
 */
public class ClientRMI {
    static String serverIP = "localhost";
    static int registerPort = 7000;
    static ICallback callback = null;
    static int interval = 1000;

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(serverIP, registerPort);
            IPrimesService svc = (IPrimesService) registry.lookup("RemServer");

            callback = prime -> System.out.println("Prime: " + prime);
            ICallback stubCallback = (ICallback) UnicastRemoteObject.exportObject(callback, 0);
            // The 0 port number means that the system will choose a free port

            // Get start and end numbers from stdin
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            System.out.print("Start number: ");
            int startNumber = scanner.nextInt();
            System.out.print("End number: ");
            int endNumber = scanner.nextInt();

            for (int start = startNumber; start <= endNumber; start += interval + 1) {
                int end = start + interval;
                if (end > endNumber)
                    end = endNumber;

                try {
                    svc.findPrimes(start, end, stubCallback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            svc.findPrimes(startNumber, endNumber, stubCallback);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.err.println("Client unhandled exception: " + ex.toString());
            ex.printStackTrace();
        }
    }
}
