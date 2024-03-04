package org.mindswap.springtheknife.converter;

import org.mindswap.springtheknife.dto.city.CityDto;
import org.mindswap.springtheknife.dto.city.CityGetDto;
import org.mindswap.springtheknife.model.City;

public class CityConverter {

 /*   public static CityDto fromModelToCreateDto(City city) {
        return new CityDto(
                city.getName(),
                city.getRestaurants().stream().map(RestaurantConverter::fromModelToRestaurantDto).toList()
        );
    }*/

    public static City fromCreateDtoToModel (CityDto cityDto){
        return City.builder()
                .name(cityDto.name())
                .build();
    }

    public static CityGetDto fromModelToCityGetDto(City city) {
        return new CityGetDto(
                city.getId(),
                city.getName(),
                city.getRestaurants().stream().map(RestaurantConverter::fromModelToRestaurantDto).toList()
        );

    }


    public static CityDto fromCreateDtoToDto(CityDto city) {
        City tempCity = fromCreateDtoToModel(city);
        return fromModelToCityDto(tempCity);
    }

    private static CityDto fromModelToCityDto(City city) {
        return new CityDto(
                city.getName()
        )
                ;
    }
}
