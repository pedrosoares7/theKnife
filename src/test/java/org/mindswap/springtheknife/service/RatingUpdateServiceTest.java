package org.mindswap.springtheknife.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindswap.springtheknife.model.Restaurant;
import org.mindswap.springtheknife.repository.RestaurantRepository;
import org.mindswap.springtheknife.service.rating.RatingUpdateService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
public class RatingUpdateServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RatingUpdateService ratingUpdateService;

    @Test
    @DisplayName("Test updateRestaurantRatings")
    public void testUpdateRestaurantRatings() {
        // Arrange
        Restaurant restaurant1 = Mockito.mock(Restaurant.class);
        Restaurant restaurant2 = Mockito.mock(Restaurant.class);
        List<Restaurant> allRestaurants = Arrays.asList(restaurant1, restaurant2);

        when(restaurantRepository.findAll()).thenReturn(allRestaurants);
        when(restaurantRepository.findAverageRating(anyLong())).thenReturn(4.0, 3.5);

        // Act
        ratingUpdateService.updateRestaurantRatings();

        // Assert
        verify(restaurantRepository, times(1)).findAll();
        verify(restaurantRepository, times(2)).findAverageRating(restaurant1.getId()); // Expect 2 invocations
        verify(restaurantRepository, times(2)).findAverageRating(restaurant2.getId()); // Expect 2 invocations
        verify(restaurant1, times(1)).setRating(4.0);
        verify(restaurant2, times(1)).setRating(3.5);
        verify(restaurantRepository, times(1)).save(restaurant1);
        verify(restaurantRepository, times(1)).save(restaurant2);
    }
}