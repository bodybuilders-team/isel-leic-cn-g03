package servermanager;

import machinesmanager.Control;

public enum Controls {
    STOP, RESTART, SENDCONFIG;

    private static final Control stopControl = Control.newBuilder()
            .setCtlNumber(0)
            .setCtltext("stop")
            .build();
    private static final Control restartControl = Control.newBuilder()
            .setCtlNumber(1)
            .setCtltext("restart")
            .build();
    private static final Control sendConfigControl = Control.newBuilder()
            .setCtlNumber(2)
            .setCtltext("sendConfig")
            .build();

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
}
