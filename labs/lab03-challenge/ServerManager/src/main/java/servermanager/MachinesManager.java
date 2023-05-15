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

/**
 * Manages the machines that are connected to the server.
 */
public class MachinesManager {

    private final Map<Integer, StreamObserver<Information>> machines = new HashMap<>();

    /**
     * Adds a machine to the list of machines.
     *
     * @param streamObserver The stream observer of the machine.
     * @return The ID of the machine.
     */
    public int addMachine(StreamObserver<Information> streamObserver) {
        int machineId = machines.size();
        machines.put(machineId, streamObserver);
        return machineId;
    }

    /**
     * Gets the IDs of the machines.
     *
     * @return The IDs of the machines.
     */
    public Set<Integer> getMachineIds() {
        return machines.keySet();
    }

    /**
     * Sends a command to a machine.
     *
     * @param command The command to send.
     */
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

    /**
     * Sends a configuration to a machine.
     *
     * @param machineId   The ID of the machine.
     * @param configPairs The configuration to send.
     */
    public void sendConfigToMachine(int machineId, Map<Integer, String> configPairs) {
        machines.get(machineId).onNext(Information.newBuilder()
                .setMID(MachineID.newBuilder().setID(machineId).build())
                .setConf(Config.newBuilder()
                        .putAllConfigPairs(configPairs)
                        .build())
                .build());
    }
}
