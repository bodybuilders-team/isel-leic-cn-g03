package servermanager;

import io.grpc.stub.StreamObserver;
import supervisormanager.AllMachineIDs;
import supervisormanager.Command;
import supervisormanager.MachID;
import supervisormanager.SupervisorManagerGrpc;
import supervisormanager.Void;

import java.util.stream.Collectors;

/**
 * The supervisor server.
 */
public class ServerSupervisor extends SupervisorManagerGrpc.SupervisorManagerImplBase {

    private final MachinesManager machinesManager;

    public ServerSupervisor(MachinesManager machinesManager) {
        this.machinesManager = machinesManager;
    }

    @Override
    public void getMachinesIds(Void request,
                               StreamObserver<AllMachineIDs> responseObserver) {
        responseObserver.onNext(AllMachineIDs.newBuilder()
                .addAllIDs(
                        machinesManager.getMachineIds().stream().map(id ->
                                MachID.newBuilder().setID(id).build()
                        ).collect(Collectors.toList()))
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void sendCommandToMachine(Command request,
                                     StreamObserver<Void> responseObserver) {
        machinesManager.sendCommandToMachine(request);
        responseObserver.onNext(Void.newBuilder().build());
        responseObserver.onCompleted();
    }
}
