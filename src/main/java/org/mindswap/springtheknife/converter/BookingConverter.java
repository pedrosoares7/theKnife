package org.mindswap.springtheknife.converter;
import org.mindswap.springtheknife.dto.booking.BookingCreateDto;
import org.mindswap.springtheknife.dto.booking.BookingGetDto;
import org.mindswap.springtheknife.model.Booking;
import org.mindswap.springtheknife.model.Restaurant;
import org.mindswap.springtheknife.model.User;

public class BookingConverter {
    public static BookingGetDto fromModelToBookingDto(Booking booking) {
        return new BookingGetDto(
                booking.getId(),
                UserConverter.fromEntityToGetDto(booking.getUser()),
                RestaurantConverter.fromModelToRestaurantDto(booking.getRestaurant()),
                booking.getBookingTime(),
                booking.getStatus()
        );
    }

    public static Booking fromBookingDtoToModel(BookingCreateDto bookingCreateDto, User user, Restaurant restaurant) {
        return Booking.builder()
                .user(user)
                .restaurant(restaurant)
                .bookingTime(bookingCreateDto.bookingTime())
                .status(bookingCreateDto.status())
                .build();
    }
}
