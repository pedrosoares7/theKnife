package org.mindswap.springtheknife.exceptions.restaurant;

public class RestaurantAlreadyExistsException extends Exception {
    public RestaurantAlreadyExistsException(String message) {
        super(message);
    }
}
