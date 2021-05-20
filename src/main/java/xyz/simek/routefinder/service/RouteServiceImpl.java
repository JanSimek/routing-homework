package xyz.simek.routefinder.service;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.BFSShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedPseudograph;
import org.springframework.stereotype.Service;
import xyz.simek.routefinder.dto.Country;
import xyz.simek.routefinder.dto.Route;
import xyz.simek.routefinder.exception.CountryNotFoundException;
import xyz.simek.routefinder.exception.RouteNotFoundException;

import java.util.Set;
import java.util.function.Function;

@Service
public class RouteServiceImpl implements RouteService {

    private final CountryService countryService;

    public RouteServiceImpl(CountryService countryService) {
        this.countryService = countryService;
    }

    @Override
    public Route findRoute(String origin, String destination) {

        var countries = countryService.getCountries();

        validateRoute(origin, destination, countries);

        var graph = buildGraph(countries);

        GraphPath<String, DefaultEdge> path = BFSShortestPath.findPathBetween(graph, origin, destination);
        if (path == null) {
            throw new RouteNotFoundException(String.format("Route from %s to %s not found", origin, destination));
        }

        return new Route(path.getVertexList());
    }

    private void validateRoute(String origin, String destination, Set<Country> countries) {

        Function<String, Country> findCountryByName = (String countryCode) -> countries.stream()
                .filter(country -> country.getName().equals(countryCode))
                .findFirst()
                .orElseThrow(() -> new CountryNotFoundException(countryCode));

        var originCountry = findCountryByName.apply(origin);
        var destinationCountry = findCountryByName.apply(destination);

        if (!originCountry.equals(destinationCountry)) {
            if (originCountry.isIsolated()) {
                throw new RouteNotFoundException(String.format("Country %s is isolated", originCountry));
            }
            if (destinationCountry.isIsolated()) {
                throw new RouteNotFoundException(String.format("Country %s is isolated", destinationCountry));
            }
        }
    }

    private DirectedPseudograph<String, DefaultEdge> buildGraph(Set<Country> countries) {

        DirectedPseudograph<String, DefaultEdge> graph = new DirectedPseudograph<>(DefaultEdge.class);

        countries.forEach(c -> {
            graph.addVertex(c.getName());
            c.getBorders().forEach(b -> {
                graph.addVertex(b);
                graph.addEdge(c.getName(), b);
            });
        });

        return graph;
    }
}
