package org.mindswap.springtheknife.dto.userexperience;

import jakarta.validation.constraints.*;
import org.mindswap.springtheknife.utils.Message;

import java.io.Serializable;

public record UserExperiencePatchDto(



        @Min(value = 0, message = Message.INVALID_RATING)
        @Max(value = 10, message = Message.INVALID_RATING)
        Double rating,


        @Pattern(regexp = Message.COMMENT_VALIDATOR, message = Message.INVALID_COMMENT)
        String comment
)implements Serializable {
}
