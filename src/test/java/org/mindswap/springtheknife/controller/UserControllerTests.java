package org.mindswap.springtheknife.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindswap.springtheknife.dto.user.UserCreateDto;
import org.mindswap.springtheknife.dto.user.UserGetDto;
import org.mindswap.springtheknife.dto.user.UserPatchDto;
import org.mindswap.springtheknife.repository.UserRepository;
import org.mindswap.springtheknife.service.user.UserServiceImpl;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    private static ObjectMapper objectMapper;
    @Mock
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @AfterEach
    void deleteData() {
        userRepository.deleteAll();
        userRepository.resetId();
    }

    @Test
    void contextLoads() {

    }


    @Test
    @DisplayName("Test get all Users when users on database returns empty list")
    void testGetAllUsersWhenNoUsersOnDatabaseReturnsEmptyList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/")
                        .param("sortBy", "username"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Test get all Users when users on database returns list of users")
    public void testGetAllUsers() throws Exception {

        List<UserGetDto> users = new ArrayList<>();
        users.add(new UserGetDto(1L, "username1", new HashSet<>()));
        users.add(new UserGetDto(2L, "username2", new HashSet<>()));


        when(userService.getAllUsers(0, 5, "username")).thenReturn(users);


        mockMvc.perform(get("/api/v1/users/")
                        .param("pageNumber", "0")
                        .param("pageSize", "5")
                        .param("sortBy", "username"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userId", is(1)))
                .andExpect(jsonPath("$[0].userName", is("username1")))
                .andExpect(jsonPath("$[1].userId", is(2)))
                .andExpect(jsonPath("$[1].userName", is("username2")));

        verify(userService, times(1)).getAllUsers(0, 5, "username");
    }

    @Test
    @DisplayName("Test get User by Id")
    public void testGetUser() throws Exception {

        UserGetDto user = new UserGetDto(1L, "username1", new HashSet<>());

        when(userService.getUser(1L)).thenReturn(user);


        mockMvc.perform(get("/api/v1/users/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.userName", is("username1")));

        verify(userService, times(1)).getUser(1L);
    }

    @Test
    @DisplayName("Test create User")
    public void testCreateUser() throws Exception {

        UserCreateDto user = new UserCreateDto("existingUsername", "password", "email@example.com", "firstName", "lastName", LocalDate.of(2000, 2, 2), new HashSet<>());

        UserGetDto userGetDto = new UserGetDto(1L, "existingUsername", new HashSet<>());

        when(userService.createUser(any(UserCreateDto.class))).thenReturn(userGetDto);

        mockMvc.perform(post("/api/v1/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());

        verify(userService, times(1)).createUser(any(UserCreateDto.class));
    }


    @Test
    @DisplayName("Test update User")
    public void testPatchUser() throws Exception {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userName", "updatedUsername");

        UserPatchDto userPatchDto = new UserPatchDto("updatedUsername", "password", "email@example.com");

        when(userService.updateUser(any(Long.class), any(UserPatchDto.class))).thenReturn(userPatchDto);

        mockMvc.perform(patch("/api/v1/users/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUser(any(Long.class), any(UserPatchDto.class));
    }

    @Test
    @DisplayName("Test delete User")
    public void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(any(Long.class));

        mockMvc.perform(delete("/api/v1/users/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(any(Long.class));
    }

}