package pt.isel.cn.landmarks.server.service.exceptions;

public class RequestNotFoundException extends Exception {
    public RequestNotFoundException(String message) {
        super(message);
    }
}
