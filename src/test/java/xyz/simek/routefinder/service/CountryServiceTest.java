package xyz.simek.routefinder.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import xyz.simek.routefinder.RoutingTest;
import xyz.simek.routefinder.dto.Country;
import xyz.simek.routefinder.exception.DataServiceException;

import java.io.IOException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest
public class CountryServiceTest extends RoutingTest {

    @Autowired
    private CountryService countryService;

    @Test
    void testGetCountries() throws IOException {

        var countries = countryService.getCountries();

        server.verify();

        assertEquals(5, countries.size());

        var names = countries.stream().map(Country::getName).collect(Collectors.toList());
        assertTrue(names.contains("CZE"));
        assertTrue(names.contains("SVK"));
        assertTrue(names.contains("AUT"));
        assertTrue(names.contains("DEU"));
        assertTrue(names.contains("ISL"));

        var borders = countries.stream()
                .filter(c -> c.getName().equals("CZE")).findFirst().get()
                .getBorders();

        assertEquals(4, borders.size());
        assertTrue(borders.contains("AUT"));
        assertTrue(borders.contains("DEU"));
        assertTrue(borders.contains("PLN"));
        assertTrue(borders.contains("SVK"));
    }

    @Test
    void testCountriesFail() {

        server.reset();

        server.expect(ExpectedCount.once(), requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON));

        DataServiceException ex = assertThrows(DataServiceException.class, () -> {
            countryService.getCountries();
        });

        assertEquals("Could not get a list of countries", ex.getMessage());
    }

    @Test
    void testCountriesEmpty() {

        server.reset();

        server.expect(ExpectedCount.once(), requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON).body(""));

        DataServiceException ex = assertThrows(DataServiceException.class, () -> {
            countryService.getCountries();
        });

        assertEquals("Received empty list of countries", ex.getMessage());
    }
}
