package machinesclient;

import io.grpc.stub.StreamObserver;
import machinesmanager.Config;
import machinesmanager.Information;

public class ClientStreamObserver implements StreamObserver<Information> {
    private boolean isCompleted = false;
    private boolean success = false;

    public boolean OnSuccess() {
        return success;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    @Override
    public void onNext(Information information) {
        if (information.getMsgOptionsCase() == Information.MsgOptionsCase.CTL) {
            int ctlNumber = information.getCtl().getCtlNumber();
            String ctlText = information.getCtl().getCtltext();

            switch (ctlNumber) {
                case 0:
                    System.out.println("Received control message 0. Stopping client.");
                    ClientApp.running = false;
                    break;
                case 1:
                    System.out.println("Received control message 1. Restarting client.");
                    ClientApp.running = false;
                    ClientApp.restarting = true;
                    break;
                case 2:
                    System.out.println("Received control message 2. Sending configuration.");
                    ClientApp.serverStreamObserver.onNext(Information.newBuilder()
                            .setConf(Config.newBuilder().putAllConfigPairs(ClientApp.config))
                            .build()
                    );
                    break;
                default:
                    System.out.println("Received unknown control message. Ignoring.");
                    break;
            }

        } else if (information.getMsgOptionsCase() == Information.MsgOptionsCase.CONF) {
            System.out.print("Received configuration: { ");
            information.getConf().getConfigPairsMap().forEach((key, value) -> {
                System.out.print(key + " -> " + value + "; ");
            });
            System.out.println("}");
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
