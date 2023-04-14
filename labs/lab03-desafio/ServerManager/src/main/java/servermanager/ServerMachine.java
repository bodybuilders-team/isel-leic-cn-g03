package servermanager;

import io.grpc.stub.StreamObserver;
import machinesmanager.Information;
import machinesmanager.MachinesManagerContractGrpc;

public class ServerMachine extends MachinesManagerContractGrpc.MachinesManagerContractImplBase {

    private final ManagedMachines managedMachines;

    public ServerMachine(ManagedMachines managedMachines) {
        this.managedMachines = managedMachines;
    }

    @Override
    public ServerStreamObserver connectToManager(
            StreamObserver<Information> responseObserver) {
        int machineId = managedMachines.addMachine(responseObserver);
        return new ServerStreamObserver(machineId, responseObserver);
    }
}
