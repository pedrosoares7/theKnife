package org.mindswap.springtheknife.dto.restaurant;


import jakarta.validation.constraints.*;
import org.mindswap.springtheknife.dto.restaurantTypeDto.RestaurantTypeDto;
import org.mindswap.springtheknife.model.Address;
import org.mindswap.springtheknife.model.RestaurantType;
import org.mindswap.springtheknife.utils.Message;

import java.io.Serializable;
import java.util.Set;

import static org.mindswap.springtheknife.utils.Message.*;

public record RestaurantGetDto(

        String cityName,
        String name,
        String email,
        Address address,
        String phoneNumber,
        Double rating,
        Set<RestaurantTypeDto> restaurantTypes
    
) implements Serializable {

}
