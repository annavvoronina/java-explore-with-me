package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.events.model.Event;
import ru.practicum.request.model.Request;
import ru.practicum.users.model.User;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllEventRequestsByEventIs(Event event);

    List<Request> findEventRequestsByRequester(User requester);

    Request findAEventRequestByIdIsAndRequesterIs(Long requestId, User requester);

    Optional<Request> findByRequesterIdAndEventId(Long userId, Long eventId);

    List<Request> findEventRequestsByIdInAndEventIs(List<Long> requestIds, Event event);
}
