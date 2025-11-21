package ru.practicum.shareit.request.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
public class Request {
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime createdAt;
}
