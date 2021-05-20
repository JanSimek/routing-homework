package xyz.simek.routefinder.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import xyz.simek.routefinder.dto.Route;
import xyz.simek.routefinder.exception.CountryNotFoundException;
import xyz.simek.routefinder.exception.DataServiceException;
import xyz.simek.routefinder.exception.RouteNotFoundException;
import xyz.simek.routefinder.service.RouteService;

@RestController
public class RoutingController {

    private final RouteService routeService;

    public RoutingController(RouteService routeService) {
        this.routeService = routeService;
    }

    @Operation(summary = "Calculate any possible land route from one country to another", responses = {
        @ApiResponse(responseCode = "200", description = "Route found"),
        @ApiResponse(responseCode = "400", description = "Route impossible or country not found", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "500", description = "Data service failure", content = @Content(schema = @Schema(hidden = true))) })
    @GetMapping(value = "/routing/{origin}/{destination}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Route routing(@PathVariable String origin, @PathVariable String destination) {

        try {
            return routeService.findRoute(origin, destination);
        } catch (RouteNotFoundException | CountryNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        } catch (DataServiceException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }
}
