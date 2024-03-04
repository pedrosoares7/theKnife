package org.mindswap.springtheknife.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.mindswap.springtheknife.dto.restaurantTypeDto.RestaurantTypeDto;
import org.mindswap.springtheknife.exceptions.restaurantType.RestaurantTypeAlreadyExistsException;
import org.mindswap.springtheknife.exceptions.restaurantType.RestaurantTypeNotFoundException;
import org.mindswap.springtheknife.service.restauranttype.RestaurantTypeService;
import org.mindswap.springtheknife.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "api/v1/restaurantTypes")
public class RestaurantTypeController {
    RestaurantTypeService restaurantTypeService;

    @Autowired
    public RestaurantTypeController(RestaurantTypeService restaurantTypeService) {
        this.restaurantTypeService = restaurantTypeService;
    }

    @Operation(summary = "Get all RestaurantType", description = "Returns a list of all restaurant types")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of restaurant types",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RestaurantTypeDto.class)))})
    })
    @GetMapping("/")
    public ResponseEntity<List<RestaurantTypeDto>> getAllRestaurantTypes(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam(value = "sortBy") String sortBy
    ) {
        return new ResponseEntity<>(restaurantTypeService.getAllRestaurantType(pageNumber, pageSize, sortBy), HttpStatus.OK);
    }

    @Operation(summary = "Get a Restaurant Type by ID", description = "Returns a restaurant type by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the restaurant type",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestaurantTypeDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurant type not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantTypeDto> getRestaurantTypeById(@PathVariable("id") Long id) throws RestaurantTypeNotFoundException {
        return new ResponseEntity<>(restaurantTypeService.getRestaurantTypeById(id), HttpStatus.OK);
    }

    @Operation(summary = "Create a new Restaurant Type", description = "Creates a new restaurant type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurant type successfully created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestaurantTypeDto.class))}),
            @ApiResponse(responseCode = "400", description = "Verify the restaurant type details",
                    content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<RestaurantTypeDto> addRestaurantType(@Valid @RequestBody RestaurantTypeDto restaurantType) throws RestaurantTypeAlreadyExistsException {
        return new ResponseEntity<>(restaurantTypeService.addRestaurantType(restaurantType), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a Restaurant Type", description = "Updates a restaurant type in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant type successfully updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestaurantTypeDto.class))}),
            @ApiResponse(responseCode = "400", description = "Restaurant type property already exists",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurant type not found",
                    content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<RestaurantTypeDto> patchRestaurantType(@PathVariable("id") Long id, @Valid @RequestBody RestaurantTypeDto restaurantType) throws RestaurantTypeNotFoundException {
        return new ResponseEntity<>(restaurantTypeService.patchRestaurantType(id, restaurantType), HttpStatus.OK);
    }

    @Operation(summary = "Delete a Restaurant Type", description = "Deletes a restaurant type from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant type successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Restaurant type ID not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRestaurantType(@PathVariable("id") Long id) throws RestaurantTypeNotFoundException {
        restaurantTypeService.deleteRestaurantType(id);
        return new ResponseEntity<>(Message.TYPE_ID + id + Message.DELETE_SUCCESSFULLY, HttpStatus.OK);
    }
}
