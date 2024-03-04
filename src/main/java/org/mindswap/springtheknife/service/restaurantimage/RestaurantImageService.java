package org.mindswap.springtheknife.service.restaurantimage;

import org.mindswap.springtheknife.exceptions.restaurant.RestaurantNotFoundException;
import org.mindswap.springtheknife.model.Restaurant;
import org.mindswap.springtheknife.model.RestaurantImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface RestaurantImageService {

    RestaurantImage saveRestaurantImage(Restaurant restaurant) throws IOException;
    void uploadFile(MultipartFile file) throws Exception;
    void uploadFileWithId(MultipartFile file, Long id) throws Exception;
}
