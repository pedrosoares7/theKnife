package org.mindswap.springtheknife.service.restauranttype;

import org.mindswap.springtheknife.converter.RestaurantTypeConverter;
import org.mindswap.springtheknife.dto.restaurantTypeDto.RestaurantTypeDto;
import org.mindswap.springtheknife.exceptions.restaurantType.RestaurantTypeAlreadyExistsException;
import org.mindswap.springtheknife.exceptions.restaurantType.RestaurantTypeNotFoundException;
import org.mindswap.springtheknife.model.RestaurantType;
import org.mindswap.springtheknife.repository.RestaurantTypeRepository;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RestaurantTypeServiceImpl implements RestaurantTypeService {
    RestaurantTypeRepository restaurantTypeRepository;

    @Autowired
    public RestaurantTypeServiceImpl(RestaurantTypeRepository restaurantTypeRepository) {
        this.restaurantTypeRepository = restaurantTypeRepository;
    }

    @Override
    @Cacheable(cacheNames = "RestaurantType", key = "{#pageNumber, #pageSize, #sortBy}")
    public List<RestaurantTypeDto> getAllRestaurantType(int pageNumber, int pageSize, String sortBy) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, sortBy);
        Page<RestaurantType> pageRestaurantType = restaurantTypeRepository.findAll(pageRequest);
        return pageRestaurantType.stream()
                .map(RestaurantTypeConverter::fromModelToRestaurantTypeDto)
                .toList();
    }
    public Set<RestaurantTypeDto> getRestaurantTypeById(Set<Long> restaurantTypeId){
        List<RestaurantType> restaurantType = restaurantTypeRepository.findAllById(restaurantTypeId);
        return restaurantType.stream().map(RestaurantTypeConverter::fromModelToRestaurantTypeDto).collect(Collectors.toSet());
            }

    @Override
    @Cacheable(cacheNames = "RestaurantTypeById", key = "#id" )
    public RestaurantTypeDto getRestaurantTypeById(Long id) throws RestaurantTypeNotFoundException {
        RestaurantType restaurantType = restaurantTypeRepository.findById(id).orElseThrow(() -> new RestaurantTypeNotFoundException(Message.TYPE_ID + id + Message.NOT_FOUND));
        return RestaurantTypeConverter.fromModelToRestaurantTypeDto(restaurantType);
    }

    @Override
    public RestaurantTypeDto addRestaurantType(RestaurantTypeDto restaurantType) throws RestaurantTypeAlreadyExistsException {
        Optional<RestaurantType> typeOptional = restaurantTypeRepository.findByType(restaurantType.type());
        if (typeOptional.isPresent()) {
            throw new RestaurantTypeAlreadyExistsException(Message.ALREADY_EXISTS);
        }
        RestaurantType restaurantType1 = RestaurantTypeConverter.fromRestaurantTypeDtoToModel(restaurantType);
        return RestaurantTypeConverter.fromModelToRestaurantTypeDto(restaurantTypeRepository.save(restaurantType1));
    }

    @Override
    @CacheEvict(cacheNames = "RestaurantTypeDelete", allEntries = true)
    public void deleteRestaurantType(Long restaurantTypeId) throws RestaurantTypeNotFoundException {
        restaurantTypeRepository.findById(restaurantTypeId).orElseThrow(() -> new RestaurantTypeNotFoundException(Message.TYPE_ID + restaurantTypeId + Message.NOT_FOUND));
        restaurantTypeRepository.deleteById(restaurantTypeId);
    }

    @Override
    @CachePut(cacheNames = "RestaurantPatch", key="#id")
    public RestaurantTypeDto patchRestaurantType(Long id, RestaurantTypeDto restaurantType) throws RestaurantTypeNotFoundException {
        RestaurantType dbRestaurantType = restaurantTypeRepository.findById(id).orElseThrow(() -> new RestaurantTypeNotFoundException(Message.TYPE_ID + id + Message.NOT_FOUND));
        if (restaurantTypeRepository.findByType(restaurantType.type()).isPresent()) {
            throw new IllegalStateException(Message.TYPE_TAKEN);
        }
        if (restaurantType.type() != null) {
            dbRestaurantType.setType(restaurantType.type());
        }
        return RestaurantTypeConverter.fromModelToRestaurantTypeDto(restaurantTypeRepository.save(dbRestaurantType));
    }
}
