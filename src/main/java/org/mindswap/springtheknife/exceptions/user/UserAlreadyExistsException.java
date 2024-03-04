package org.mindswap.springtheknife.exceptions.user;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String message){

        super (message);
    }
}
