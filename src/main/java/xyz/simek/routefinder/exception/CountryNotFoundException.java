package xyz.simek.routefinder.exception;

public class CountryNotFoundException extends RuntimeException {
    public CountryNotFoundException(String countryCode) {
        super(String.format("Country %s not found", countryCode));
    }
}
