package org.mindswap.springtheknife.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.mindswap.springtheknife.dto.city.CityDto;
import org.mindswap.springtheknife.dto.city.CityGetDto;
import org.mindswap.springtheknife.exceptions.city.CityNotFoundException;
import org.mindswap.springtheknife.exceptions.city.CityAlreadyExistsException;
import org.mindswap.springtheknife.model.City;
import org.mindswap.springtheknife.repository.CityRepository;
import org.mindswap.springtheknife.service.city.CityServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class CityServiceTests {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityServiceImpl cityService;

    private static ObjectMapper objectMapper;

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
    public void testGetCities() throws Exception {
        List<City> cities = new ArrayList<>();
        Page<City> pageCity = new PageImpl<>(cities);

        when(cityRepository.findAll(any(Pageable.class))).thenReturn(pageCity);

        List<CityGetDto> result = cityService.getAllCities(1, 3, "asc");

        assertEquals(cities.size(), result.size());
    }


    @Test
    public void testDelete() throws CityNotFoundException {
        long cityId = 1L;

        when(cityRepository.existsById(cityId)).thenReturn(true);

        cityService.deleteCity(cityId);

        verify(cityRepository, times(1)).deleteById(cityId);
    }

    @Test
    void testCreateCityAndConverter() throws CityAlreadyExistsException {
        CityDto existingCityDto = new CityDto("Existing City");

        when(cityRepository.findByName(existingCityDto.name())).thenReturn(Optional.of(new City()));

        assertThrows(CityAlreadyExistsException.class, () -> {
            cityService.createCity(existingCityDto);
        });

        verify(cityRepository, never()).save(any(City.class));
    }

    @Test
    void createCityWithDuplicatedNameThrowsException() {
        CityDto cityDto = new CityDto("Porto");
        Mockito.when(this.cityRepository.findByName(cityDto.name())).thenReturn(Optional.of(new City()));
        assertThrows(CityAlreadyExistsException.class, () -> {
            this.cityService.createCity(cityDto);
        });
        Assertions.assertEquals("City with name Porto already exists", ((CityAlreadyExistsException) assertThrows(CityAlreadyExistsException.class, () -> {
            this.cityService.createCity(cityDto);
        })).getMessage());
    }

    @Test
    void testUpdateCity() throws CityNotFoundException {
        long cityId = 1L;
        City existingCity = new City();
        City updatedCity = new City();

        when(cityRepository.findById(cityId)).thenReturn(Optional.of(existingCity));
        when(cityRepository.save(existingCity)).thenReturn(updatedCity);

        cityService.updateCity(cityId, updatedCity);

        assertEquals(updatedCity.getName(), existingCity.getName());

        verify(cityRepository, times(1)).findById(cityId);
        verify(cityRepository, times(1)).save(existingCity);
        verifyNoMoreInteractions(cityRepository);
    }

    @Test
    void testGetCityById() throws CityNotFoundException {
        long cityId = 1L;
        City existingCity = new City();

        when(cityRepository.findById(cityId)).thenReturn(Optional.of(existingCity));

        City result = cityService.getCityById(cityId);

        assertEquals(existingCity, result);

        verify(cityRepository, times(1)).findById(cityId);

        verifyNoMoreInteractions(cityRepository);
    }

    @Test
    void testGetCityByIdNotFoundThrowsException() {
        long nonExistingCityId = 2L;

        when(cityRepository.findById(nonExistingCityId)).thenReturn(Optional.empty());

        assertThrows(CityNotFoundException.class, () -> {
            cityService.getCityById(nonExistingCityId);
        });

        verify(cityRepository, times(1)).findById(nonExistingCityId);
        verifyNoMoreInteractions(cityRepository);
    }
}