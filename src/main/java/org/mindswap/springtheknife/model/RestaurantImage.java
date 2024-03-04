package org.mindswap.springtheknife.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mindswap.springtheknife.utils.ImageApiHandler;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private byte[] images;

    @Setter
    private String imagePath;

    @Setter
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;


    public byte[] convertToByteArray(String prompt) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(ImageApiHandler.getImageDataFromAPI(prompt), byte[].class);
    }

    public String createImageFile(Long id) throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(images);
        String filePath = String.format("src/main/imagefiles/%s/", id);

        File file = new File(filePath);
        file.mkdirs();
        filePath = filePath + "/rest_default.png";

        ImageIO.write(ImageIO.read(stream), "png", new File(filePath));
        return filePath;
    }

}
