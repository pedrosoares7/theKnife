package org.mindswap.springtheknife.exceptions.restaurantType;

public class RestaurantTypeAlreadyExistsException extends Exception {
    public RestaurantTypeAlreadyExistsException (String message){
        super(message);
    }
}
