package org.mindswap.springtheknife.dto.booking;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.mindswap.springtheknife.Enum.BookingStatus;
import org.mindswap.springtheknife.dto.restaurant.RestaurantGetDto;
import org.mindswap.springtheknife.dto.user.UserGetDto;
import org.mindswap.springtheknife.utils.Message;

import java.io.Serializable;
import java.time.LocalDateTime;

public record BookingGetDto (

        Long id,

        UserGetDto user,
        RestaurantGetDto restaurant,
        LocalDateTime bookingTime,

        BookingStatus status
) implements Serializable {
}
