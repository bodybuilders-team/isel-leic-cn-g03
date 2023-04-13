package supervisormanager;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Scanner;

public class SupervisorManager extends SupervisorManagerGrpc.SupervisorManagerImplBase {

    private static final int svcPort = 8000;

    public static void main(String[] args) {
        try {
            Server server = ServerBuilder.forPort(svcPort)
                    .addService(new SupervisorManager())
                    .build()
                    .start();

            System.out.println("Supervisor server started, listening on " + svcPort);
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            server.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void getMachinesIds(Void request,
//                               StreamObserver<AllMachineIDs> responseObserver) {
//        throw new UnsupportedOperationException("Not implemented yet.");
//    }
//
//    @Override
//    public void sendCommandToMachine(Command request,
//                                     StreamObserver<Void> responseObserver) {
//        throw new UnsupportedOperationException("Not implemented yet.");
//    }

//    @Override
//    public void isAlive(Void request, StreamObserver<Text> responseObserver) {
//        responseObserver.onNext(Text.newBuilder().setMsg("Alive").build());
//        responseObserver.onCompleted();
//    }
//
//    @Override
//    public void findPrimes(PrimesInterval request, StreamObserver<Prime> responseObserver) {
//        new Thread(() -> {
//            for (int j = request.getStartNum(); j <= request.getEndNum(); j++) {
//                if (isPrime(j)) {
//                    try {
//                        responseObserver.onNext(Prime.newBuilder().setPrime(j).build());
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//            responseObserver.onCompleted();
//        }).start();
//    }
}
