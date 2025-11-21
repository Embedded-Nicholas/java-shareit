package ru.practicum.shareit.user.service;


import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.Collection;

public interface UserService {
    Collection<ItemResponseDto> getAllUserItems(Long userId);

    UserResponseDto getUserById(Long userId);

    UserResponseDto createUser(UserRequestDto userRequestDto);

    UserResponseDto updateUser(Long userId, UserUpdateDto userRequestDto);

    void deleteUser(Long userId);
}

