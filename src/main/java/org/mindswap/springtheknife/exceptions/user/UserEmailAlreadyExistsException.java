package org.mindswap.springtheknife.exceptions.user;

public class UserEmailAlreadyExistsException extends Exception {

    public UserEmailAlreadyExistsException(String message){

        super (message);
    }
}
