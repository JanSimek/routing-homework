package xyz.simek.routefinder.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import xyz.simek.routefinder.dto.Country;
import xyz.simek.routefinder.exception.DataServiceException;

import java.util.Set;

@Service
public class CountryServiceImpl implements CountryService {

    private final String endpointUrl;
    private final RestTemplate restTemplate;

    public CountryServiceImpl(@Value("${rest.endpoint.countries}") String endpointUrl, RestTemplate restTemplate) {
        this.endpointUrl = endpointUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public Set<Country> getCountries() {

        try {
            Country[] countries = restTemplate.getForObject(endpointUrl, Country[].class);

            if (countries == null) {
                throw new DataServiceException("Received empty list of countries");
            }

            return Set.of(countries);

        } catch (HttpStatusCodeException ex) {
            throw new DataServiceException("Could not get a list of countries", ex);
        }
    }
}
