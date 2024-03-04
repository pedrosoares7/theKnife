package org.mindswap.springtheknife.dto.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.mindswap.springtheknife.utils.Message.*;

public record UserCreateDto (

     @NotBlank(message = USERNAME_MANDATORY)
     @Pattern(regexp = USERNAME_VALIDATOR, message = VALID_USERNAME)
     String userName,
     @NotBlank(message = PASSWORD_MANDATORY)
     @Pattern(regexp = PASSWORD_VALIDATOR, message = VALID_PASSWORD)
     String password,
     @Email(message = EMAIL_MANDATORY)
     @Pattern(regexp = EMAIL_VALIDATOR, message = VALID_EMAIL)
     String email,

     @NotBlank(message = FIRSTNAME_MANDATORY)
     @Pattern(regexp = NAME_VALIDATOR, message = VALID_FIRSTNAME)
     String firstName,
     @NotBlank(message = LASTNAME_MANDATORY)
     @Pattern(regexp = NAME_VALIDATOR, message = VALID_LASTNAME)
     String lastName,

     @Valid
     @NotNull(message = DATE_OF_BIRTH_MANDATORY)
     @Past(message = VALID_DATE_OF_BIRTH)
     LocalDate dateOfBirth,

     Set<Long> favoriteRestaurants
) implements Serializable {

}
