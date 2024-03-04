package org.mindswap.springtheknife.dto.restaurant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.mindswap.springtheknife.model.Address;

import java.io.Serializable;

import static org.mindswap.springtheknife.utils.Message.*;
import static org.mindswap.springtheknife.utils.Message.VALID_EMAIL;

public record RestaurantPatchDto(

        @NotNull(message = RESTAURANT_ADDRESS_MANDATORY)
        Address address,

        @Email(message = EMAIL_MANDATORY)
        @Pattern(regexp = EMAIL_VALIDATOR, message =VALID_EMAIL)
        String email

) implements Serializable {
}
