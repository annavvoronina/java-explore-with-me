package ru.practicum.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.events.model.Event;
import ru.practicum.users.model.User;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    Optional<Event> findByCategoryId(Long categoryId);

    List<Event> findAllByIdIn(List<Long> ids);

    Page<Event> findAllByInitiator(User user, Pageable pageable);

    Optional<Event> findByInitiatorAndId(User user, Long id);

    Event findEventByInitiatorAndId(User user, Long id);
}
