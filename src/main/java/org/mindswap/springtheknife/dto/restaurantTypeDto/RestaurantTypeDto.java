package org.mindswap.springtheknife.dto.restaurantTypeDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.mindswap.springtheknife.utils.Message;

import java.io.Serializable;

public record RestaurantTypeDto(

        Long id,
        @Schema(example = "ItalianFood")
        @NotNull(message = Message.TYPE_MANDATORY)
        @Pattern(regexp = Message.TYPE_VALIDATOR, message = Message.INVALID_TYPE)
        String type
) implements Serializable {
}