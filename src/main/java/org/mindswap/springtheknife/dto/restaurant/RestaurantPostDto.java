package org.mindswap.springtheknife.dto.restaurant;

import jakarta.validation.constraints.*;
import org.mindswap.springtheknife.dto.city.CityDto;
import org.mindswap.springtheknife.model.Address;
import org.mindswap.springtheknife.model.RestaurantType;

import java.io.Serializable;
import java.util.Set;
import static org.mindswap.springtheknife.utils.Message.*;

public record RestaurantPostDto(
        @NotBlank(message = RESTAURANT_NAME_MANDATORY)
        @Pattern(regexp = RESTAURANT_VALIDATOR, message = VALID_RESTAURANT_NAME)
        String name,
        @NotNull(message = RESTAURANT_ADDRESS_MANDATORY)
        Address address,
        @Email(message = EMAIL_MANDATORY)
        @Pattern(regexp = EMAIL_VALIDATOR, message =VALID_EMAIL)
        String email,
        @NotNull(message = PHONE_NUMBER_MANDATORY)
        @Pattern(regexp = PHONE_NUMBER_VALIDATOR, message = INVALID_PHONE_NUMBER)
        String phoneNumber,
        @NotNull(message = LATITUDE_MANDATORY)
        @Min(value = -90, message = INVALID_LATITUDE)
        @Max(value = 90, message = INVALID_LATITUDE)
        Double latitude,
        @NotNull(message = LONGITUDE_MANDATORY)
        @Min(value = -180, message = INVALID_LONGITUDE)
        @Max(value = 180, message = INVALID_LONGITUDE)
        Double longitude,
        @Min(value = 1, message = INVALID_CITY_ID)
        Long cityId,
        @NotNull
        Set<Long> restaurantTypes
        
)implements Serializable {
}
