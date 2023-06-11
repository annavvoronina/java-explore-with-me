package ru.practicum.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.users.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findUsersByIdIn(List<Long> ids);

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    User findByEmail(String email);

}
