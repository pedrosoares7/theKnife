package org.mindswap.springtheknife.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.mindswap.springtheknife.dto.user.UserCreateDto;
import org.mindswap.springtheknife.dto.user.UserGetDto;
import org.mindswap.springtheknife.dto.user.UserPatchDto;
import org.mindswap.springtheknife.exceptions.user.UserAlreadyExistsException;
import org.mindswap.springtheknife.exceptions.user.UserEmailAlreadyExistsException;
import org.mindswap.springtheknife.exceptions.user.UserNotFoundException;
import org.mindswap.springtheknife.model.User;
import org.mindswap.springtheknife.service.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {


    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;

    }

    @Operation(summary = "Get all users", description = "Returns a list of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of users",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserGetDto.class)))})
    })
    @GetMapping("/")
    public ResponseEntity<List<UserGetDto>> getAllUsers(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam(value = "sortBy") String sortBy

    ) {
        return new ResponseEntity<>(userServiceImpl.getAllUsers(pageNumber, pageSize, sortBy), HttpStatus.OK);
    }


    @Operation(summary = "Get a user by ID", description = "Returns a user by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserGetDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserGetDto> getUserById(@PathVariable("userId") Long userId) throws UserNotFoundException {
        return new ResponseEntity<>(userServiceImpl.getUser(userId), HttpStatus.OK);

    }

    @Operation(summary = "Create a new user", description = "Creates a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserCreateDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid user details provided",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "User or email already exists",
                    content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<UserGetDto> addUser(@Valid @RequestBody UserCreateDto user) throws UserAlreadyExistsException, UserEmailAlreadyExistsException {
        return new ResponseEntity<>(userServiceImpl.createUser(user), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a user", description = "Updates a user by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserPatchDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid user details provided",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)
    })
    @PatchMapping("/{userId}")
    public ResponseEntity<UserPatchDto> patchUser(@Valid @PathVariable("userId") Long id,
                                                  @Valid @RequestBody UserPatchDto user) throws UserNotFoundException, UserAlreadyExistsException {
        userServiceImpl.updateUser(id, user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Delete a user", description = "Deletes a user by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully deleted"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)
    })
    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<User> deleteUser(@Valid @PathVariable("userId") Long id) throws UserNotFoundException {
        userServiceImpl.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}