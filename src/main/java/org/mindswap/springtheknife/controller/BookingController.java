package org.mindswap.springtheknife.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.mindswap.springtheknife.dto.booking.BookingCreateDto;
import org.mindswap.springtheknife.dto.booking.BookingGetDto;
import org.mindswap.springtheknife.dto.booking.BookingPatchDto;
import org.mindswap.springtheknife.exceptions.booking.BookingAlreadyExistsException;
import org.mindswap.springtheknife.exceptions.booking.BookingNotFoundException;
import org.mindswap.springtheknife.exceptions.booking.OperationNotAllowedException;
import org.mindswap.springtheknife.exceptions.restaurant.RestaurantNotFoundException;
import org.mindswap.springtheknife.exceptions.user.UserNotFoundException;
import org.mindswap.springtheknife.model.Booking;
import org.mindswap.springtheknife.service.booking.BookingServiceImpl;
import org.mindswap.springtheknife.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/bookings")
public class BookingController {
    private final BookingServiceImpl bookingServiceImpl;

    @Autowired
    public BookingController(BookingServiceImpl bookingService) {
        this.bookingServiceImpl = bookingService;
    }

    @Operation(summary = "Get all Bookings", description = "This method returns a list of all Bookings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of Bookings",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Booking.class))),})
    @GetMapping("/")
    public ResponseEntity<List<BookingGetDto>> getAllBookings(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy
    ) {
        return new ResponseEntity<>(bookingServiceImpl.getAllBookings(pageNumber, pageSize, sortBy), HttpStatus.OK);
    }

    @Operation(summary = "Get a Booking by ID", description = "This method retrieves a booking by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the Booking",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Booking.class))),
            @ApiResponse(responseCode = "400", description = "Booking not found by the provided ID",
                    content = @Content),
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookingGetDto> getBookingById(@PathVariable("id") Long id) throws BookingNotFoundException {
        return new ResponseEntity<>(bookingServiceImpl.getBookingById(id), HttpStatus.OK);
    }

    @Operation(summary = "Create a new Booking", description = "New Booking created")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "New Booking created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Booking.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Booking details provided",
                    content = @Content),
    })
    @PostMapping("/")
    public ResponseEntity<BookingGetDto> addBooking(@Valid @RequestBody BookingCreateDto booking) throws BookingAlreadyExistsException, UserNotFoundException, RestaurantNotFoundException {

        return new ResponseEntity<>(bookingServiceImpl.addBooking(booking), HttpStatus.CREATED);
    }


    @Operation(summary = "Update a Booking", description = "Updates a Booking in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Booking.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Booking details provided",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Booking not found by the provided ID",
                    content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<BookingGetDto> patchBooking(@PathVariable("id") Long id, @Valid @RequestBody BookingPatchDto booking) throws BookingNotFoundException, OperationNotAllowedException {
        return new ResponseEntity<>(bookingServiceImpl.patchBooking(id, booking), HttpStatus.OK);
    }

    @Operation(summary = "Delete a Booking", description = "Deletes a Booking from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Booking not found by the provided ID",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable("id") Long id) throws BookingNotFoundException {
        bookingServiceImpl.deleteBooking(id);
        return new ResponseEntity<>(Message.BOOKING_ID + id + Message.DELETE_SUCCESSFULLY, HttpStatus.OK);
    }
}
