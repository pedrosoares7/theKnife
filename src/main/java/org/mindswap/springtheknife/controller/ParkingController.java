package org.mindswap.springtheknife.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.mindswap.springtheknife.model.Parking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parking")
public class ParkingController {

    private final WebClient webClient;
    private ObjectMapper objectMapper;

    @Autowired
    public ParkingController(ObjectMapper objectMapper) {
        this.webClient = WebClient.create();
        this.objectMapper = objectMapper;
    }

    @Operation(summary = "Get Parking Data", description = "Fetches parking data from an external service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved parking data",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Parking.Record.class)))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/checkParking/{limit}")
    public Mono<List<Parking.Record>> getParkingData(@PathVariable("limit") Integer limit) {

        String url = "http://localhost:8081/parking/" + limit;

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .map(body -> {
                    try {
                        Parking parking = objectMapper.readValue(body, Parking.class);
                        return parking.getResult().getRecords();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
