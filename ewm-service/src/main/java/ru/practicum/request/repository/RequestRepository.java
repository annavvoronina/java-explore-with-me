package ru.practicum.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.events.model.Event;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.users.model.User;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends PagingAndSortingRepository<Request, Long> {
    List<Request> findAllEventRequestsByEventIs(Event event);

    List<Request> findEventRequestsByRequester(User requester);

    Page<Request> findByRequesterIdAndStatusIs(Long requesterId, RequestStatus requestStatus, Pageable pageable);

    Request findEventRequestByIdIsAndRequesterIs(Long requestId, User requester);

    Optional<Request> findByRequesterIdAndEventId(Long userId, Long eventId);

    List<Request> findEventRequestsByIdInAndEventIs(List<Long> requestIds, Event event);
}
