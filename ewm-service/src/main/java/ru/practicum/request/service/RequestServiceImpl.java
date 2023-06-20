package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.State;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.request.dto.EventRequestFullDto;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Override
    @Transactional
    public EventRequestFullDto createRequest(Long userId, Long eventId) {
        Request eventRequest = new Request();
        eventRequest.setCreated(LocalDateTime.now().withNano(0));
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден " + userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Событие не найдено " + eventId));
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ConflictException("Некорректный инициатор " + event.getInitiator().getId());
        }
        Optional<Request> request = requestRepository.findByRequesterIdAndEventId(userId, eventId);
        if (request.isPresent()) {
            throw new ConflictException("Повторный запрос от пользователя " + userId + " на событие" + eventId);
        }

        List<Request> requestList = requestRepository.findAllEventRequestsByEventIs(event);
        if (event.getParticipantLimit() > 0 && requestList.size() >= event.getParticipantLimit()) {
            throw new ConflictException("Превышен лимит участников");
        }

        if (event.getState() != State.PUBLISHED) {
            throw new ConflictException("Некорректный статус " + event.getState());
        }

        if (event.getParticipantLimit() == 0) {
            eventRequest.setStatus(RequestStatus.CONFIRMED);
        } else {
            eventRequest.setStatus(RequestStatus.PENDING);
        }

        eventRequest.setRequester(requester);
        eventRequest.setEvent(event);
        Request createdCategory = requestRepository.save(eventRequest);
        return RequestMapper.toEventRequestDto(createdCategory);
    }

    @Override
    public List<EventRequestFullDto> getRequest(Long userId) {
        Optional<User> requester = userRepository.findById(userId);
        if (requester.isEmpty()) {
            throw new ObjectNotFoundException("Пользователь не найден " + userId);
        } else {
            return requestRepository.findEventRequestsByRequester(requester.get())
                    .stream()
                    .map(RequestMapper::toEventRequestDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public EventRequestFullDto updateCancel(Long userId, Long requestId) {
        Optional<User> requester = userRepository.findById(userId);
        if (requester.isEmpty()) {
            throw new ObjectNotFoundException("Пользователь не найден " + userId);
        }

        Request request = requestRepository.findEventRequestByIdIsAndRequesterIs(requestId, requester.get());
        if (request == null) {
            throw new ObjectNotFoundException("Запрос не найден " + userId);
        }

        request.setStatus(RequestStatus.CANCELED);
        Request updatedRequest = requestRepository.save(request);

        return RequestMapper.toEventRequestDto(updatedRequest);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResultDto updateRequestStatus(Long userId,
                                                                 Long eventId,
                                                                 EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        EventRequestStatusUpdateResultDto eventRequestListDto = new EventRequestStatusUpdateResultDto();
        Optional<User> requester = userRepository.findById(userId);
        if (requester.isEmpty()) {
            throw new ObjectNotFoundException("Пользователь не найден " + userId);
        }
        Optional<Event> eventOptional = eventRepository.findByInitiatorAndId(requester.get(), eventId);
        if (eventOptional.isEmpty()) {
            throw new ObjectNotFoundException("Событие не найдено " + eventId);
        }
        Event event = eventOptional.get();
        List<Request> requestList = requestRepository.findAllEventRequestsByEventIs(event);
        if (event.getParticipantLimit() > 0 && requestList.size() >= event.getParticipantLimit()) {
            throw new ConflictException("Превышен лимит участников");
        }

        if (eventRequestStatusUpdateRequest == null) {
            throw new ConflictException("Список запросов не передан");
        }

        List<Request> eventRequestList = requestRepository.findEventRequestsByIdInAndEventIs(
                eventRequestStatusUpdateRequest.getRequestIds(),
                event
        );
        if (eventRequestList.size() == 0) {
            return eventRequestListDto;
        }

        eventRequestList.forEach(eventRequest -> eventRequest.setStatus(eventRequestStatusUpdateRequest.getStatus()));
        requestRepository.saveAll(eventRequestList);

        if (eventRequestStatusUpdateRequest.getStatus() == RequestStatus.CONFIRMED) {
            eventRepository.save(event);
        }

        if (eventRequestStatusUpdateRequest.getStatus() == RequestStatus.REJECTED) {
            eventRequestListDto.setRejectedRequests(requestRepository.findEventRequestsByIdInAndEventIs(eventRequestStatusUpdateRequest.getRequestIds(), event)
                    .stream()
                    .map(RequestMapper::toEventRequestDtoPart)
                    .collect(Collectors.toList()));
        } else {
            eventRequestListDto.setConfirmedRequests(requestRepository.findEventRequestsByIdInAndEventIs(eventRequestStatusUpdateRequest.getRequestIds(), event)
                    .stream()
                    .map(RequestMapper::toEventRequestDtoPart)
                    .collect(Collectors.toList()));
        }
        return eventRequestListDto;
    }
}
