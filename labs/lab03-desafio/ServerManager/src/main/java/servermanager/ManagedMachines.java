package servermanager;

import io.grpc.stub.StreamObserver;
import machinesmanager.Config;
import machinesmanager.Control;
import machinesmanager.Information;
import machinesmanager.MachineID;
import supervisormanager.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ManagedMachines {

    private final Map<Integer, StreamObserver<Information>> machines = new HashMap<>();

    public int addMachine(StreamObserver<Information> streamObserver) {
        int machineId = machines.size();
        machines.put(machineId, streamObserver);
        return machineId;
    }

    public Set<Integer> getMachineIds() {
        return machines.keySet();
    }

    public void sendCommandToMachine(Command command) {
        int machineId = command.getId().getID();

        machines.get(machineId).onNext(Information.newBuilder()
                .setMID(MachineID.newBuilder().setID(machineId).build())
                .setCtl(Control.newBuilder()
                        .setCtlNumber(command.getCtlNumber())
                        .setCtltext(command.getCtltext())
                        .build())
                .build());
    }

    public void sendConfigToMachine(int machineId, Map<Integer, String> configPairs) {
        machines.get(machineId).onNext(Information.newBuilder()
                .setMID(MachineID.newBuilder().setID(machineId).build())
                .setConf(Config.newBuilder()
                        .putAllConfigPairs(configPairs)
                        .build())
                .build());
    }
}
