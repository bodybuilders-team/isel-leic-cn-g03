package servermanager;

import io.grpc.stub.StreamObserver;
import supervisormanager.Void;
import supervisormanager.*;

import java.util.stream.Collectors;

public class ServerSupervisor extends SupervisorManagerGrpc.SupervisorManagerImplBase {

    private final ManagedMachines managedMachines;

    public ServerSupervisor(ManagedMachines managedMachines) {
        this.managedMachines = managedMachines;
    }

    @Override
    public void getMachinesIds(Void request,
                               StreamObserver<AllMachineIDs> responseObserver) {
        responseObserver.onNext(AllMachineIDs.newBuilder()
                .addAllIDs(
                        managedMachines.getMachineIds().stream().map(id ->
                                MachID.newBuilder().setID(id).build()
                        ).collect(Collectors.toList()))
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void sendCommandToMachine(Command request,
                                     StreamObserver<Void> responseObserver) {
        managedMachines.sendCommandToMachine(request);
        responseObserver.onNext(Void.newBuilder().build());
        responseObserver.onCompleted();
    }
}
