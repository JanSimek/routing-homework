package xyz.simek.routefinder.exception;

public class DataServiceException extends RuntimeException {

    public DataServiceException(String reason) {
        super(reason);
    }

    public DataServiceException(String reason, Throwable exception) {
        super(reason, exception);
    }
}
