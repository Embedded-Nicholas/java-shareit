package ru.practicum.shareit.item.dto;

import lombok.Builder;
import ru.practicum.shareit.comment.dto.CommentDto;

import java.time.LocalDateTime;
import java.util.Collection;

@Builder
public record ItemResponseDto(
        Long id,
        String name,
        String description,
        Boolean available,
        Long ownerId,
        Collection<CommentDto> comments,
        LocalDateTime lastBooking,
        LocalDateTime nextBooking) { }

