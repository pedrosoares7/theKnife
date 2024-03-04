package org.mindswap.springtheknife.service.userexperience;

import org.mindswap.springtheknife.Enum.BookingStatus;
import org.mindswap.springtheknife.converter.UserExperienceConverter;
import org.mindswap.springtheknife.dto.userexperience.UserExperienceCreateDto;
import org.mindswap.springtheknife.dto.userexperience.UserExperienceGetDto;
import org.mindswap.springtheknife.dto.userexperience.UserExperiencePatchDto;
import org.mindswap.springtheknife.exceptions.booking.BookingNotFoundException;
import org.mindswap.springtheknife.exceptions.restaurant.RestaurantNotFoundException;
import org.mindswap.springtheknife.exceptions.user.UserNotFoundException;
import org.mindswap.springtheknife.exceptions.userexperience.UserExperienceNotFoundException;
import org.mindswap.springtheknife.model.User;
import org.mindswap.springtheknife.model.UserExperience;
import org.mindswap.springtheknife.repository.BookingRepository;
import org.mindswap.springtheknife.repository.UserExperienceRepository;
import org.mindswap.springtheknife.service.booking.BookingServiceImpl;
import org.mindswap.springtheknife.service.restaurant.RestaurantServiceImpl;
import org.mindswap.springtheknife.service.user.UserServiceImpl;
import org.mindswap.springtheknife.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserExperienceServiceImpl implements UserExperienceService {

    private final UserExperienceRepository userExperienceRepository;
    private final BookingRepository bookingRepository;

    private final UserServiceImpl userServiceImpl;

    private final RestaurantServiceImpl restaurantServiceImpl;
    private final BookingServiceImpl bookingServiceImpl;

    @Autowired
    public UserExperienceServiceImpl(UserExperienceRepository userExperienceRepository, BookingRepository bookingRepository, UserServiceImpl userServiceImpl,
                                     RestaurantServiceImpl restaurantServiceImpl, BookingServiceImpl bookingServiceImpl) {
        this.userExperienceRepository = userExperienceRepository;
        this.bookingRepository = bookingRepository;
        this.userServiceImpl = userServiceImpl;
        this.restaurantServiceImpl = restaurantServiceImpl;

        this.bookingServiceImpl = bookingServiceImpl;
    }

    @Override
    @Cacheable(cacheNames = "UserExperience", key = "{#pageNumber, #pageSize, #sortBy}")
    public List<UserExperienceGetDto> getAllUsersExperiences(int pageNumber, int pageSize, String sortBy) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, sortBy);
        Page<UserExperience> pageUserExperiences = userExperienceRepository.findAll(pageRequest);
        return pageUserExperiences.stream()
                .map(UserExperienceConverter::fromEntityToGetDto)
                .toList();
    }

    @Override
    @Cacheable(cacheNames = "UserExperienceById", key = "#id" )
    public UserExperienceGetDto getUserExperienceById(Long id) throws UserExperienceNotFoundException {
        Optional<UserExperience> userExperienceOptional = userExperienceRepository.findById(id);
        if (userExperienceOptional.isEmpty()) {
            throw new UserExperienceNotFoundException(id + Message.USER_EXPERIENCE_ID_NOT_FOUND);
        }
        UserExperience userExperience = userExperienceOptional.get();
        return UserExperienceConverter.fromEntityToGetDto(userExperience);
    }

    @Override
    public UserExperienceGetDto addNewUserExperience(UserExperienceCreateDto userExperience) throws UserNotFoundException, RestaurantNotFoundException, BookingNotFoundException, UserExperienceNotFoundException {
       /* User newUser = UserConverter.fromGetDtoToEntity(userService.getUserById(userExperience.userId()));
        Restaurant newRestaurant = RestaurantConverter.fromRestaurantDtoToModel(restaurantService.getById(userExperience.restaurantId()));
        UserExperience userExperienceEntity = UserExperienceConverter.fromUserExperienceCreateDtoToEntity(userExperience, newUser, newRestaurant);
        UserExperience userExperienceSaved = userExperienceRepository.save(userExperienceEntity);
        return UserExperienceConverter.fromEntityToGetDto(userExperienceSaved);*/
        Optional<UserExperience> userExperienceOptional = userExperienceRepository.findById(userExperience.bookingId());
        if (userExperienceOptional.isPresent()) {
            throw new UserExperienceNotFoundException(Message.USER_EXPERIENCE_ID_EXISTS);
        }
        if (bookingServiceImpl.getBookingById(userExperience.bookingId()).status() != BookingStatus.COMPLETE) {
            throw new UserExperienceNotFoundException(Message.BOOKING_STATUS_ILLEGAL);
        }
        UserExperience userExperienceToSave = UserExperienceConverter.fromUserExperienceCreateDtoToEntity
                (userExperience, bookingServiceImpl.getBookingId(userExperience.bookingId()),userServiceImpl.getUserById(userExperience.userId()),
                        restaurantServiceImpl.getById(userExperience.restaurantId()));
        userExperienceToSave.setTimestamp(LocalDateTime.now());
        userExperienceRepository.save(userExperienceToSave);
        return UserExperienceConverter.fromEntityToGetDto(userExperienceToSave);
    }

    @Override
    @CachePut(cacheNames = "UserExperiencePatch", key="#id")
    public UserExperiencePatchDto updateUserExperience(Long id, UserExperiencePatchDto userExperience) throws UserExperienceNotFoundException {
        Optional<UserExperience> userExperienceOptional = userExperienceRepository.findById(id);
        if (!userExperienceOptional.isPresent()) {
            throw new UserExperienceNotFoundException(id + Message.USER_EXPERIENCE_ID_NOT_FOUND);
        }
        UserExperience userExperienceToUpdate = userExperienceOptional.get();
        if (userExperience.rating() > 0 && userExperience.rating() != (userExperienceToUpdate.getRating())) {
            userExperienceToUpdate.setRating(userExperience.rating());
        }
        if (userExperience.comment() != null && !userExperience.comment().equals(userExperienceToUpdate.getComment())) {
            userExperienceToUpdate.setComment(userExperience.comment());
        }
        return UserExperienceConverter.fromEntityToPatchDto(userExperienceRepository.save(userExperienceToUpdate));
    }

    @Override
    @CacheEvict(cacheNames = "UserExperienceDelete", allEntries = true)
    public void deleteUserExperience(Long userExperienceId) throws UserExperienceNotFoundException {
        userExperienceRepository.findById(userExperienceId).orElseThrow(() -> new UserExperienceNotFoundException(userExperienceId + Message.USER_EXPERIENCE_ID_NOT_FOUND));
        userExperienceRepository.deleteById(userExperienceId);
    }
}

