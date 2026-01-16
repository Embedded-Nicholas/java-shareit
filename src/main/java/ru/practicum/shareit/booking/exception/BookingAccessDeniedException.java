package ru.practicum.shareit.booking.exception;

import ru.practicum.shareit.exception.AccessDeniedException;

public class BookingAccessDeniedException extends AccessDeniedException {
    public BookingAccessDeniedException(Long userId, Long bookingId) {
        super(String.format("User %d is not the owner of item in booking %d", userId, bookingId));
    }
}
