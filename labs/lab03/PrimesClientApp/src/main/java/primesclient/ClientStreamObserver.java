package primesclient;

import io.grpc.stub.StreamObserver;
import primesservice.Prime;

import java.util.ArrayList;
import java.util.List;

public class ClientStreamObserver implements StreamObserver<Prime> {
    List<Prime> primes = new ArrayList<Prime>();
    private boolean isCompleted = false;
    private boolean success = false;

    public boolean OnSuccesss() {return success;}

    public boolean isCompleted() {return isCompleted;}

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