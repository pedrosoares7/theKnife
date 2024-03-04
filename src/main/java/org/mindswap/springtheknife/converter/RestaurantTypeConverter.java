package org.mindswap.springtheknife.converter;

import org.mindswap.springtheknife.dto.city.CityDto;
import org.mindswap.springtheknife.dto.restaurantTypeDto.RestaurantTypeDto;
import org.mindswap.springtheknife.model.City;
import org.mindswap.springtheknife.model.RestaurantType;

import java.util.Set;

public class RestaurantTypeConverter {
    public static RestaurantTypeDto fromModelToRestaurantTypeDto(RestaurantType restaurantType) {
        return new RestaurantTypeDto(
                restaurantType.getId(),
                restaurantType.getType()
        );
    }

    public static RestaurantType fromRestaurantTypeDtoToModel (RestaurantTypeDto restaurantTypeDto){
        return RestaurantType.builder()
                .type(restaurantTypeDto.type())
                .build();
    }

}
