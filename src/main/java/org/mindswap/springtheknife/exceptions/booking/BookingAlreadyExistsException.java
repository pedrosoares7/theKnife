package org.mindswap.springtheknife.exceptions.booking;

public class BookingAlreadyExistsException extends Exception{
    public BookingAlreadyExistsException(String message){
        super(message);
    }
}
