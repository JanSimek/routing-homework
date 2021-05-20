package xyz.simek.routefinder.service;

import xyz.simek.routefinder.dto.Route;

public interface RouteService {

    /**
     * Finds the shortest possible route from one country to another
     *
     * @param origin      country
     * @param destination country
     * @return route with a list of border crossings to get from origin to destination
     */
    Route findRoute(String origin, String destination);
}
