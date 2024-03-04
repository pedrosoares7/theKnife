package org.mindswap.springtheknife.dto.booking;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import org.mindswap.springtheknife.Enum.BookingStatus;
import org.mindswap.springtheknife.utils.Message;

import java.io.Serializable;
import java.time.LocalDateTime;

public record BookingPatchDto(
        @FutureOrPresent(message = Message.BOOKING_FUTURE)
        LocalDateTime bookingTime,

        @Enumerated(EnumType.STRING)
        BookingStatus status
) implements Serializable {
}
