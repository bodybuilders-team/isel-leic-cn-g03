package server;

import contract.ICallback;
import contract.IPrimesService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

/**
 * Server RMI for the primes service.
 */
public class ServerRMI implements IPrimesService {
    static String serverIP = null;
    static int serverPort = 7001;
    static int registerPort = 7000;
    static ServerRMI svc = null;

    /**
     * Entry point for the server.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        try {
            serverIP = args[0];

            Properties props = System.getProperties();
            props.put("java.rmi.server.hostname", serverIP);

            svc = new ServerRMI();
            IPrimesService stubSvc = (IPrimesService) UnicastRemoteObject.exportObject(svc, serverPort);
            Registry registry = LocateRegistry.createRegistry(registerPort);

            registry.rebind("RemServer", stubSvc);

            System.out.println("Server ready: Press any key to finish server");
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            scanner.nextLine();
            System.exit(0);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.err.println("Server unhandled exception: " + ex);
            ex.printStackTrace();
        }
    }

    /**
     * Checks if a number is prime.
     *
     * @param num the number to check
     * @return true if the number is prime, false otherwise
     */
    static boolean isPrime(int num) {
        if (num <= 1)
            return false;
        if (num == 2 || num == 3)
            return true;
        if (num % 2 == 0)
            return false;
        for (int i = 3; i <= Math.sqrt(num); i += 2) {
            if (num % i == 0)
                return false;
        }

        return true;
    }

    /**
     * Finds all primes between startNumber and endNumber.
     *
     * @param startNumber the start number
     * @param endNumber   the end number
     * @param iCallback   the callback to use to return the primes
     * @throws RemoteException if an error occurs
     */
    @Override
    public void findPrimes(int startNumber, int endNumber, ICallback iCallback) throws RemoteException {
        new Thread(() -> {
            for (int i = startNumber; i <= endNumber; i++) {
                if (isPrime(i)) {
                    try {
                        iCallback.nextPrime(i);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }
}
