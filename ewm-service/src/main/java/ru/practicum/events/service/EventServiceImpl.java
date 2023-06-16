package ru.practicum.events.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatisticRequestDto;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.client.StatisticClient;
import ru.practicum.events.dto.*;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.QEvent;
import ru.practicum.events.model.State;
import ru.practicum.events.model.StateAction;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.request.dto.EventRequestFullDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.users.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatisticClient client;
    private final RequestRepository requestRepository;

    @Value("${app.name}")
    private String appName;

    @Override
    @Transactional
    public EventFullDto createEvent(NewEventDto newEventDto, Long userId) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Не корректная дата " + newEventDto.getEventDate());
        }
        if (newEventDto.getParticipantLimit() == null) {
            long participantLimitDefault = 0;
            newEventDto.setParticipantLimit(participantLimitDefault);
        }
        Event event = EventMapper.toEvent(newEventDto);
        var initiator = userRepository.findById(userId);
        if (initiator.isEmpty()) {
            throw new ObjectNotFoundException("Пользователь не найден " + userId);
        } else {
            event.setInitiator(initiator.get());
        }
        var category = categoryRepository.findById(newEventDto.getCategory());
        category.ifPresent(event::setCategory);
        Event createdEvent = eventRepository.save(event);
        return EventMapper.toEventFullDto(createdEvent);
    }

    @Override
    @Transactional
    public EventFullDto updateEventUser(Long userId, Long eventId, EventRequestDto eventDto) {
        if (eventDto.getEventDate() != null && eventDto.getEventDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Не корректная дата " + eventDto.getEventDate());
        }
        var eventPresent = eventRepository.findById(eventId);
        if (!eventPresent.isPresent()) {
            throw new ObjectNotFoundException("Событие не найдено " + eventId);
        }

        if (eventDto.getTitle() != null && eventDto.getTitle().isBlank()) {
            throw new BadRequestException("Пустое название события");
        }
        if (eventDto.getDescription() != null && eventDto.getDescription().isBlank()) {
            throw new BadRequestException("Пустое описание события");
        }
        if (eventDto.getAnnotation() != null && eventDto.getAnnotation().isBlank()) {
            throw new BadRequestException("Пустая аннотация события");
        }

        var event = eventPresent.get();
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ObjectNotFoundException("Пользователь не найден " + userId);
        }
        if (event.getState() == State.PUBLISHED) {
            throw new ConflictException("Событие нельзя изменить " + eventId);
        }
        EventMapper.toEventUserUpdate(event, eventDto);
        if (eventDto.getStateAction() != null) {
            event.setState(State.stringToState(String.valueOf(eventDto.getStateAction())));
        }
        event.setId(eventId);
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getAllByUserId(Long userId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        var initiator = userRepository.findById(userId);
        if (initiator.isEmpty()) {
            throw new ObjectNotFoundException("Пользователь не найден " + userId);
        } else {
            return eventRepository.findAllByInitiator(initiator.get(), pageable)
                    .stream()
                    .map(EventMapper::toEventShortDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public EventFullDto getAllByUserIdAndEventId(Long userId, Long eventId) {
        var initiator = userRepository.findById(userId);
        if (initiator.isEmpty()) {
            throw new ObjectNotFoundException("Пользователь не найден " + userId);
        } else {
            return EventMapper.toEventFullDto(eventRepository.findEventByInitiatorAndId(initiator.get(), eventId));
        }
    }

    @Override
    public List<EventRequestFullDto> getAllRequests(Long userId, Long eventId) {
        var initiator = userRepository.findById(userId);
        if (initiator.isEmpty()) {
            throw new ObjectNotFoundException("Пользователь не найден " + userId);
        }
        var event = eventRepository.findByInitiatorAndId(initiator.get(), eventId);
        if (event.isEmpty()) {
            throw new ObjectNotFoundException("Событие не найдено" + eventId);
        }
        return requestRepository.findAllEventRequestsByEventIs(event.get())
                .stream().map(RequestMapper::toEventRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> getEventAdmin(AdminSearchDto adminSearch, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> events = eventRepository.findAll(getRequestParam(adminSearch), pageable).getContent();
        return events.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto updateEventAdmin(Long eventId, EventRequestDto eventDtoRequest) {
        if (eventDtoRequest.getEventDate() != null && eventDtoRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new BadRequestException("Не корректная дата " + eventDtoRequest.getEventDate());
        }

        if (eventDtoRequest.getTitle() != null && eventDtoRequest.getTitle().isBlank()) {
            throw new BadRequestException("Пустое название события");
        }
        if (eventDtoRequest.getDescription() != null && eventDtoRequest.getDescription().isBlank()) {
            throw new BadRequestException("Пустое описание события");
        }
        if (eventDtoRequest.getAnnotation() != null && eventDtoRequest.getAnnotation().isBlank()) {
            throw new BadRequestException("Пустая аннотация события");
        }

        var eventOptional = eventRepository.findById(eventId);
        if (!eventOptional.isPresent()) {
            throw new ObjectNotFoundException(String.format("Событие не найдено " + eventId));
        }
        var event = eventOptional.get();
        if (eventDtoRequest.getStateAction() != null) {
            if (State.stringToState(String.valueOf(eventDtoRequest.getStateAction())) == event.getState()) {
                throw new ConflictException("Не корректный статус " + eventDtoRequest.getStateAction());
            }
            if (StateAction.stringToStateAction(String.valueOf(eventDtoRequest.getStateAction())) == StateAction.PUBLISH_EVENT
                    && event.getState() == State.CANCELED) {
                throw new ConflictException("Не корректный статус " + eventDtoRequest.getStateAction());
            }
            if (StateAction.stringToStateAction(String.valueOf(eventDtoRequest.getStateAction())) == StateAction.REJECT_EVENT
                    && event.getState() == State.PUBLISHED) {
                throw new ConflictException("Не корректный статус " + eventDtoRequest.getStateAction());
            }
        }
        EventMapper.toEvent(event, eventDtoRequest);
        if (StateAction.stringToStateAction(String.valueOf(eventDtoRequest.getStateAction())) == StateAction.PUBLISH_EVENT) {
            event.setPublishedOn(LocalDateTime.now().withNano(0));
        }
        event.setState(State.stringToState(String.valueOf(eventDtoRequest.getStateAction())));
        event.setId(eventId);
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getAllEventsPublic(UserSearchDto userSearch,
                                                  int from,
                                                  int size,
                                                  HttpServletRequest request) {
        Pageable pageable = PageRequest.of(from / size, size);
        StatisticRequestDto statRequestDto = StatisticRequestDto.builder().app(appName)
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timeStamp(LocalDateTime.now()).build();
        client.createStat(statRequestDto);
        List<Event> events = eventRepository.findAll(getRequestParamForDb(userSearch), pageable).getContent();
        List<EventShortDto> eventShort = events.stream()
                .map(event -> EventMapper.toEventShortDto(event))
                .collect(Collectors.toList());
        if (userSearch.getSort() != null) {
            switch (userSearch.getSort()) {
                case VIEWS:
                    eventShort = eventShort.stream()
                            .sorted(Comparator.comparing(EventShortDto::getViews))
                            .collect(Collectors.toList());
                    break;
                case EVENT_DATE:
                    eventShort = eventShort.stream()
                            .sorted(Comparator.comparing(EventShortDto::getEventDate))
                            .collect(Collectors.toList());
                    break;
            }
        }
        return eventShort;
    }

    @Override
    public EventFullDto getEventPublicById(Long id,
                                           HttpServletRequest request) {
        StatisticRequestDto statRequestDto = StatisticRequestDto.builder().app(appName)
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timeStamp(LocalDateTime.now()).build();
        client.createStat(statRequestDto);

        Optional<LocalDateTime> minPublishedDate = eventRepository.findMinPublishedDate();

        ResponseEntity<Object> existingStat = client.getStat(
                minPublishedDate.map(localDateTime -> localDateTime.format(EventMapper.DATE_TIME_FORMATTER))
                        .orElseGet(() -> LocalDateTime.now().format(EventMapper.DATE_TIME_FORMATTER)),
                LocalDateTime.now().format(EventMapper.DATE_TIME_FORMATTER),
                new String[]{request.getRequestURI()},
                true
        );

        Optional<Event> event = eventRepository.findById(id);
        if (event.isEmpty() || event.get().getState() != State.PUBLISHED) {
            throw new ObjectNotFoundException("Событие не найдено " + id);
        }

        if (existingStat != null) {
            long views = event.get().getViews() != null ? event.get().getViews() : 0;
            event.get().setViews(views + 1);
        }

        return EventMapper.toEventFullDto(event.get());
    }

    private BooleanBuilder getRequestParam(AdminSearchDto param) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (param.getUsers() != null) {
            booleanBuilder.and(QEvent.event.initiator.id.in(param.getUsers()));
        }
        if (param.getStates() != null) {
            booleanBuilder.and(QEvent.event.state.in(param.getStates()));
        }
        if (param.getCategories() != null) {
            booleanBuilder.and((QEvent.event.category.id.in(param.getCategories())));
        }
        if (param.getRangeStart() != null) {
            booleanBuilder.and(QEvent.event.eventDate.after(param.getRangeStart()));
        }
        if (param.getRangeEnd() != null) {
            booleanBuilder.and(QEvent.event.eventDate.before(param.getRangeEnd()));
        }
        return booleanBuilder;
    }

    private BooleanBuilder getRequestParamForDb(UserSearchDto param) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(QEvent.event.state.eq(State.PUBLISHED));
        if (param.getText() != null) {
            String text = param.getText();
            booleanBuilder.and((QEvent.event.annotation.containsIgnoreCase(text)
                    .or(QEvent.event.description.containsIgnoreCase(text))));
        }
        if (param.getCategories() != null) {
            booleanBuilder.and(QEvent.event.category.id.in(param.getCategories()));
        }
        if (param.getPaid() != null) {
            booleanBuilder.and(QEvent.event.paid.eq(param.getPaid()));
        }
        if (param.getRangeStart() != null) {
            booleanBuilder.and(QEvent.event.eventDate.after(param.getRangeStart()));
        }
        if (param.getRangeEnd() != null) {
            booleanBuilder.and(QEvent.event.eventDate.before(param.getRangeEnd()));
        }
        if (param.getOnlyAvailable() != null) {
            booleanBuilder.and((QEvent.event.confirmedRequests.lt(QEvent.event.participantLimit.intValue())).or(QEvent.event.confirmedRequests.isNull()));
        }
        return booleanBuilder;
    }
}
