package org.mindswap.springtheknife.dto.userexperience;

import org.mindswap.springtheknife.dto.restaurant.RestaurantGetDto;
import org.mindswap.springtheknife.dto.user.UserGetDto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record UserExperienceGetDto(

        Long id,

        Long bookingId,

        UserGetDto user,

        RestaurantGetDto restaurant,

        double rating,

        String comment,

        LocalDateTime timestamp

)implements Serializable {


}

