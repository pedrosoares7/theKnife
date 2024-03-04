package org.mindswap.springtheknife.service.restauranttype;

import org.mindswap.springtheknife.dto.restaurantTypeDto.RestaurantTypeDto;
import org.mindswap.springtheknife.exceptions.restaurantType.RestaurantTypeAlreadyExistsException;
import org.mindswap.springtheknife.exceptions.restaurantType.RestaurantTypeNotFoundException;

import java.util.List;

public interface RestaurantTypeService {

    List<RestaurantTypeDto> getAllRestaurantType(int pageNumber, int pageSize, String sortBy);

    RestaurantTypeDto getRestaurantTypeById(Long id) throws RestaurantTypeNotFoundException;

    RestaurantTypeDto addRestaurantType(RestaurantTypeDto restaurantType) throws RestaurantTypeAlreadyExistsException;

    void deleteRestaurantType(Long restaurantTypeId) throws RestaurantTypeNotFoundException;

    RestaurantTypeDto patchRestaurantType(Long id, RestaurantTypeDto restaurantType) throws RestaurantTypeNotFoundException;

}
