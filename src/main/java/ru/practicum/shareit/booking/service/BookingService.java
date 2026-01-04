package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface BookingService {
    BookingResponseDto createBooking(BookingCreateDto bookingCreateDto, Long userId);

    BookingResponseDto cancelBooking(Long bookingId, Long bookerId) throws AccessDeniedException;

    BookingResponseDto manageBooking(Long itemOwnerId, Long bookingId, Boolean approved) throws AccessDeniedException;

    BookingResponseDto getBooking(Long requesterId, Long bookingId) throws AccessDeniedException;

    List<BookingResponseDto> getBookingsByBooker(Long userId, String state, int from, int size);

    List<BookingResponseDto> getBookingsByOwner(Long userId, String state, int from, int size);
}
