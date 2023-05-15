package servermanager;

import io.grpc.stub.StreamObserver;
import machinesmanager.Information;
import machinesmanager.MachinesManagerContractGrpc;

/**
 * Manages the machines that are connected to the server.
 */
public class ServerMachine extends MachinesManagerContractGrpc.MachinesManagerContractImplBase {

    private final MachinesManager machinesManager;

    public ServerMachine(MachinesManager machinesManager) {
        this.machinesManager = machinesManager;
    }

    @Override
    public ServerStreamObserver connectToManager(StreamObserver<Information> responseObserver) {
        int machineId = machinesManager.addMachine(responseObserver);
        return new ServerStreamObserver(machineId, responseObserver);
    }
}
