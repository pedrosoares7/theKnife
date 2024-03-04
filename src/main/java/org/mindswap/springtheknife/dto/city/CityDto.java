package org.mindswap.springtheknife.dto.city;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.mindswap.springtheknife.dto.restaurant.RestaurantGetDto;
import org.mindswap.springtheknife.utils.Message;

import java.io.Serializable;
import java.util.List;

public record CityDto(
        @Schema(example = "Porto")
        @NotNull(message = Message.NAME_ERROR)
        String name

) implements Serializable {
}