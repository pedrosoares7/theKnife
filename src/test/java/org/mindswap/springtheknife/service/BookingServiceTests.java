package org.mindswap.springtheknife.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mindswap.springtheknife.Enum.BookingStatus;
import org.mindswap.springtheknife.converter.BookingConverter;
import org.mindswap.springtheknife.dto.booking.BookingCreateDto;
import org.mindswap.springtheknife.dto.booking.BookingGetDto;
import org.mindswap.springtheknife.dto.booking.BookingPatchDto;
import org.mindswap.springtheknife.exceptions.booking.BookingNotFoundException;
import org.mindswap.springtheknife.exceptions.booking.OperationNotAllowedException;
import org.mindswap.springtheknife.exceptions.restaurant.RestaurantNotFoundException;
import org.mindswap.springtheknife.exceptions.restaurantType.RestaurantTypeNotFoundException;
import org.mindswap.springtheknife.exceptions.user.UserNotFoundException;
import org.mindswap.springtheknife.model.Booking;
import org.mindswap.springtheknife.model.City;
import org.mindswap.springtheknife.model.Restaurant;
import org.mindswap.springtheknife.model.User;
import org.mindswap.springtheknife.repository.BookingRepository;
import org.mindswap.springtheknife.service.booking.BookingService;
import org.mindswap.springtheknife.service.booking.BookingServiceImpl;
import org.mindswap.springtheknife.service.restaurant.RestaurantServiceImpl;
import org.mindswap.springtheknife.service.restauranttype.RestaurantTypeServiceImpl;
import org.mindswap.springtheknife.service.user.UserServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingServiceTests {

    private static ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private BookingRepository bookingRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;
    @Mock
    private BookingConverter bookingConverter;
    @Mock
    private RestaurantServiceImpl restaurantService;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private RestaurantTypeServiceImpl restaurantTypeService;
    @Mock
    private BookingStatus CONFIRMED;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @AfterEach
    void deleteData() {
        bookingRepository.deleteAll();
        bookingRepository.resetId();
    }

    @Test
    public void testAllBookings() {
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";
        List<Booking> booking = IntStream.range(0, pageSize)
                .mapToObj(i -> {
                    Booking booking1 = new Booking();
                    User user = new User();
                    City city = Mockito.mock(City.class);
                    Mockito.when(city.getName()).thenReturn("Test City");
                    Restaurant restaurant = Mockito.mock(Restaurant.class);
                    Mockito.when(restaurant.getCity()).thenReturn(city);
                    booking1.setRestaurant(restaurant);
                    booking1.setBookingTime(LocalDateTime.now());
                    booking1.setStatus(CONFIRMED);
                    booking1.setUser(user);
                    return booking1; // Adicione esta linha para corrigir o erro de inferência de tipo
                })
                .collect(Collectors.toList());

        Page<Booking> pageBooking = new PageImpl<>(booking);
        when(bookingRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, sortBy)))
                .thenReturn(pageBooking);

        List<BookingGetDto> result = bookingService.getAllBookings(pageNumber, pageSize, sortBy);

        assertEquals(pageSize, result.size());
    }


    @Test
    void testDeleteBooking() throws BookingNotFoundException {
        long bookingId = 1L;
        Booking booking = new Booking();

        BookingRepository bookingRepository = mock(BookingRepository.class);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        BookingService bookingService = new BookingServiceImpl(bookingRepository, userService, restaurantService);

        assertDoesNotThrow(() -> bookingService.deleteBooking(bookingId));

        verify(bookingRepository, times(1)).findById(bookingId);
        verify(bookingRepository, times(1)).deleteById(bookingId);
    }

    @Test
    void testAddBooking() throws UserNotFoundException, RestaurantNotFoundException, RestaurantTypeNotFoundException {
        BookingCreateDto bookingCreateDto = new BookingCreateDto(1L, 1L, LocalDateTime.now(), BookingStatus.CONFIRMED);

        User user = new User();
        when(userService.getUserById(1L)).thenReturn(user);

        City city = new City();
        city.setId(1L);
        city.setName("Test City");

        Restaurant restaurant = Mockito.mock(Restaurant.class);
        when(restaurant.getCity()).thenReturn(city);
        when(restaurantService.getById(1L)).thenReturn(restaurant);

        BookingGetDto result = bookingService.addBooking(bookingCreateDto);

        assertNotNull(result);

        verify(bookingRepository, times(1)).save(any(Booking.class));
        verify(userService, times(1)).getUserById(1L);
        verify(restaurantService, times(1)).getById(1L);
    }

    @Test
    void testGetBookingById() throws BookingNotFoundException, UserNotFoundException, RestaurantNotFoundException {
        long bookingId = 1L;

        User user = new User();
        user.setId(1L);
        when(userService.getUserById(1L)).thenReturn(user);

        City city = new City();
        city.setId(1L);
        city.setName("Test City");

        Restaurant restaurant = Mockito.mock(Restaurant.class);
        when(restaurant.getCity()).thenReturn(city);
        when(restaurantService.getById(1L)).thenReturn(restaurant);

        Booking existingBooking = new Booking();
        existingBooking.setId(bookingId);
        existingBooking.setUser(user);
        existingBooking.setRestaurant(restaurant);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(existingBooking));

        BookingGetDto result = bookingService.getBookingById(bookingId);

        assertEquals(existingBooking.getId(), result.id());
        assertEquals(user.getId(), result.user().userId());

        verify(bookingRepository, times(1)).findById(bookingId);
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void testGetBookingByIdNotFound() {

        long bookingId = 1L;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> {
            bookingService.getBookingById(bookingId);
        });

        verify(bookingRepository, times(1)).findById(bookingId);
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void testPatchBooking() throws BookingNotFoundException, RestaurantNotFoundException, UserNotFoundException, OperationNotAllowedException {
        long bookingId = 1L;

        User user = new User();
        user.setId(1L);
        when(userService.getUserById(1L)).thenReturn(user);

        City city = new City();
        city.setId(1L);
        city.setName("Test City");

        Restaurant restaurant = Mockito.mock(Restaurant.class);
        when(restaurant.getCity()).thenReturn(city);
        when(restaurantService.getById(1L)).thenReturn(restaurant);

        Booking existingBooking = new Booking();
        existingBooking.setId(bookingId);
        existingBooking.setUser(user);
        existingBooking.setRestaurant(restaurant);
        existingBooking.setBookingTime(LocalDateTime.now().minusHours(1));
        existingBooking.setStatus(BookingStatus.CONFIRMED);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(existingBooking));

        BookingPatchDto patchDto = new BookingPatchDto(LocalDateTime.now(), BookingStatus.CONFIRMED);

        when(bookingRepository.save(any())).thenReturn(existingBooking);

        BookingGetDto result = bookingService.patchBooking(bookingId, patchDto);

        assertNotNull(result);
        assertEquals(bookingId, result.id());
        assertEquals(BookingStatus.CONFIRMED, result.status());

        verify(bookingRepository, times(1)).findById(bookingId);
        verify(bookingRepository, times(1)).save(existingBooking);
        verifyNoMoreInteractions(bookingRepository);
    }
}