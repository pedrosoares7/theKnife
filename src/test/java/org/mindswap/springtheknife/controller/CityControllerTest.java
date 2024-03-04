package org.mindswap.springtheknife.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindswap.springtheknife.dto.city.CityDto;
import org.mindswap.springtheknife.dto.city.CityGetDto;
import org.mindswap.springtheknife.exceptions.city.CityNotFoundException;
import org.mindswap.springtheknife.model.City;
import org.mindswap.springtheknife.repository.CityRepository;
import org.mindswap.springtheknife.service.city.CityServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CityControllerTest {

    private static ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CityRepository cityRepository;

    @MockBean
    private CityServiceImpl cityServiceImpl;

    @InjectMocks
    private CityController cityController;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @AfterEach
    void deleteData() {
        cityRepository.deleteAll();
        cityRepository.resetId();
    }

    @Test
    void contextLoads() {

    }

    @Test
    @DisplayName("Test get all Cities when Cities on database returns empty list")
    void testGetAllCitiesWhenNoCitiesOnDatabaseReturnsEmptyList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cities/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(0)));

    }

    @Test
    void testGetAllCities() throws Exception {
        List<CityGetDto> cities = new ArrayList<>();

        cities.add(new CityGetDto(1L, "Porto", null));
        cities.add(new CityGetDto(2L, "Braga", null));

        when(cityServiceImpl.getAllCities(0, 2, "name")).thenReturn(cities);

        mockMvc.perform(get("/api/v1/cities/")
                        .param("pageNumber", "0")
                        .param("pageSize", "2")
                        .param("sortBy", "name"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Porto")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Braga")));

        verify(cityServiceImpl, times(1)).getAllCities(0, 2, "name");
    }

    @Test
    void testGetCity() throws Exception {

        CityGetDto city = new CityGetDto(1L, "Porto", null);

        when(cityServiceImpl.getCity(1L)).thenReturn(city);

        mockMvc.perform(get("/api/v1/cities/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Porto")));

        verify(cityServiceImpl, times(1)).getCity(1L);
    }

    @Test
    void testAddNewCity() throws Exception {
        CityDto city = new CityDto("Porto");

        when(cityServiceImpl.createCity(city)).thenReturn(new CityDto("Porto"));

        mockMvc.perform(post("/api/v1/cities/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(city)))
                .andExpect(status().isCreated());

        verify(cityServiceImpl, times(1)).createCity(any(CityDto.class));
    }

    @Test
    void testUpdateCity() throws CityNotFoundException {

        CityServiceImpl cityServiceMock = mock(CityServiceImpl.class);
        CityController cityController = new CityController(cityServiceMock);

        long cityId = 1L;
        City updatedCity = new City(1L, "Porto", null);

        ResponseEntity<String> response = cityController.patchCity(updatedCity, cityId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cityServiceMock).updateCity(eq(cityId), any(City.class));
    }

    @Test
    void testDeleteCity() throws Exception {

        doNothing().when(cityServiceImpl).deleteCity(any(Long.class));

        mockMvc.perform(delete("/api/v1/cities/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(cityServiceImpl, times(1)).deleteCity(any(Long.class));
    }
}








