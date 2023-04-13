package servermanager;

import io.grpc.ServerBuilder;

import java.util.Scanner;

public class ServerManager {
    private static final int svcPort = 8000;

    public static void main(String[] args) {
        try {
            ManagedMachines manMach = new ManagedMachines();
            io.grpc.Server svc = ServerBuilder
                    .forPort(svcPort)
                    .addService(new ServerMachine(manMach))
                    .addService(new ServerSupervisor(manMach))
                    .build()
                    .start();

            System.out.println("Server started, listening on " + svcPort);
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            svc.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
