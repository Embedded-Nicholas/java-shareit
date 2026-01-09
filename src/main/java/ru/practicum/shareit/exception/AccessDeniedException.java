package ru.practicum.shareit.exception;

public abstract class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
