package ru.practicum.shareit.user.entity;

import lombok.*;
import ru.practicum.shareit.user.enums.UserRole;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @ToString.Include
    @EqualsAndHashCode.Include
    private Long id;
    @ToString.Include
    private String name;
    private String password;
    @ToString.Include
    private String email;
    private UserRole role;
}

