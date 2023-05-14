package contract;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The callback interface.
 */
public interface ICallback extends Remote {

    /**
     * Called when a prime is found.
     *
     * @param prime the prime number
     * @throws RemoteException if an error occurs
     */
    void nextPrime(int prime) throws RemoteException;
}
