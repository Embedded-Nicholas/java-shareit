package ru.practicum.shareit.item.repository;

import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.entity.User;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item,Long> {
    @Query("SELECT i FROM Item i " +
            "WHERE i.available = true AND " +
            "(LOWER(i.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            " LOWER(i.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    Collection<Item> searchAvailableItems(@Param("query") String query);

    @EntityGraph(attributePaths = {"request"})
    Collection<Item> findByOwnerIdOrderByName(Long ownerId);
}

