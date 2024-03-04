package org.mindswap.springtheknife.service.restaurant;

import org.mindswap.springtheknife.converter.RestaurantConverter;
import org.mindswap.springtheknife.dto.restaurant.RestaurantGetDto;
import org.mindswap.springtheknife.dto.restaurant.RestaurantPatchDto;
import org.mindswap.springtheknife.dto.restaurant.RestaurantPostDto;
import org.mindswap.springtheknife.exceptions.city.CityNotFoundException;
import org.mindswap.springtheknife.exceptions.restaurant.RestaurantAlreadyExistsException;
import org.mindswap.springtheknife.exceptions.restaurant.RestaurantNotFoundException;
import org.mindswap.springtheknife.model.City;
import org.mindswap.springtheknife.model.Restaurant;
import org.mindswap.springtheknife.model.RestaurantType;
import org.mindswap.springtheknife.repository.RestaurantRepository;
import org.mindswap.springtheknife.repository.RestaurantTypeRepository;
import org.mindswap.springtheknife.service.city.CityServiceImpl;
import org.mindswap.springtheknife.service.restaurantimage.RestaurantImageService;
import org.mindswap.springtheknife.service.restauranttype.RestaurantTypeServiceImpl;
import org.mindswap.springtheknife.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final CityServiceImpl cityServiceImpl;

    private final RestaurantTypeServiceImpl restaurantTypeServiceImpl;
    private final RestaurantTypeRepository restaurantTypeRepository;

    private final RestaurantImageService restaurantImageService;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository clientRepository, CityServiceImpl cityServiceImpl, RestaurantTypeServiceImpl restaurantTypeServiceImpl, RestaurantTypeRepository restaurantTypeRepository, RestaurantImageService restaurantImageService) {
        this.restaurantRepository = clientRepository;
        this.cityServiceImpl = cityServiceImpl;
        this.restaurantTypeServiceImpl = restaurantTypeServiceImpl;
        this.restaurantTypeRepository = restaurantTypeRepository;
        this.restaurantImageService = restaurantImageService;
    }

    @Override
    @Cacheable(cacheNames = "Restaurant", key = "{#pageNumber, #pageSize, #sortBy}")
    public List<RestaurantGetDto> getAllRestaurants(int pageNumber, int pageSize, String sortBy) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, sortBy);
        Page<Restaurant> pageRestaurants = restaurantRepository.findAll(pageRequest);
        return pageRestaurants.stream()
                .map(RestaurantConverter::fromModelToRestaurantDto)
                .toList();
    }


    @Override
    @Cacheable(cacheNames = "RestaurantById", key = "#id" )
    public RestaurantGetDto getRestaurant(Long id) throws RestaurantNotFoundException {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);
        if (restaurantOptional.isEmpty()) {
            throw new RestaurantNotFoundException(id + Message.USER_ID_DOES_NOT_EXIST);
        }
        return RestaurantConverter.fromModelToRestaurantDto(restaurantOptional.get());
    }

    @Override
    public Restaurant getById(Long id) throws RestaurantNotFoundException {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);
        if (restaurantOptional.isEmpty()) {
            throw new RestaurantNotFoundException(id + Message.NOT_EXIST);
        }
        return (restaurantOptional.get());
    }

    @Override
    public RestaurantGetDto addRestaurant(RestaurantPostDto restaurant) throws RestaurantAlreadyExistsException, CityNotFoundException, IOException {
        List<RestaurantType> restaurantTypes = restaurant.restaurantTypes().stream().map(restaurantTypeRepository::findById).filter(Optional::isPresent).map(Optional::get).toList();

        Optional<City> cityOptional = Optional.ofNullable(this.cityServiceImpl.getCityById(restaurant.cityId()));
        if (cityOptional.isEmpty()) {
            throw new CityNotFoundException(restaurant.cityId() + Message.CITY_NOT_FOUND);
        }
        Optional<Restaurant> restaurantOpt = this.restaurantRepository.findByEmail(restaurant.email());
        if (restaurantOpt.isPresent()) {
            throw new RestaurantAlreadyExistsException("This restaurant already exists.");
        }

        Restaurant newRestaurant = RestaurantConverter.fromRestaurantCreateDtoToEntity(restaurant, cityServiceImpl.getCityById(restaurant.cityId()), restaurantTypes);
        restaurantRepository.save(newRestaurant);

        return RestaurantConverter.fromModelToRestaurantDto(newRestaurant);
    }

    @Override
    public List<RestaurantGetDto> addListOfRestaurants(List<RestaurantPostDto> restaurantList) throws RestaurantAlreadyExistsException, CityNotFoundException, IOException {
        List<RestaurantGetDto> newRestaurantsList = new ArrayList<>();
        for (RestaurantPostDto restaurantPostDto : restaurantList) {
            List<RestaurantType> restaurantTypes = restaurantPostDto.restaurantTypes().stream().map(restaurantTypeRepository::findById).filter(Optional::isPresent).map(Optional::get).toList();
            Optional<City> cityOptional = Optional.ofNullable(this.cityServiceImpl.getCityById(restaurantPostDto.cityId()));
            if (cityOptional.isEmpty()) {
                throw new CityNotFoundException(restaurantPostDto.cityId() + Message.CITY_NOT_FOUND);
            }
            Optional<Restaurant> restaurantOpt = this.restaurantRepository.findByEmail(restaurantPostDto.email());
            if (restaurantOpt.isPresent()) {
                throw new RestaurantAlreadyExistsException("This restaurant already exists.");
            }
            Restaurant newRestaurant = RestaurantConverter.fromRestaurantCreateDtoToEntity(restaurantPostDto, cityServiceImpl.getCityById(restaurantPostDto.cityId()), restaurantTypes);
            restaurantRepository.save(newRestaurant);
            newRestaurantsList.add(RestaurantConverter.fromModelToRestaurantDto(newRestaurant));
        }
        return newRestaurantsList;
    }

    @Override
    @CacheEvict(cacheNames = "RestaurantDelete", allEntries = true)
    public void deleteRestaurant(Long restaurantId) throws RestaurantNotFoundException {
        restaurantRepository.findById(restaurantId).orElseThrow(() -> new RestaurantNotFoundException("Restaurant with id " + restaurantId + " not found."));
        restaurantRepository.deleteById(restaurantId);
    }

    @Override
    @CachePut(cacheNames = "RestaurantPatch", key="#id")
    public RestaurantGetDto patchRestaurant(Long id, RestaurantPatchDto restaurant) throws RestaurantNotFoundException {
        Restaurant dbRestaurant = restaurantRepository.findById(id).orElseThrow(() -> new RestaurantNotFoundException("Restaurant with id " + id + " not found."));
        if (restaurantRepository.findByEmail(restaurant.email()).isPresent()) {
            throw new IllegalStateException("email taken");
        }
        if (restaurant.address() != null) {
            dbRestaurant.setAddress(restaurant.address());
        }
        if (restaurant.email() != null) {
            dbRestaurant.setEmail(restaurant.email());
        }
        return RestaurantConverter.fromModelToRestaurantDto(restaurantRepository.save(dbRestaurant));
    }

    @Override
    public RestaurantGetDto addRestaurantWithImage(RestaurantPostDto restaurant) throws RestaurantAlreadyExistsException, CityNotFoundException, IOException {
        List<RestaurantType> restaurantTypes = restaurant.restaurantTypes().stream().map(restaurantTypeRepository::findById).filter(Optional::isPresent).map(Optional::get).toList();

        Optional<City> cityOptional = Optional.ofNullable(this.cityServiceImpl.getCityById(restaurant.cityId()));
        if (cityOptional.isEmpty()) {
            throw new CityNotFoundException(restaurant.cityId() + Message.CITY_NOT_FOUND);
        }
        Optional<Restaurant> restaurantOpt = this.restaurantRepository.findByEmail(restaurant.email());
        if (restaurantOpt.isPresent()) {
            throw new RestaurantAlreadyExistsException("This restaurant already exists.");
        }

        Restaurant newRestaurant = RestaurantConverter.fromRestaurantCreateDtoToEntity(restaurant, cityServiceImpl.getCityById(restaurant.cityId()), restaurantTypes);
        restaurantRepository.save(newRestaurant);
        restaurantImageService.saveRestaurantImage(newRestaurant);

        return RestaurantConverter.fromModelToRestaurantDto(newRestaurant);
    }

    @Override
    public List<RestaurantGetDto> addListOfRestaurantsWithImage(List<RestaurantPostDto> restaurantList) throws RestaurantAlreadyExistsException, CityNotFoundException, IOException {
        List<RestaurantGetDto> newRestaurantsList = new ArrayList<>();
        for (RestaurantPostDto restaurantPostDto : restaurantList) {
            List<RestaurantType> restaurantTypes = restaurantPostDto.restaurantTypes().stream().map(restaurantTypeRepository::findById).filter(Optional::isPresent).map(Optional::get).toList();
            Optional<City> cityOptional = Optional.ofNullable(this.cityServiceImpl.getCityById(restaurantPostDto.cityId()));
            if (cityOptional.isEmpty()) {
                throw new CityNotFoundException(restaurantPostDto.cityId() + Message.CITY_NOT_FOUND);
            }
            Optional<Restaurant> restaurantOpt = this.restaurantRepository.findByEmail(restaurantPostDto.email());
            if (restaurantOpt.isPresent()) {
                throw new RestaurantAlreadyExistsException("This restaurant already exists.");
            }
            Restaurant newRestaurant = RestaurantConverter.fromRestaurantCreateDtoToEntity(restaurantPostDto, cityServiceImpl.getCityById(restaurantPostDto.cityId()), restaurantTypes);

            restaurantRepository.save(newRestaurant);
            restaurantImageService.saveRestaurantImage(newRestaurant);

            newRestaurantsList.add(RestaurantConverter.fromModelToRestaurantDto(newRestaurant));
        }
        return newRestaurantsList;
    }

    @Override
    public Double findAverageRating(Long restaurantId) {
        return restaurantRepository.findAverageRating(restaurantId);
    }
}
