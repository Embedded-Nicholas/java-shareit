package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.enums.BookingStatus;

public record BookingUpdateDto(
        Long id,
        BookingStatus status
) {
}
