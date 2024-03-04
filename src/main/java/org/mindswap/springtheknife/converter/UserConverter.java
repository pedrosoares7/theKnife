package org.mindswap.springtheknife.converter;


import org.mindswap.springtheknife.dto.user.UserCreateDto;
import org.mindswap.springtheknife.dto.user.UserGetDto;
import org.mindswap.springtheknife.dto.user.UserPatchDto;
import org.mindswap.springtheknife.model.Restaurant;
import org.mindswap.springtheknife.model.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserConverter {

    public static User fromCreateDtoToEntity(UserCreateDto userCreateDto, Set<Restaurant> favoriteRestaurants) {
        return  User.builder()
                .userName(userCreateDto.userName())
                .password(userCreateDto.password())
                .firstName(userCreateDto.firstName())
                .email(userCreateDto.email())
                .lastName(userCreateDto.lastName())
                .dateOfBirth(userCreateDto.dateOfBirth())
                .favoriteRestaurants(favoriteRestaurants)
                .build();
            }


    public static UserGetDto fromEntityToGetDto(User user) {
        return new UserGetDto(
                user.getId(),
                user.getUserName(),
                user.getFavoriteRestaurants().stream()
                        .map(RestaurantConverter::fromModelToRestaurantDto)
                        .collect(Collectors.toSet())
        );
    }

    public static UserPatchDto fromEntityToPatchDto(User user) {
        return new UserPatchDto(
                user.getUserName(),
                user.getPassword(),
                user.getEmail()
        );
    }

    /*public static UserCreateDto fromEntityToCreateDto(User user) {
        return new UserCreateDto(
                user.getUserName(),
                user.getPassword(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getDateOfBirth()

        );
    }*/


    public static User fromGetDtoToEntity(UserGetDto user) {
        return  User.builder()
                .id(user.userId())
                .userName(user.userName())
                .build();
    }

    /*public static UserGetDto fromCreateDtoToGetDto(UserCreateDto userCreateDto){
        User tempUser = fromCreateDtoToEntity(userCreateDto);
        return fromEntityToGetDto(tempUser);
    }*/
    }

