package xyz.simek.routefinder.service;

import xyz.simek.routefinder.dto.Country;

import java.util.Set;

public interface CountryService {

    /**
     * Calls a third party service to obtain a list of countries
     *
     * @return all countries in the world and their borders
     */
    Set<Country> getCountries();
}
