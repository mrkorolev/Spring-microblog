package ru.specialist.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.specialist.spring.entity.Tag;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String tagName);
}
