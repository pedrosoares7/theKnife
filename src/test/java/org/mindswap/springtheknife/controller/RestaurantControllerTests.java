package org.mindswap.springtheknife.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindswap.springtheknife.dto.restaurant.RestaurantGetDto;
import org.mindswap.springtheknife.dto.restaurant.RestaurantPatchDto;
import org.mindswap.springtheknife.dto.restaurant.RestaurantPostDto;
import org.mindswap.springtheknife.model.Address;
import org.mindswap.springtheknife.repository.RestaurantRepository;
import org.mindswap.springtheknife.service.restaurant.RestaurantServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class RestaurantControllerTests {

    @MockBean
    RestaurantRepository restaurantRepository;
    @MockBean
    RestaurantServiceImpl restaurantService;
    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @Test
    @AfterEach
    void init() {
        restaurantRepository.deleteAll();
        restaurantRepository.resetId();
    }


    @Test
    @DisplayName("Test get all restaurants when database is empty")
    void testGetAllRestaurantsWhenDatabaseIsEmpty() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/restaurants/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Test get all restaurants when database has 1 user")
    void testGetAllRestaurantsWhenDatabaseHas1User() throws Exception {

        List<RestaurantGetDto> restaurants = new ArrayList<>();
        restaurants.add(new RestaurantGetDto("Porto", "Pizza", "pizza@ge.com", new Address(), "+351219879876", 0.0, new HashSet<>()));


        when(restaurantService.getAllRestaurants(0, 5, "name")).thenReturn(restaurants);


        mockMvc.perform(get("/api/v1/restaurants/")
                        .param("pageNumber", "0")
                        .param("pageSize", "5")
                        .param("sortBy", "name"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Pizza")));
    }

    @Test
    @DisplayName("Test get a restaurant by id")
    void testGetRestaurantById() throws Exception {
        when(restaurantService.getRestaurant(1L)).thenReturn(new RestaurantGetDto("Porto", "Pizza", "pizza@ge.com", new Address(), "+351219879876", 0.0, new HashSet<>()));

        mockMvc.perform(get("/api/v1/restaurants/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Pizza")));
    }

    @Test
    @DisplayName("Test get a restaurant by id when restaurant does not exist")
    void testGetRestaurantByIdWhenRestaurantDoesNotExist() throws Exception {
        //Given
        RestaurantPostDto newRestaurant = new RestaurantPostDto("Pizza", new Address(), "pizza@ge.com", "+351219879876", 8.123, -9.32, 1L, new HashSet<>());

        //When
        when(restaurantService.addRestaurant(any(RestaurantPostDto.class))).thenReturn(new RestaurantGetDto("Porto", "Pizza", "pizza@ge.com", new Address(), "+351219879876", 0.0, new HashSet<>()));

        mockMvc.perform(post("/api/v1/departments/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newRestaurant)));

        //Then
        mockMvc.perform(get("/api/v1/departments/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test adding a restaurant to the database")
    void testAddRestaurant() throws Exception {

        //Given
        RestaurantPostDto newRestaurant = new RestaurantPostDto("Pizza", new Address(), "pizza@ge.com", "+351219879876", 8.123, -9.32, 1L, new HashSet<>());

        //When
        when(restaurantService.addRestaurant(any(RestaurantPostDto.class))).thenReturn(new RestaurantGetDto("Porto", "Pizza", "pizza@ge.com", new Address(), "+351219879876", 0.0, new HashSet<>()));

        //Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/restaurants/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newRestaurant)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Pizza")));
    }

    @Test
    @DisplayName("Test adding a list of restaurants to the database")
    void testAddListOfRestaurants() throws Exception {

        //Given
        List<RestaurantPostDto> restaurantsPost = new ArrayList<>();
        restaurantsPost.add(new RestaurantPostDto("Pizza", new Address(), "pizza@ge.com", "+351219879876", 8.123, -9.32, 1L, new HashSet<>()));
        restaurantsPost.add(new RestaurantPostDto("Sushi", new Address(), "sushi@ge.com", "+351219823476", 8.345, -9.56, 2L, new HashSet<>()));

        List<RestaurantGetDto> restaurantsGet = new ArrayList<>();
        restaurantsGet.add(new RestaurantGetDto("Porto", "Pizza", "pizza@ge.com", new Address(), "+351219879876", 0.0, new HashSet<>()));
        restaurantsGet.add(new RestaurantGetDto("Lisbon", "Sushi", "sushi@ge.com", new Address(), "+351219823476", 0.0, new HashSet<>()));

        //When
        when(restaurantService.addListOfRestaurants(any(List.class))).thenReturn(restaurantsGet);

        //Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/restaurants/list/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(restaurantsPost)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Pizza")))
                .andExpect(jsonPath("$[1].name", is("Sushi")));

    }

    @Test
    @DisplayName("Test delete a restaurant by id")
    void testDeleteRestaurantById() throws Exception {

        doNothing().when(restaurantService).deleteRestaurant(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/restaurants/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Restaurant with id 1 deleted successfully."));
    }

    @Test
    @DisplayName("Test patch a restaurant by id")
    void testPatchRestaurantById() throws Exception {
        String requestBody = """
                {
                  "address": {
                    "street": "rua",
                    "number": "33",
                    "zipCode": "4100"
                  },
                  "email": "6@asdasd.pt"
                }
                """;

        RestaurantGetDto patchedRestaurant = new RestaurantGetDto("Porto", "Pizza", "6@asdasd.pt", new Address("rua", "33", "4100"), "+351219879876", 0.0, new HashSet<>());

        when(restaurantService.patchRestaurant(any(Long.class), any(RestaurantPatchDto.class))).thenReturn(patchedRestaurant);

        mockMvc.perform(patch("/api/v1/restaurants/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        verify(restaurantService, times(1)).patchRestaurant(any(Long.class), any(RestaurantPatchDto.class));
    }

    @Test
    @DisplayName("Test get average rating of a restaurant")
    void testGetAverageRating() throws Exception {
        Long restaurantId = 1L;
        Double expectedAverageRating = 4.5;

        when(restaurantService.findAverageRating(restaurantId)).thenReturn(expectedAverageRating);

        mockMvc.perform(get("/api/v1/restaurants/{id}/averageRating", restaurantId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(expectedAverageRating.toString()));
    }
}