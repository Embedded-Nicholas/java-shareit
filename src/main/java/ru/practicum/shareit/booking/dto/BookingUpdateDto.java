package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.enums.BookingStatus;

import java.time.LocalDateTime;

public record BookingUpdateDto(
        Long id,
        BookingStatus status
) { }
