package ru.specialist.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.specialist.spring.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
