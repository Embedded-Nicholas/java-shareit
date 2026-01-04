package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.file.AccessDeniedException;
import java.util.Collection;
import java.util.Collections;

/**
 * TODO Sprint add-controllers.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";


    @GetMapping("/{itemId}")
    public ItemResponseDto getItemById(@PathVariable("itemId") Long itemId) {
        return itemService.getItemById(itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto updateItemById(
            @RequestBody ItemUpdateDto updateDto,
            @PathVariable("itemId") Long itemId,
            @RequestHeader(value = ItemController.USER_ID_HEADER) Long userId) throws AccessDeniedException {
        return itemService.updateItem(itemId, updateDto, userId);
    }

    @PostMapping
    public ResponseEntity<ItemResponseDto> createItem(
            @RequestHeader(value = ItemController.USER_ID_HEADER, required = true) Long ownerId,
            @RequestBody @Valid ItemRequestDto itemRequestDto) {

        ItemResponseDto response = itemService.createItem(itemRequestDto, ownerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> createItemComment(
            @RequestBody CommentRequestDto request,
            @RequestHeader(value = ItemController.USER_ID_HEADER, required = true) Long userId,
            @PathVariable("itemId") Long itemId) {

        CommentDto response = this.itemService.createItemComment(itemId,userId,request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/search")
    public Collection<ItemResponseDto> searchItemsByQuery(
            @RequestParam String text,
            @RequestHeader(value = ItemController.USER_ID_HEADER, required = true) Long ownerId) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        return itemService.searchItems(text, ownerId);
    }

    @GetMapping
    public Collection<ItemResponseDto> getUserItems(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId) {
        return itemService.getUserItems(ownerId);
    }
}
