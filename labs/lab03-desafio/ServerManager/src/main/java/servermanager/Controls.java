package servermanager;

import machinesmanager.Control;

public enum Controls {
    STOP, RESTART, SENDCONFIG;

    public Control getControl() {
        switch (this) {
            case STOP:
                return stopControl;
            case RESTART:
                return restartControl;
            case SENDCONFIG:
                return sendConfigControl;
            default:
                throw new IllegalArgumentException();
        }
    }

    private static Control stopControl = Control.newBuilder()
            .setCtlNumber(0)
            .setCtltext("stop")
            .build();

    private static Control restartControl = Control.newBuilder()
            .setCtlNumber(1)
            .setCtltext("restart")
            .build();

    private static Control sendConfigControl = Control.newBuilder()
            .setCtlNumber(2)
            .setCtltext("sendConfig")
            .build();
}
