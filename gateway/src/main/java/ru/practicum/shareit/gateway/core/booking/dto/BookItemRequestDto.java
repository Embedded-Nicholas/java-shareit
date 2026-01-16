package ru.practicum.shareit.gateway.core.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BookItemRequestDto(
        long itemId,
        @FutureOrPresent
        LocalDateTime start,
        @Future
        LocalDateTime end
) {
}