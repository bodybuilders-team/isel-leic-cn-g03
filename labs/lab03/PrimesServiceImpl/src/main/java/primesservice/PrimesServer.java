package primesservice;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Scanner;

/**
 * Server for the primes service using gRPC.
 */
public class PrimesServer extends PrimesServiceGrpc.PrimesServiceImplBase {

    private static final int svcPort = 8000;

    /**
     * Entry point for the server.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Server svc = ServerBuilder.forPort(svcPort)
                    .addService(new PrimesServer())
                    .build()
                    .start();
            System.out.println("PrimesServer started on port " + svcPort);

            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            svc.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
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
     * Checks if the server is alive.
     *
     * @param request          the request
     * @param responseObserver the response observer
     */
    @Override
    public void isAlive(Void request, StreamObserver<Text> responseObserver) {
        Text response = Text.newBuilder().setMsg("I'm alive!").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    /**
     * Finds all primes between the start and end numbers.
     *
     * @param request          the request
     * @param responseObserver the response observer
     */
    @Override
    public void findPrimes(PrimesInterval request, StreamObserver<Prime> responseObserver) {
        int start = request.getStartNum();
        int end = request.getEndNum();
        for (int i = start; i <= end; i++) {
            if (isPrime(i)) {
                Prime prime = Prime.newBuilder().setPrime(i).build();
                responseObserver.onNext(prime);
            }
        }
        responseObserver.onCompleted();
    }
}
