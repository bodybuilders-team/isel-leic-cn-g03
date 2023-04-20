package servermanager;

import io.grpc.stub.StreamObserver;
import machinesmanager.Information;
import machinesmanager.MachineID;

public class ServerStreamObserver implements StreamObserver<Information> {
    private static final double MAX_TEMP = 100.0;
    private final int machineId;
    private final StreamObserver<Information> machineStreamObserver;
    private boolean isCompleted = false;
    private boolean success = false;

    public ServerStreamObserver(int machineId, StreamObserver<Information> machineStreamObserver) {
        this.machineId = machineId;
        this.machineStreamObserver = machineStreamObserver;
    }

    public boolean OnSuccess() {
        return success;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    @Override
    public void onNext(Information information) {
        if (information.getMsgOptionsCase() == Information.MsgOptionsCase.CONF) {
            System.out.print("Received configuration from " + machineId + ": { ");
            information.getConf().getConfigPairsMap().forEach((key, value) -> {
                System.out.print(key + " -> " + value + "; ");
            });
            System.out.println("}");
        } else if (information.getMsgOptionsCase() == Information.MsgOptionsCase.TEMPERATURE) {
            System.out.println("Received temperature from " + machineId + ": " + information.getTemperature());
            if (information.getTemperature() > MAX_TEMP) {
                System.out.println("Temperature of machine " + machineId + " exceeded MAX_TEMP (" + MAX_TEMP + "ºC). Stopping machine.");
                machineStreamObserver.onNext(Information.newBuilder()
                        .setMID(MachineID.newBuilder().setID(machineId).build())
                        .setCtl(Controls.STOP.getControl())
                        .build()
                );
            }
        }
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("Error on call:" + throwable.getMessage());
        isCompleted = true;
        success = false;
    }

    @Override
    public void onCompleted() {
        System.out.println("Stream completed");
        isCompleted = true;
        success = true;
    }
}
