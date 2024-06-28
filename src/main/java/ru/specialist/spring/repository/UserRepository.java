package ru.specialist.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.specialist.spring.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query(value = """
            SELECT u.*
            FROM users u
            JOIN users_roles ur
                ON u.id = ur.user_id
            GROUP BY u.id
            ORDER BY COUNT(*) DESC;
            """, nativeQuery = true)
    List<User> usersByRolesDesc();
}
