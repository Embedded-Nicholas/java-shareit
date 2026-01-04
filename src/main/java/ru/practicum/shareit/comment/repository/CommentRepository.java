package ru.practicum.shareit.comment.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.comment.model.Comment;

import java.util.Collection;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @EntityGraph(attributePaths = {"author"})
    Collection<Comment> findByItemIdOrderByCreatedDesc(Long itemId);
}
