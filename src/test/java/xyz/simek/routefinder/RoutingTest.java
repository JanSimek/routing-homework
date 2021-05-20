package xyz.simek.routefinder;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import xyz.simek.routefinder.dto.Country;

import java.io.IOException;
import java.nio.file.Files;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

public abstract class RoutingTest {

    protected static final Resource jsonFile = new ClassPathResource("countries.json");
    protected static Country[] countries;
    protected static String jsonCountries;

    @Autowired
    protected RestTemplate restTemplate;

    protected MockRestServiceServer server;

    @Value("${rest.endpoint.countries}")
    protected String url;

    @BeforeAll
    public static void init() throws IOException {
        jsonCountries = Files.readString(jsonFile.getFile().toPath());
        countries = new ObjectMapper().readValue(jsonCountries, Country[].class);
    }

    @BeforeEach
    public void initServer() {
        server = MockRestServiceServer.bindTo(restTemplate).build();

        server.expect(ExpectedCount.once(), requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(jsonCountries));
    }
}
