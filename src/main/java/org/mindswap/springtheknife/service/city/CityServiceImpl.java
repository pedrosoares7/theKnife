package org.mindswap.springtheknife.service.city;

import org.mindswap.springtheknife.converter.CityConverter;
import org.mindswap.springtheknife.dto.city.CityDto;
import org.mindswap.springtheknife.dto.city.CityGetDto;
import org.mindswap.springtheknife.exceptions.city.CityNotFoundException;
import org.mindswap.springtheknife.exceptions.city.CityAlreadyExistsException;
import org.mindswap.springtheknife.model.City;
import org.mindswap.springtheknife.repository.CityRepository;
import org.mindswap.springtheknife.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    @Cacheable(cacheNames = "City", key = "{#pageNumber, #pageSize, #sortBy}")
    public List<CityGetDto> getAllCities(int pageNumber, int pageSize, String sortBy) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, sortBy);
        Page<City> pageCities = this.cityRepository.findAll(pageRequest);
        return pageCities.stream()
                .map(CityConverter::fromModelToCityGetDto)
                .toList();
    }

    @Override
    @Cacheable(cacheNames = "getCity", key = "#id")
    public CityGetDto getCity(Long id) throws CityNotFoundException {
        Optional<City> cityOptional = cityRepository.findById(id);
        if (cityOptional.isEmpty()) {
            throw new CityNotFoundException(id + Message.CITY_NOT_FOUND);
        }
        return CityConverter.fromModelToCityGetDto(cityOptional.get());
    }

    public City getCityById(Long cityId) throws CityNotFoundException {
        Optional<City> cityOptional = cityRepository.findById(cityId);
        if (cityOptional.isEmpty()) {
            throw new CityNotFoundException(Message.CITY_WITH_ID + cityId + Message.NOT_EXIST);
        }
        return cityOptional.get();
    }

    @Override
    public CityDto createCity(CityDto city) throws CityAlreadyExistsException {
        Optional<City> cityOptional = this.cityRepository.findByName(city.name());
        if (cityOptional.isPresent()) {
            throw new CityAlreadyExistsException(Message.DUPLICATE_NAME + " " + city.name() + " " + Message.EXIST);
        }
        cityRepository.save(CityConverter.fromCreateDtoToModel(city));
        return CityConverter.fromCreateDtoToDto(city);

    }

    @Override
    @CachePut(cacheNames = "CityPatch", key = "#cityId")
    public void updateCity(long cityId, City city) throws CityNotFoundException {
        Optional<City> cityOptional = cityRepository.findById(cityId);
        if (cityOptional.isEmpty()) {
            throw new CityNotFoundException(Message.CITY_WITH_ID + " " + cityId + " " + Message.NOT_EXIST);
        }
        City cityToUpdate = cityOptional.get();
        if (city.getName() != null && !city.getName().isEmpty() && !city.getName().equals(cityToUpdate.getName())) {
            cityToUpdate.setName(city.getName());
        }
        cityRepository.save(cityToUpdate);
    }

    @Override
    @CacheEvict(cacheNames = "CityDelete", allEntries = true)
    public void deleteCity(long cityId) throws CityNotFoundException {
        boolean exists = cityRepository.existsById(cityId);
        if (!exists) {
            throw new CityNotFoundException(Message.CITY_WITH_ID + " " + cityId + " " + Message.NOT_EXIST);
        }
        cityRepository.deleteById(cityId);
    }
}
