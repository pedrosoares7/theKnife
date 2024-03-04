package org.mindswap.springtheknife.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindswap.springtheknife.Enum.BookingStatus;
import org.mindswap.springtheknife.dto.booking.BookingGetDto;
import org.mindswap.springtheknife.dto.restaurant.RestaurantGetDto;
import org.mindswap.springtheknife.dto.user.UserGetDto;
import org.mindswap.springtheknife.dto.userexperience.UserExperienceCreateDto;
import org.mindswap.springtheknife.dto.userexperience.UserExperienceGetDto;
import org.mindswap.springtheknife.dto.userexperience.UserExperiencePatchDto;
import org.mindswap.springtheknife.exceptions.booking.BookingNotFoundException;
import org.mindswap.springtheknife.exceptions.restaurant.RestaurantNotFoundException;
import org.mindswap.springtheknife.exceptions.user.UserNotFoundException;
import org.mindswap.springtheknife.exceptions.userexperience.UserExperienceNotFoundException;
import org.mindswap.springtheknife.model.*;
import org.mindswap.springtheknife.repository.UserExperienceRepository;
import org.mindswap.springtheknife.service.booking.BookingServiceImpl;
import org.mindswap.springtheknife.service.restaurant.RestaurantServiceImpl;
import org.mindswap.springtheknife.service.user.UserServiceImpl;
import org.mindswap.springtheknife.service.userexperience.UserExperienceServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserExperienceServiceTests {

    private static ObjectMapper objectMapper;
    @Mock
    private UserExperienceRepository userExperienceRepository;
    @Mock
    private UserServiceImpl userServiceImpl;
    @Mock
    private RestaurantServiceImpl restaurantServiceImpl;
    @Mock
    private BookingServiceImpl bookingServiceImpl;

    @MockBean
    private User user;

    @MockBean
    private Restaurant restaurant;

    @MockBean
    private Booking booking;

    @MockBean
    private UserExperience userExperience;
    @InjectMocks
    private UserExperienceServiceImpl userExperienceService;


    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @AfterEach
    void deleteData() {
        userExperienceRepository.deleteAll();
        userExperienceRepository.resetId();
    }

    @Test
    void contextLoads() {

    }

    @Test
    @DisplayName("Test to get all user experiences")
    void testGetAllUsersExperiencesWithEmptyList() {

        List<UserExperience> userExperience = new ArrayList<>();
        Page<UserExperience> pageUserExperience = new PageImpl<>(userExperience);
        when(userExperienceRepository.findAll(any(Pageable.class))).thenReturn(pageUserExperience);
        List<UserExperienceGetDto> result = userExperienceService.getAllUsersExperiences(1, 3, "asc");
        assertEquals(userExperience.size(), result.size());
    }

    @Test
    @DisplayName("Test to get all user experiences")
    void testGetAllUsersExperiences() {
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";

        List<UserExperience> userExperiences = IntStream.range(0, pageSize)
                .mapToObj(i -> {
                    UserExperience userExperience = new UserExperience();
                    Booking booking = new Booking();
                    booking.setId((long) i);
                    User user = new User();
                    user.setId((long) i);
                    booking.setUser(user);
                    userExperience.setBooking(booking);
                    userExperience.setUser(user);
                    Restaurant restaurant = new Restaurant();
                    City city = new City();
                    city.setName("City" + i);
                    restaurant.setCity(city);
                    booking.setRestaurant(restaurant);
                    userExperience.setRestaurant(restaurant);
                    List<RestaurantType> restaurantTypes = new ArrayList<>();
                    RestaurantType restaurantType = new RestaurantType();
                    restaurantType.setType("Type" + i);
                    restaurantTypes.add(restaurantType);
                    restaurant.setRestaurantTypes(restaurantTypes);
                    userExperience.setRating((double) i);
                    return userExperience;
                })
                .collect(Collectors.toList());

        Page<UserExperience> pageUserExperiences = new PageImpl<>(userExperiences);

        when(userExperienceRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, sortBy)))
                .thenReturn(pageUserExperiences);

        List<UserExperienceGetDto> result = userExperienceService.getAllUsersExperiences(pageNumber, pageSize, sortBy);

        assertEquals(pageSize, result.size());
    }

    @Test
    @DisplayName("Test to get user experience by id")
    void testGetUserExperienceById() throws UserExperienceNotFoundException {

        Long id = 1L;
        UserExperience userExperience = new UserExperience();
        User user = new User();
        City city = Mockito.mock(City.class);
        Mockito.when(city.getName()).thenReturn("Test City");
        Restaurant restaurant = Mockito.mock(Restaurant.class);
        Mockito.when(restaurant.getCity()).thenReturn(city);
        userExperience.setUser(user);
        userExperience.setRestaurant(restaurant);
        userExperience.setRating(5.0);
        when(userExperienceRepository.findById(id)).thenReturn(Optional.of(userExperience));

    }

    @Test
    @DisplayName("Test to get user experience by id - Not Found")
    void testGetUserExperienceById_NotFound() {

        Long id = 1L;
        when(userExperienceRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserExperienceNotFoundException.class, () -> userExperienceService.getUserExperienceById(id));
    }

    @Test
    @DisplayName("Test to add new user experience with non-existing booking id")
    void testAddNewUserExperience_NonExistingBookingId() throws BookingNotFoundException {

        Long bookingId = 1L;
        Long userId = 1L;
        Long restaurantId = 1L;
        Double rating = 5.0;
        String comment = "Test comment";
        UserExperienceCreateDto userExperienceCreateDto = new UserExperienceCreateDto(bookingId, userId, restaurantId, rating, comment);


        when(bookingServiceImpl.getBookingById(bookingId)).thenThrow(new BookingNotFoundException("Booking not found"));


        assertThrows(BookingNotFoundException.class, () -> userExperienceService.addNewUserExperience(userExperienceCreateDto));
    }

    @Test
    @DisplayName("Test to add new user experience with existing booking id")
    void testAddNewUserExperience_ExistingBookingId() {

        Long existingBookingId = 1L;
        Long userId = 1L;
        Long restaurantId = 1L;
        Double rating = 5.0;
        String comment = "Test comment";
        UserExperienceCreateDto userExperienceCreateDto = new UserExperienceCreateDto(existingBookingId, userId, restaurantId, rating, comment);


        when(userExperienceRepository.findById(existingBookingId)).thenReturn(Optional.of(new UserExperience(1L, user, restaurant, 1.0, comment, LocalDateTime.now(), booking)));

        assertThrows(UserExperienceNotFoundException.class, () -> userExperienceService.addNewUserExperience(userExperienceCreateDto));
    }

    @Test
    @DisplayName("Test to add user experience by booking id - User not found")
    void testAddNewUserExperience_UserNotFound() throws UserNotFoundException, BookingNotFoundException {

        Long bookingId = 1L;
        Long userId = 1L;
        Long restaurantId = 1L;
        Double rating = 5.0;
        String comment = "Test comment";
        UserExperienceCreateDto userExperienceCreateDto = new UserExperienceCreateDto(bookingId, userId, restaurantId, rating, comment);

        // Create mock objects and dummy values for the BookingGetDto constructor
        Long id = 1L;
        UserGetDto userGetDto = mock(UserGetDto.class);
        RestaurantGetDto restaurantGetDto = mock(RestaurantGetDto.class);
        LocalDateTime dateTime = LocalDateTime.now();
        BookingStatus status = BookingStatus.COMPLETE; // Set the status to COMPLETE

        // Create a new instance of BookingGetDto with the required arguments
        BookingGetDto bookingGetDto = new BookingGetDto(id, userGetDto, restaurantGetDto, dateTime, status);

        when(bookingServiceImpl.getBookingById(bookingId)).thenReturn(bookingGetDto);

        when(userServiceImpl.getUserById(userId)).thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> userExperienceService.addNewUserExperience(userExperienceCreateDto));
    }

    @Test
    @DisplayName("Test to add user experience by booking id - Restaurant not found")
    void testAddNewUserExperience_RestaurantNotFound() throws UserNotFoundException, RestaurantNotFoundException, BookingNotFoundException {

        Long bookingId = 1L;
        Long userId = 1L;
        Long restaurantId = 1L;
        Double rating = 5.0;
        String comment = "Test comment";
        UserExperienceCreateDto userExperienceCreateDto = new UserExperienceCreateDto(bookingId, userId, restaurantId, rating, comment);

        // Create mock objects and dummy values for the BookingGetDto constructor
        Long id = 1L;
        UserGetDto userGetDto = mock(UserGetDto.class);
        RestaurantGetDto restaurantGetDto = mock(RestaurantGetDto.class);
        LocalDateTime dateTime = LocalDateTime.now();
        BookingStatus status = BookingStatus.COMPLETE; // Set the status to COMPLETE

        // Create a new instance of BookingGetDto with the required arguments
        BookingGetDto bookingGetDto = new BookingGetDto(id, userGetDto, restaurantGetDto, dateTime, status);

        when(bookingServiceImpl.getBookingById(bookingId)).thenReturn(bookingGetDto);

        when(restaurantServiceImpl.getById(restaurantId)).thenThrow(new RestaurantNotFoundException("Restaurant not found"));

        assertThrows(RestaurantNotFoundException.class, () -> userExperienceService.addNewUserExperience(userExperienceCreateDto));
    }

    @Test
    @DisplayName("Test to delete user experience by booking id - User experience not found")
    void testDeleteUserExperience_UserExperienceNotFound() {

        Long userExperienceId = 1L;
        when(userExperienceRepository.findById(userExperienceId)).thenReturn(Optional.empty());

        assertThrows(UserExperienceNotFoundException.class, () -> userExperienceService.deleteUserExperience(userExperienceId));
    }

    @Test
    @DisplayName("Test to delete user experience")
    void testDeleteUserExperience() throws UserExperienceNotFoundException {

        Long userExperienceId = 1L;
        UserExperience userExperience = new UserExperience();
        when(userExperienceRepository.findById(userExperienceId)).thenReturn(Optional.of(userExperience));

        userExperienceService.deleteUserExperience(userExperienceId);

        verify(userExperienceRepository, times(1)).deleteById(userExperienceId);
    }

    @Test
    @DisplayName("Test to update user experience - User experience not found")
    void testUpdateUserExperience_UserExperienceNotFound() {

        Long userExperienceId = 1L;
        UserExperiencePatchDto userExperiencePatchDto = new UserExperiencePatchDto(4.0, "Updated comment");
        when(userExperienceRepository.findById(userExperienceId)).thenReturn(Optional.empty());


        assertThrows(UserExperienceNotFoundException.class, () -> userExperienceService.updateUserExperience(userExperienceId, userExperiencePatchDto));
    }

    @Test
    @DisplayName("Test to update user experience")
    void testUpdateUserExperience() throws UserExperienceNotFoundException {

        Long userExperienceId = 1L;
        UserExperiencePatchDto userExperiencePatchDto = new UserExperiencePatchDto(4.0, "Updated comment");
        UserExperience userExperience = new UserExperience();
        userExperience.setRating(3.0);
        userExperience.setComment("Original comment");
        when(userExperienceRepository.findById(userExperienceId)).thenReturn(Optional.of(userExperience));
        when(userExperienceRepository.save(any(UserExperience.class))).thenAnswer(i -> i.getArguments()[0]);

        UserExperiencePatchDto result = userExperienceService.updateUserExperience(userExperienceId, userExperiencePatchDto);

    }

}