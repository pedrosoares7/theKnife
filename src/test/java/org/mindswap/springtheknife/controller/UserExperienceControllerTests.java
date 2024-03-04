package org.mindswap.springtheknife.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindswap.springtheknife.dto.restaurant.RestaurantGetDto;
import org.mindswap.springtheknife.dto.restaurantTypeDto.RestaurantTypeDto;
import org.mindswap.springtheknife.dto.user.UserGetDto;
import org.mindswap.springtheknife.dto.userexperience.UserExperienceCreateDto;
import org.mindswap.springtheknife.dto.userexperience.UserExperienceGetDto;
import org.mindswap.springtheknife.dto.userexperience.UserExperiencePatchDto;
import org.mindswap.springtheknife.model.Address;
import org.mindswap.springtheknife.repository.UserExperienceRepository;
import org.mindswap.springtheknife.service.userexperience.UserExperienceServiceImpl;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserExperienceControllerTests {

    private static ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserExperienceServiceImpl userExperienceService;

    @Mock
    private UserExperienceRepository userExperienceRepository;

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
    @DisplayName("Test get all UserExperience when users on database returns empty list")
    void testGetAllUserExperienceWhenNoUserExperienceOnDatabaseReturnsEmptyList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/userexperiences/")
                        .param("sortBy", "id"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Test get all UserExperience when users on database returns list of UserExperience")
    public void testGetAllUsersExperiences() throws Exception {


        RestaurantTypeDto restaurantType1 = new RestaurantTypeDto(
                1L,
                "Type1"
        );

        RestaurantTypeDto restaurantType2 = new RestaurantTypeDto(
                2L,
                "Type2"
        );

        RestaurantGetDto restaurant1 = new RestaurantGetDto(
                "city1",
                "restaurant1",
                "email1",
                new Address(),
                "Contact1",
                4.5,
                Set.of(restaurantType1)
        );

        RestaurantGetDto restaurant2 = new RestaurantGetDto(
                "city2",
                "restaurant2",
                "email2",
                new Address(),
                "Contact2",
                4.5,
                Set.of(restaurantType2)
        );

        UserGetDto user1 = new UserGetDto(
                1L,
                "User1",
                Set.of(restaurant1)
        );

        UserGetDto user2 = new UserGetDto(
                2L,
                "User2",
                Set.of(restaurant2)
        );

        UserExperienceGetDto userExperience1 = new UserExperienceGetDto(
                1L,
                1L,
                user1,
                restaurant1,
                4.5,
                "Great experience!",
                LocalDateTime.now()
        );

        UserExperienceGetDto userExperience2 = new UserExperienceGetDto(
                2L,
                2L,
                user2,
                restaurant2,
                5.0,
                "Excellent service!",
                LocalDateTime.now()
        );

        List<UserExperienceGetDto> userExperienceList = Arrays.asList(userExperience1, userExperience2);

        when(userExperienceService.getAllUsersExperiences(0, 5, "id")).thenReturn(userExperienceList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/userexperiences/")
                        .param("pageNumber", "0")
                        .param("pageSize", "5")
                        .param("sortBy", "id"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(userExperienceList.size()));
    }


    @Test
    @DisplayName("Test get UserExperience by id")
    public void testGetUserExperienceById() throws Exception {

        Long userExperienceId = 1L;

        RestaurantTypeDto restaurantType1 = new RestaurantTypeDto(
                1L,
                "Type1"
        );
        RestaurantGetDto restaurant1 = new RestaurantGetDto(
                "city1",
                "restaurant1",
                "email1",
                new Address(),
                "Contact1",
                4.5,
                Set.of(restaurantType1)
        );
        UserGetDto user1 = new UserGetDto(
                1L,
                "User1",
                Set.of(restaurant1)
        );
        UserExperienceGetDto expectedUserExperience = new UserExperienceGetDto(
                userExperienceId,
                1L,
                user1,
                restaurant1,
                4.5,
                "Great experience!",
                LocalDateTime.now()
        );

        when(userExperienceService.getUserExperienceById(userExperienceId)).thenReturn(expectedUserExperience);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/userexperiences/" + userExperienceId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedUserExperience.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rating").value(expectedUserExperience.rating()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").value(expectedUserExperience.comment()));
    }


    @Test
    @DisplayName("Test add UserExperience")
    public void testAddUserExperience() throws Exception {
        // Arrange
        UserExperienceCreateDto userExperienceCreateDto = new UserExperienceCreateDto(
                1L,
                1L,
                1L,
                4.5,
                "Great experience!"
        );

        Long userExperienceId = 1L;

        RestaurantTypeDto restaurantType1 = new RestaurantTypeDto(
                1L,
                "Type1"
        );
        RestaurantGetDto restaurant1 = new RestaurantGetDto(
                "City1",
                "Restaurant1",
                "Email1",
                new Address(),
                "Contact1",
                4.5,
                Set.of(restaurantType1)
        );
        UserGetDto user1 = new UserGetDto(
                1L,
                "User1",
                Set.of(restaurant1)
        );
        UserExperienceGetDto expectedUserExperience = new UserExperienceGetDto(
                userExperienceId,
                1L,
                user1,
                restaurant1,
                4.5,
                "Great experience!",
                LocalDateTime.now()
        );

        when(userExperienceService.addNewUserExperience(userExperienceCreateDto)).thenReturn(expectedUserExperience);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/userexperiences/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userExperienceCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.rating").value(expectedUserExperience.rating()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").value(expectedUserExperience.comment()));
    }

    @Test
    @DisplayName("Test delete UserExperience")
    public void testDeleteUserExperience() throws Exception {

        Long userExperienceId = 1L;

        doNothing().when(userExperienceService).deleteUserExperience(userExperienceId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/userexperiences/" + userExperienceId))
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("Test update UserExperience")
    public void testUpdateUserExperience() throws Exception {
        // Arrange
        Long userExperienceId = 1L;

        UserExperiencePatchDto userExperiencePatchDto = new UserExperiencePatchDto(
                4.5,
                "Great experience!"
        );

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/userexperiences/" + userExperienceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userExperiencePatchDto)))
                .andExpect(status().isOk());
    }
}
