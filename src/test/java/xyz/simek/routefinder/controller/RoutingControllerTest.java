package xyz.simek.routefinder.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.servlet.MockMvc;
import xyz.simek.routefinder.RoutingTest;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RoutingControllerTest extends RoutingTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetRoutingSingleOK() throws Exception {
        this.mockMvc.perform(get("/routing/CZE/CZE")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"route\":[\"CZE\"]}"));
    }

    @Test
    void testGetRoutingMultipleOK() throws Exception {
        this.mockMvc.perform(get("/routing/SVK/DEU")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"route\":[\"SVK\",\"AUT\",\"DEU\"]}"));
    }

    @Test
    void testGetRoutingInvalidCountry400() throws Exception {
        this.mockMvc.perform(get("/routing/XYZ/CZE")).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString("Country XYZ not found")));
    }

    @Test
    void testGetRoutingInvalidRoute400() throws Exception {
        this.mockMvc.perform(get("/routing/ISL/CZE")).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(containsString("Country ISL is isolated")));
    }

    @Test
    void testGetRoutingServiceFail500() throws Exception {

        server.reset();

        server.expect(ExpectedCount.once(), requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON));

        this.mockMvc.perform(get("/routing/ISL/CZE")).andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(status().reason(containsString("Could not get a list of countries")));
    }
}
