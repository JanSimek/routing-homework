package xyz.simek.routefinder.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.simek.routefinder.RoutingTest;
import xyz.simek.routefinder.exception.CountryNotFoundException;
import xyz.simek.routefinder.exception.RouteNotFoundException;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class RouteServiceTest extends RoutingTest {

    @Autowired
    private RouteService routeService;

    @Test
    void testOriginCountryNotFound() {

        var ex1 = assertThrows(CountryNotFoundException.class, () -> {
            routeService.findRoute("XXX", "SVK");
        });
        assertEquals("Country XXX not found", ex1.getMessage());
    }

    @Test
    void testDestinationCountryNotFound() {
        var ex2 = assertThrows(CountryNotFoundException.class, () -> {
            routeService.findRoute("CZE", "AAA");
        });
        assertEquals("Country AAA not found", ex2.getMessage());
    }

    @Test
    void testRouteNotFound() {

        var ex = assertThrows(RouteNotFoundException.class, () -> {
            routeService.findRoute("ISL", "SVK");
        });

        assertEquals("Country ISL is isolated", ex.getMessage());
    }

    @Test
    void testRouteFound() {

        var route = routeService.findRoute("DEU", "SVK");

        assertEquals(Arrays.asList("DEU", "AUT", "SVK"), route.getRoute());
    }

    @Test
    void testSameCountry() {

        var route = routeService.findRoute("CZE", "CZE");

        assertEquals(Collections.singletonList("CZE"), route.getRoute());
    }
}
