package servermanager;

import io.grpc.stub.StreamObserver;
import machinesmanager.Control;
import machinesmanager.Information;
import machinesmanager.MachineID;
import supervisormanager.Command;

import java.util.HashMap;
import java.util.Set;

public class ManagedMachines {

    private final HashMap<Integer, StreamObserver<Information>> machines = new HashMap<>();

    public void addMachine(StreamObserver<Information> streamObserver) {
        machines.put(machines.size(), streamObserver);
    }

    public Set<Integer> getMachineIds() {
        return machines.keySet();
    }

    public void sendCommandToMachine(Command command) {
        int id = command.getId().getID();

        machines.get(id).onNext(Information.newBuilder()
                        .setMID(MachineID.newBuilder().setID(id).build())
                        .setCtl(Control.newBuilder()
                                .setCtlNumber(command.getCtlNumber())
                                .setCtltext(command.getCtltext())
                                .build())
                .build());
    }
}
