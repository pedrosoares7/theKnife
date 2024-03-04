package org.mindswap.springtheknife.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindswap.springtheknife.repository.RestaurantRepository;
import org.mindswap.springtheknife.service.restaurantimage.RestaurantImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RestaurantImageControllerTests {

    @MockBean
    private RestaurantImageService restaurantImageService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private RestaurantRepository restaurantRepository;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @AfterEach
    void init() {
        restaurantRepository.deleteAll();
        restaurantRepository.resetId();
    }

    @Test
    @DisplayName("Test file upload")
    void testFileUpload() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        doNothing().when(restaurantImageService).uploadFile(any(MultipartFile.class));

        mockMvc.perform(multipart("/api/v1/restaurants/img/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("File uploaded successfully"));
    }

    @Test
    @DisplayName("Test file upload with id")
    void testFileUploadWithId() throws Exception {
        Long restaurantId = 1L;
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        doNothing().when(restaurantImageService).uploadFileWithId(any(MultipartFile.class), any(Long.class));

        mockMvc.perform(multipart("/api/v1/restaurants/img/upload/{id}", restaurantId).file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("File uploaded successfully"));
    }

}