package org.mindswap.springtheknife.dto.userexperience;

import jakarta.validation.constraints.*;
import org.mindswap.springtheknife.utils.Message;

import java.io.Serializable;

public record UserExperienceCreateDto(
        @Min(value = 1, message = Message.BOOKING_ID_MANDATORY)
        @NotNull(message = Message.BOOKING_ID_MANDATORY)
        Long bookingId,
        @Min(value = 1, message = Message.USER_ID_MANDATORY)
        @NotNull(message = Message.USER_ID_MANDATORY)
        Long userId,
        @Min(value = 1, message = Message.RESTAURANT_ID_MANDATORY)
        @NotNull(message = Message.RESTAURANT_ID_MANDATORY)
        Long restaurantId,

        @NotNull(message = Message.RATING_MANDATORY)
        @Min(value = 0, message = Message.INVALID_RATING)
        @Max(value = 10, message = Message.INVALID_RATING)
        Double rating,
        @NotBlank(message = Message.COMMENT_MANDATORY)
        @Pattern(regexp = Message.COMMENT_VALIDATOR, message = Message.INVALID_COMMENT)
        String comment

)implements Serializable
{

}

