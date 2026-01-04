package ru.practicum.shareit;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BookingItemCreationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void testManageBookingDoesNotCreateNewItem() throws AccessDeniedException {
        User owner = userRepository.save(new User(null, "Owner", "owner@test.com", "password123", null));
        User booker = userRepository.save(new User(null, "Booker", "booker@test.com", "password456", null));
        Item originalItem = itemRepository.save(
                new Item(null, "Original Item", "Description", true, owner, null)
        );

        Booking booking = bookingRepository.save(
                new Booking(null,
                        LocalDateTime.now().plusHours(1),
                        LocalDateTime.now().plusHours(3),
                        originalItem,
                        booker,
                        BookingStatus.WAITING,
                        LocalDateTime.now()
                )
        );

        long itemCountBefore = itemRepository.count();
        long originalItemId = originalItem.getId();

        entityManager.flush();
        entityManager.clear();

        BookingResponseDto result = bookingService.manageBooking(owner.getId(), booking.getId(), true);


        long itemCountAfter = itemRepository.count();
        assertEquals(itemCountBefore, itemCountAfter, "Количество items изменилось!");

        Booking updatedBooking = bookingRepository.findById(booking.getId()).orElseThrow();
        assertEquals(originalItemId, updatedBooking.getItem().getId(), "Item ID изменился!");

        assertFalse(updatedBooking.getItem().getAvailable(), "Item должен быть недоступен");
        assertEquals("Original Item", updatedBooking.getItem().getName(), "Имя item изменилось");
    }

    @Test
    void testItemIsProperlyLinkedAfterApproval() throws AccessDeniedException {
        User owner = userRepository.save(new User(null, "Owner", "owner@test.com", "password123", null));
        User booker = userRepository.save(new User(null, "Booker", "booker@test.com", "password456", null));
        Item item = itemRepository.save(
                new Item(null, "Test Item", "Desc", true, owner, null)
        );

        Booking booking = bookingRepository.save(
                new Booking(null,
                        LocalDateTime.now().plusHours(1),
                        LocalDateTime.now().plusHours(3),
                        item,
                        booker,
                        BookingStatus.WAITING,
                        LocalDateTime.now()
                )
        );

        // Подтверждаем
        bookingService.manageBooking(owner.getId(), booking.getId(), true);

        // Проверяем связь
        entityManager.flush();
        entityManager.clear();

        Booking savedBooking = bookingRepository.findById(booking.getId()).orElseThrow();
        Item savedItem = savedBooking.getItem();

        assertNotNull(savedItem);
        assertEquals(item.getId(), savedItem.getId());
        assertEquals(owner.getId(), savedItem.getOwner().getId());
        assertFalse(savedItem.getAvailable());
    }
}