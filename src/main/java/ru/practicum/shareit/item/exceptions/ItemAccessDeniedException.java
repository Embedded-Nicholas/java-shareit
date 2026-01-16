package ru.practicum.shareit.item.exceptions;

import ru.practicum.shareit.exception.AccessDeniedException;

public class ItemAccessDeniedException extends AccessDeniedException {
    public ItemAccessDeniedException(String message) {
        super(message);
    }
}
