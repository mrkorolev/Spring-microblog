package ru.specialist.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.specialist.spring.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByTitle(String title);
    List<Post> findByDtCreatedBetween(LocalDateTime from, LocalDateTime to);

    @Query(value = "SELECT * FROM posts;", nativeQuery = true)
    List<Post> findAllPostsNativeQuery();

    @Query(value = """
            SELECT p.*
            FROM posts p
            JOIN posts_tags pt
                ON p.id = pt.post_id
            GROUP BY p.id
            ORDER BY COUNT(*) DESC;
            """, nativeQuery = true)
    List<Post> findPostsOrderByTagsDesc();

    List<Post> findByContentContainingIgnoreCaseOrderByDtCreatedDesc(String search);
}
