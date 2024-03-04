package org.mindswap.springtheknife.dto.user;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.mindswap.springtheknife.dto.restaurant.RestaurantGetDto;
import org.mindswap.springtheknife.model.Restaurant;
import org.mindswap.springtheknife.utils.Message;

import java.io.Serializable;
import java.util.Set;

import static org.mindswap.springtheknife.utils.Message.USERNAME_VALIDATOR;

public record UserGetDto(

        Long userId,
        String userName,
        Set<RestaurantGetDto> favoriteRestaurants
) implements Serializable {

}
