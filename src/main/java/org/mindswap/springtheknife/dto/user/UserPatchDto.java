package org.mindswap.springtheknife.dto.user;

import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.mindswap.springtheknife.utils.Message;

import java.io.Serializable;
import static org.mindswap.springtheknife.utils.Message.*;

public record UserPatchDto(

    @Pattern(regexp = USERNAME_VALIDATOR, message = Message.VALID_USERNAME)
    String userName,

    @Pattern(regexp = PASSWORD_VALIDATOR, message = Message.VALID_PASSWORD)
    String password,
    @Email(message = VALID_EMAIL)
    @Pattern(regexp = EMAIL_VALIDATOR)
    String email
    
) implements Serializable {

}

