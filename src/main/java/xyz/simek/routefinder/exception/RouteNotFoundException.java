package xyz.simek.routefinder.exception;

public class RouteNotFoundException extends RuntimeException {
    public RouteNotFoundException(String reason) {
        super(reason);
    }
}
