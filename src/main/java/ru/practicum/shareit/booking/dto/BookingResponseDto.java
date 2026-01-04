package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.practicum.shareit.booking.enums.BookingStatus;

import java.time.LocalDateTime;

public record BookingResponseDto(
        Long id,
        BookingStatus status,
        @JsonProperty("start")
        LocalDateTime bookingStartDate,
        @JsonProperty("end")
        LocalDateTime bookingEndDate,
        ItemShortDto item,
        UserShortDto booker
) {
    public record ItemShortDto(Long id, String name) {}
    public record UserShortDto(Long id, String name) {}
}
