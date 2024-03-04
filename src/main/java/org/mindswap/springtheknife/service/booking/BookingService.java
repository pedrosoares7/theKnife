package org.mindswap.springtheknife.service.booking;

import org.mindswap.springtheknife.dto.booking.BookingCreateDto;
import org.mindswap.springtheknife.dto.booking.BookingGetDto;
import org.mindswap.springtheknife.dto.booking.BookingPatchDto;
import org.mindswap.springtheknife.exceptions.booking.BookingAlreadyExistsException;
import org.mindswap.springtheknife.exceptions.booking.BookingNotFoundException;
import org.mindswap.springtheknife.exceptions.booking.OperationNotAllowedException;
import org.mindswap.springtheknife.exceptions.restaurant.RestaurantNotFoundException;
import org.mindswap.springtheknife.exceptions.user.UserNotFoundException;

import java.util.List;

public interface BookingService {


    List<BookingGetDto> getAllBookings(int pageNumber, int pageSize, String sortBy);

    BookingGetDto getBookingById(Long id) throws BookingNotFoundException;

    BookingGetDto addBooking(BookingCreateDto booking) throws BookingAlreadyExistsException, UserNotFoundException, RestaurantNotFoundException;

    BookingGetDto patchBooking(Long id, BookingPatchDto booking) throws BookingNotFoundException, OperationNotAllowedException;

    void deleteBooking(Long id) throws BookingNotFoundException;
}
