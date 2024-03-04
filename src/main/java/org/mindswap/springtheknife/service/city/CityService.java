package org.mindswap.springtheknife.service.city;

import org.mindswap.springtheknife.dto.city.CityDto;
import org.mindswap.springtheknife.dto.city.CityGetDto;
import org.mindswap.springtheknife.exceptions.city.CityNotFoundException;
import org.mindswap.springtheknife.exceptions.city.CityAlreadyExistsException;
import org.mindswap.springtheknife.model.City;

import java.util.List;

public interface CityService {
    List<CityGetDto> getAllCities(int pageNumber, int pageSize, String sortBy) throws Exception;

    City getCityById(Long cityId) throws CityNotFoundException;

    CityDto createCity(CityDto city) throws CityAlreadyExistsException;

    void updateCity(long cityId, City city) throws CityNotFoundException;

    void deleteCity(long cityId) throws CityNotFoundException;

    CityGetDto getCity(Long id) throws CityNotFoundException;
}
