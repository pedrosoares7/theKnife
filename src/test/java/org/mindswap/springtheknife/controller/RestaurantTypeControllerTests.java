package org.mindswap.springtheknife.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindswap.springtheknife.dto.restaurantTypeDto.RestaurantTypeDto;
import org.mindswap.springtheknife.repository.RestaurantTypeRepository;
import org.mindswap.springtheknife.service.restauranttype.RestaurantTypeServiceImpl;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RestaurantTypeControllerTests {

    private static ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RestaurantTypeRepository restaurantTypeRepository;
    @MockBean
    private RestaurantTypeServiceImpl restaurantTypeService;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @AfterEach
    void deleteData() {
        restaurantTypeRepository.deleteAll();
        restaurantTypeRepository.resetId();
    }

    @Test
    void contextLoads() {

    }


    @Test
    @DisplayName("Test to get all restaurant types")
    public void testGetAllRestaurantTypes() throws Exception {
        mockMvc.perform(get("/api/v1/restaurantTypes/")
                        .param("pageNumber", "0")
                        .param("pageSize", "5")
                        .param("sortBy", "id"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Test to get restaurant type by id")
    public void testGetRestaurantTypeById() throws Exception {

        RestaurantTypeDto restaurantType = new RestaurantTypeDto(1L, "ItalianFood");

        when(restaurantTypeService.getRestaurantTypeById(1L)).thenReturn(restaurantType);

        mockMvc.perform(get("/api/v1/restaurantTypes/" + restaurantType.id()))
                .andExpect(status().isOk());

        verify(restaurantTypeService).getRestaurantTypeById(restaurantType.id());
    }

    @Test
    @DisplayName("Test to get restaurant type by name")
    public void testAddType() throws Exception {

        RestaurantTypeDto restaurantType = new RestaurantTypeDto(1L, "ItalianFood");

        when(restaurantTypeService.addRestaurantType(restaurantType)).thenReturn(restaurantType);

        mockMvc.perform(post("/api/v1/restaurantTypes/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantType)))
                .andExpect(status().isCreated());

        verify(restaurantTypeService).addRestaurantType(restaurantType);
    }

    @Test
    @DisplayName("Test to update restaurant type")
    public void testPatchType() throws Exception {

        RestaurantTypeDto restaurantType = new RestaurantTypeDto(1L, "ItalianFood");

        when(restaurantTypeService.patchRestaurantType(restaurantType.id(), restaurantType)).thenReturn(restaurantType);

        mockMvc.perform(patch("/api/v1/restaurantTypes/" + restaurantType.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantType)))
                .andExpect(status().isOk());

        verify(restaurantTypeService).patchRestaurantType(restaurantType.id(), restaurantType);
    }

    @Test
    @DisplayName("Test to delete restaurant type")
    public void testDeleteType() throws Exception {

        Long id = 1L;

        doNothing().when(restaurantTypeService).deleteRestaurantType(id);

        mockMvc.perform(delete("/api/v1/restaurantTypes/" + id))
                .andExpect(status().isOk());

        verify(restaurantTypeService).deleteRestaurantType(id);
    }
}