package org.mindswap.springtheknife.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.mindswap.springtheknife.dto.userexperience.UserExperienceCreateDto;
import org.mindswap.springtheknife.dto.userexperience.UserExperienceGetDto;
import org.mindswap.springtheknife.dto.userexperience.UserExperiencePatchDto;
import org.mindswap.springtheknife.exceptions.booking.BookingNotFoundException;
import org.mindswap.springtheknife.exceptions.restaurant.RestaurantNotFoundException;
import org.mindswap.springtheknife.exceptions.userexperience.UserExperienceNotFoundException;
import org.mindswap.springtheknife.exceptions.user.UserNotFoundException;
import org.mindswap.springtheknife.service.userexperience.UserExperienceServiceImpl;
import org.mindswap.springtheknife.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/userexperiences")
public class UserExperienceController {

    private final UserExperienceServiceImpl userExperienceService;

    @Autowired
    public UserExperienceController(UserExperienceServiceImpl userExperienceService) {
        this.userExperienceService = userExperienceService;
    }

    @Operation(summary = "Get all users experiences", description = "Returns a list of all users experiences")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of user experiences",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserExperienceGetDto.class)))})
    })
    @GetMapping("/")
    public ResponseEntity<List<UserExperienceGetDto>> getAllUsersExperiences(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam(value = "sortBy") String sortBy
    ) {
        return new ResponseEntity<>(userExperienceService.getAllUsersExperiences(pageNumber, pageSize, sortBy), HttpStatus.OK);
    }

    @Operation(summary = "Get user experience by ID", description = "Returns a user experience by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the user experience",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserExperienceGetDto.class))}),
            @ApiResponse(responseCode = "404", description = "User experience not found",
                    content = @Content)
    })
    @GetMapping("/{userExperienceId}")
    public ResponseEntity<UserExperienceGetDto> getUserExperienceById(@PathVariable("userExperienceId") Long id) throws UserExperienceNotFoundException {
        UserExperienceGetDto userExperience = userExperienceService.getUserExperienceById(id);
        return new ResponseEntity<>(userExperience, HttpStatus.OK);
    }

    @Operation(summary = "Add a new user experience", description = "Creates a new user experience")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User experience successfully created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserExperienceGetDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid user experience details provided",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User or restaurant not found",
                    content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<UserExperienceGetDto> addUserExperience(@Valid @RequestBody UserExperienceCreateDto userExperienceCreateDto) throws UserNotFoundException, RestaurantNotFoundException, UserExperienceNotFoundException, BookingNotFoundException {
        return new ResponseEntity<>(userExperienceService.addNewUserExperience(userExperienceCreateDto),
                HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a user experience", description = "Deletes a user experience by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User experience successfully deleted"),
            @ApiResponse(responseCode = "404", description = "User experience not found",
                    content = @Content)
    })
    @DeleteMapping("/{userExperienceId}")
    public ResponseEntity<String> deleteUserExperience(@PathVariable("userExperienceId") Long userExperienceId)
            throws UserExperienceNotFoundException {
        userExperienceService.deleteUserExperience(userExperienceId);
        return new ResponseEntity<>(Message.USER_EXPERIENCE_ID_DELETED + userExperienceId, HttpStatus.OK);
    }

    @Operation(summary = "Update a user experience", description = "Updates a user experience by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User experience successfully updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserExperiencePatchDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid user experience details provided",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User experience not found",
                    content = @Content)
    })
    @PatchMapping(path = "/{userExperienceId}")
    public ResponseEntity<UserExperiencePatchDto> patchUserExperience
            (@PathVariable("userExperienceId") Long userExperienceId,
             @Valid @RequestBody UserExperiencePatchDto userExperience) throws UserExperienceNotFoundException {
        userExperienceService.updateUserExperience(userExperienceId, userExperience);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

