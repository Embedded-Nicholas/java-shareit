package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.exceptions.UserAlreadyExistsException;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> storage = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0);

    @Override
    public User save(User user) {
        boolean emailInUse = storage.values().stream()
                .anyMatch(existing -> existing.getEmail().equalsIgnoreCase(user.getEmail())
                        && !Objects.equals(existing.getId(), user.getId()));

        if (emailInUse) {
            throw new UserAlreadyExistsException("User already exists with email: " + user.getEmail());
        }

        if (user.getId() == null) {
            user.setId(idSequence.incrementAndGet());
        }

        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return storage.values().stream()
                .filter(u -> u.getEmail() != null && u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }

    @Override
    public Collection<User> findAll() {
        return new ArrayList<>(storage.values());
    }
}

