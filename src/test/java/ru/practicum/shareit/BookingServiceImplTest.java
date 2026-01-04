package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User owner;
    private User booker;
    private Item item;
    private Booking booking;
    private BookingCreateDto bookingCreateDto;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .id(1L)
                .name("Owner")
                .email("owner@test.com")
                .build();

        booker = User.builder()
                .id(2L)
                .name("Booker")
                .email("booker@test.com")
                .build();

        item = Item.builder()
                .id(1L)
                .name("Дрель")
                .description("Мощная дрель")
                .available(true)
                .owner(owner)
                .build();

        booking = Booking.builder()
                .id(1L)
                .bookingStartDate(LocalDateTime.now().plusHours(1))
                .bookingEndDate(LocalDateTime.now().plusHours(3))
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .created(LocalDateTime.now())
                .build();

        bookingCreateDto = new BookingCreateDto(
                1L,  // itemId
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(3)
        );
    }

    @Test
    void createBooking_ShouldCreateBookingSuccessfully() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(bookingMapper.toEntity(any(BookingCreateDto.class))).thenReturn(booking);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingMapper.toDto(any(Booking.class))).thenReturn(
                new BookingResponseDto(
                        1L, BookingStatus.WAITING,
                        booking.getBookingStartDate(),
                        booking.getBookingEndDate(),
                        null, null
                )
        );

        BookingResponseDto result = bookingService.createBooking(bookingCreateDto, 2L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(BookingStatus.WAITING, result.status());

        verify(userRepository, times(1)).findById(2L);
        verify(itemRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).save(any(Booking.class));

        assertTrue(item.getAvailable());
    }

    @Test
    void manageBooking_Approve_ShouldUpdateStatusAndMakeItemUnavailable() throws AccessDeniedException {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        bookingService.manageBooking(1L, 1L, true);

        assertEquals(BookingStatus.APPROVED, booking.getStatus());
        assertFalse(item.getAvailable());

        verify(bookingRepository, never()).save(any(Booking.class));
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void manageBooking_Reject_ShouldUpdateStatusAndKeepItemAvailable() throws AccessDeniedException {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        bookingService.manageBooking(1L, 1L, false);

        assertEquals(BookingStatus.REJECTED, booking.getStatus());
        assertTrue(item.getAvailable());

        verify(bookingRepository, never()).save(any(Booking.class));
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void manageBooking_WhenBookingNotFound_ShouldThrowException() {
        when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            bookingService.manageBooking(1L, 999L, true);
        });
    }

    @Test
    void manageBooking_WhenNotOwner_ShouldThrowException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        assertThrows(RuntimeException.class, () -> {
            bookingService.manageBooking(999L, 1L, true);
        });
    }

    @Test
    void getBooking_ShouldReturnBookingForOwner() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingMapper.toDto(any(Booking.class))).thenReturn(
                new BookingResponseDto(1L, BookingStatus.WAITING, null, null, null, null)
        );

        BookingResponseDto result = bookingService.getBooking(1L, 1L);  // Owner requests

        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    void getBooking_ShouldReturnBookingForBooker() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingMapper.toDto(any(Booking.class))).thenReturn(
                new BookingResponseDto(1L, BookingStatus.WAITING, null, null, null, null)
        );

        BookingResponseDto result = bookingService.getBooking(2L, 1L);  // Booker requests

        assertNotNull(result);
        assertEquals(1L, result.id());
    }
}