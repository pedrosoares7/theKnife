package org.mindswap.springtheknife.service.user;

import org.mindswap.springtheknife.dto.user.UserCreateDto;
import org.mindswap.springtheknife.dto.user.UserGetDto;
import org.mindswap.springtheknife.dto.user.UserPatchDto;
import org.mindswap.springtheknife.exceptions.user.UserAlreadyExistsException;
import org.mindswap.springtheknife.exceptions.user.UserEmailAlreadyExistsException;
import org.mindswap.springtheknife.exceptions.user.UserNotFoundException;

import java.util.List;

public interface UserService {

    List<UserGetDto> getAllUsers(int pageNumber, int pageSize, String sortBy);

    UserGetDto getUser(Long id) throws UserNotFoundException;


   // UserGetDto getUserById(Long id) throws UserNotFoundException;

    UserGetDto createUser(UserCreateDto user) throws UserNotFoundException, UserAlreadyExistsException, UserEmailAlreadyExistsException;

    UserPatchDto updateUser(Long id, UserPatchDto user) throws UserNotFoundException, UserAlreadyExistsException;

    void deleteUser(Long id) throws UserNotFoundException;


}


