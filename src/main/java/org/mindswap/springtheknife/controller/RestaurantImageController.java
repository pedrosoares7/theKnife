package org.mindswap.springtheknife.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.mindswap.springtheknife.dto.restaurant.RestaurantGetDto;
import org.mindswap.springtheknife.service.restaurantimage.RestaurantImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping(path = "api/v1/restaurants/img")
public class RestaurantImageController {

    private final RestaurantImageService restaurantImageService;

    @Autowired
    public RestaurantImageController(RestaurantImageService restaurantImageService) {
        this.restaurantImageService = restaurantImageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> fileUpload(@RequestParam("file") MultipartFile file) {
        try {
            restaurantImageService.uploadFile(file);
            return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to upload file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Upload restaurant image", description = "Creates a new restaurant image in the db")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully saved the image",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RestaurantGetDto.class))))
    })
    @PostMapping("/upload/{id}")
    public ResponseEntity<String> fileUploadWithId(@RequestParam("file") MultipartFile file, @PathVariable("id") Long id) {
        try {
            restaurantImageService.uploadFileWithId(file, id);
            return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to upload file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
