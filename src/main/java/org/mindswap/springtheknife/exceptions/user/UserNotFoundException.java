package org.mindswap.springtheknife.exceptions.user;

public class UserNotFoundException extends Exception  {
    public UserNotFoundException(String message) {
        super(message);
    }
}
