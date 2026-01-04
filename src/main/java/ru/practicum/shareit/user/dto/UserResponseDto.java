package ru.practicum.shareit.user.dto;


import ru.practicum.shareit.user.enums.UserRole;

public record UserResponseDto(Long id, String name, String email, UserRole role) {
}

