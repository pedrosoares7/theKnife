package org.mindswap.springtheknife.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.mindswap.springtheknife.dto.restaurant.RestaurantGetDto;
import org.mindswap.springtheknife.dto.restaurant.RestaurantPatchDto;
import org.mindswap.springtheknife.dto.restaurant.RestaurantPostDto;
import org.mindswap.springtheknife.exceptions.city.CityNotFoundException;
import org.mindswap.springtheknife.exceptions.restaurant.RestaurantAlreadyExistsException;
import org.mindswap.springtheknife.exceptions.restaurant.RestaurantNotFoundException;
import org.mindswap.springtheknife.service.restaurant.RestaurantServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/restaurants")
public class RestaurantController {

    private final RestaurantServiceImpl restaurantServiceImpl;

    @Autowired
    public RestaurantController(RestaurantServiceImpl restaurantServiceImpl) {
        this.restaurantServiceImpl = restaurantServiceImpl;

    }

    @Operation(summary = "Get all restaurants", description = "Returns a list of all restaurants")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of restaurants",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RestaurantGetDto.class))))
    })
    @GetMapping("/")
    public ResponseEntity<List<RestaurantGetDto>> getAllRestaurants(
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", required = false) String sortBy
    ) {
        return new ResponseEntity<>(restaurantServiceImpl.getAllRestaurants(pageNumber, pageSize, sortBy), HttpStatus.OK);
    }

    @Operation(summary = "Get a restaurant by ID", description = "Returns a restaurant by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the restaurant",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestaurantGetDto.class))),
            @ApiResponse(responseCode = "404", description = "Restaurant not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantGetDto> getRestaurantById(@PathVariable("id") Long id) throws RestaurantNotFoundException {
        return new ResponseEntity<>(restaurantServiceImpl.getRestaurant(id), HttpStatus.OK);
    }

    @Operation(summary = "Add a new restaurant", description = "Creates a new restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurant successfully added",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestaurantGetDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid restaurant details provided",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "City not found",
                    content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<RestaurantGetDto> addRestaurant(@Valid @RequestBody RestaurantPostDto restaurant) throws RestaurantAlreadyExistsException, CityNotFoundException, IOException, RestaurantNotFoundException {
        return new ResponseEntity<>(restaurantServiceImpl.addRestaurant(restaurant), HttpStatus.CREATED);
    }

    @Operation(summary = "Add a list of restaurants", description = "Creates a list of new restaurants")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of restaurants successfully added",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RestaurantGetDto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid restaurant details provided",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "City not found",
                    content = @Content)
    })
    @PostMapping("/list/")
    public ResponseEntity<List<RestaurantGetDto>> addRestaurantList(@Valid @RequestBody List<RestaurantPostDto> restaurantList) throws RestaurantAlreadyExistsException, CityNotFoundException, IOException {
        return new ResponseEntity<>(restaurantServiceImpl.addListOfRestaurants(restaurantList), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a restaurant", description = "Updates a restaurant by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestaurantGetDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid restaurant details provided",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurant not found",
                    content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<RestaurantGetDto> patchRestaurant(@PathVariable("id") Long id, @Valid @RequestBody RestaurantPatchDto restaurant) throws RestaurantNotFoundException {
        return new ResponseEntity<>(restaurantServiceImpl.patchRestaurant(id, restaurant), HttpStatus.OK);
    }

    @Operation(summary = "Delete a restaurant", description = "Deletes a restaurant by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurant not found",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable("id") Long id) throws RestaurantNotFoundException {
        restaurantServiceImpl.deleteRestaurant(id);
        return new ResponseEntity<>("Restaurant with id " + id + " deleted successfully.", HttpStatus.OK);
    }

    @Operation(summary = "Get average rating of a restaurant", description = "Returns the average rating of a restaurant by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the average rating",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Double.class))),
            @ApiResponse(responseCode = "404", description = "Restaurant not found",
                    content = @Content)
    })
    @GetMapping("/{id}/averageRating")
    public ResponseEntity<Double> getAverageRating(@PathVariable("id") Long id) {
        Double averageRating = restaurantServiceImpl.findAverageRating(id);
        return ResponseEntity.ok(averageRating);
    }

    @PostMapping("/generate")
    public ResponseEntity<RestaurantGetDto> addRestaurantWithImage(@Valid @RequestBody RestaurantPostDto restaurant) throws RestaurantAlreadyExistsException, CityNotFoundException, IOException {
        return new ResponseEntity<>(restaurantServiceImpl.addRestaurantWithImage(restaurant), HttpStatus.OK);
    }

    @PostMapping("/list/generate")
    public ResponseEntity<List<RestaurantGetDto>> addRestaurantListWithImage(@Valid @RequestBody List<RestaurantPostDto> restaurantList) throws RestaurantAlreadyExistsException, CityNotFoundException, IOException {
        return new ResponseEntity<>(restaurantServiceImpl.addListOfRestaurantsWithImage(restaurantList), HttpStatus.OK);
    }
}
