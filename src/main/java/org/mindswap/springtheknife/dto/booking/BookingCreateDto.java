package org.mindswap.springtheknife.dto.booking;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import org.mindswap.springtheknife.Enum.BookingStatus;
import org.mindswap.springtheknife.utils.Message;

import java.io.Serializable;
import java.time.LocalDateTime;

public record BookingCreateDto(

        @Min(value = 1, message = Message.USER_ID_MANDATORY)
        Long userId,
        @Min(value = 1, message = Message.RESTAURANT_ID_MANDATORY)
        Long restaurantId,
        @FutureOrPresent(message = Message.BOOKING_FUTURE)
        @NotNull(message = Message.BOOKING_MANDATORY)
        LocalDateTime bookingTime,
        @Enumerated(EnumType.STRING)
        BookingStatus status

) implements Serializable {
}
