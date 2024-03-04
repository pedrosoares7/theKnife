package org.mindswap.springtheknife.aspects;

import org.mindswap.springtheknife.exceptions.booking.BookingAlreadyExistsException;
import org.mindswap.springtheknife.exceptions.booking.BookingNotFoundException;
import org.mindswap.springtheknife.exceptions.booking.OperationNotAllowedException;
import org.mindswap.springtheknife.exceptions.city.CityAlreadyExistsException;
import org.mindswap.springtheknife.exceptions.city.CityNotFoundException;
import org.mindswap.springtheknife.exceptions.restaurant.RestaurantAlreadyExistsException;
import org.mindswap.springtheknife.exceptions.restaurant.RestaurantNotFoundException;
import org.mindswap.springtheknife.exceptions.restaurantType.RestaurantTypeAlreadyExistsException;
import org.mindswap.springtheknife.exceptions.restaurantType.RestaurantTypeNotFoundException;
import org.mindswap.springtheknife.exceptions.user.UserAlreadyExistsException;
import org.mindswap.springtheknife.exceptions.user.UserEmailAlreadyExistsException;
import org.mindswap.springtheknife.exceptions.user.UserNotFoundException;
import org.mindswap.springtheknife.exceptions.userexperience.UserExperienceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {BookingNotFoundException.class, CityNotFoundException.class,
            RestaurantNotFoundException.class, RestaurantTypeNotFoundException.class,UserNotFoundException.class, UserExperienceNotFoundException.class})
    public ResponseEntity<String> NotFoundHandler(Exception ex) {
        logger.error("Known Exception: " + ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {BookingAlreadyExistsException.class, CityAlreadyExistsException.class,
            RestaurantAlreadyExistsException.class, RestaurantTypeAlreadyExistsException.class, UserAlreadyExistsException.class, UserEmailAlreadyExistsException.class,
            UserEmailAlreadyExistsException.class})
    public ResponseEntity<String> AlreadyExistsHandler(Exception ex) {
        logger.error("Known Exception: " + ex);
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {OperationNotAllowedException.class})
    public ResponseEntity<String> NotAllowedHandler(Exception ex) {
        logger.error("Known Exception: " + ex);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }
}
