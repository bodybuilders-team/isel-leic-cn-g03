package servermanager;

import io.grpc.ServerBuilder;

import java.util.Scanner;

/**
 * The server manager.
 */
public class ServerManager {

    private static final int SVC_PORT = 8000;

    /**
     * The entry point of the server manager.
     *
     * @param args The arguments of the server manager.
     */
    public static void main(String[] args) {
        try {
            MachinesManager machinesManager = new MachinesManager();
            io.grpc.Server svc = ServerBuilder
                    .forPort(SVC_PORT)
                    .addService(new ServerMachine(machinesManager))
                    .addService(new ServerSupervisor(machinesManager))
                    .build()
                    .start();

            System.out.println("Server started, listening on " + SVC_PORT);
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();
            svc.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
