package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import ru.practicum.shareit.user.enums.UserRole;

public record UserRequestDto(String name, String password, @NotBlank @Email String email, UserRole role) {
}

