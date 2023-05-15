package pt.isel.cn.primes_client;


import io.grpc.stub.StreamObserver;
import primesservice.Prime;

import java.util.ArrayList;
import java.util.List;

/**
 * Stream observer for the findPrimes client streaming call.
 */
public class ClientStreamObserver implements StreamObserver<Prime> {
    List<Prime> primes = new ArrayList<>();
    private boolean isCompleted = false;
    private boolean success = false;

    /**
     * Returns true if the call was successful.
     *
     * @return true if the call was successful
     */
    public boolean OnSuccesss() {return success;}

    /**
     * Returns true if the call was completed.
     *
     * @return true if the call was completed
     */
    public boolean isCompleted() {return isCompleted;}

    /**
     * Returns the list of primes.
     *
     * @return the list of primes
     */
    public List<Prime> getPrimes() {return primes;}

    @Override
    public void onNext(Prime prime) {
        System.out.println("Received prime:" + prime.getPrime());
        primes.add(prime);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("Error on call:" + throwable.getMessage());
        isCompleted = true;
        success = false;
    }

    @Override
    public void onCompleted() {
        System.out.println("Stream completed");
        isCompleted = true;
        success = true;
    }
}
