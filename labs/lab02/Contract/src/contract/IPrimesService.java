package contract;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The primes service interface.
 */
public interface IPrimesService extends Remote {

    /**
     * Finds all primes between startNumber and endNumber.
     *
     * @param startNumber the start number
     * @param endNumber   the end number
     * @param callback    the callback to use to return the primes
     * @throws RemoteException if an error occurs
     */
    void findPrimes(
            int startNumber,
            int endNumber,
            ICallback callback
    ) throws RemoteException;
}
