package ru.practicum.shareit.user.repository;


import ru.practicum.shareit.user.entity.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    void deleteById(Long id);

    Collection<User> findAll();
}

