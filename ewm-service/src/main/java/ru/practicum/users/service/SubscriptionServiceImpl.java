package ru.practicum.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.State;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.users.dto.SubscriptionDto;
import ru.practicum.users.mapper.UserMapper;
import ru.practicum.users.model.StatusSubscription;
import ru.practicum.users.model.Subscription;
import ru.practicum.users.repository.SubscriptionRepository;
import ru.practicum.users.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Transactional
    @Override
    public SubscriptionDto createSubscribe(Long userId, Long subscriberId) {
        validateUsers(userId, subscriberId);
        var subscription = subscriptionRepository.findByUserIdAndSubscriberId(userId, subscriberId);
        var subscriptionSubscriber = subscriptionRepository.findByUserIdAndSubscriberId(subscriberId, userId);
        if (subscription == null) {
            subscription = subscriptionRepository.save(UserMapper.toSubscription(userId, subscriberId, StatusSubscription.PENDING));
            subscriptionRepository.save(UserMapper.toSubscription(subscriberId, userId, StatusSubscription.REQUEST));
        } else {
            if (!subscription.getStatus().equals(StatusSubscription.FOLLOWER) && !subscription.getStatus().equals(StatusSubscription.REQUEST)) {
                throw new ObjectNotFoundException("Уже есть подписка на пользователя или ожидает подтверждения " + subscriberId);
            }
            subscription.setStatus(StatusSubscription.FRIEND);
            subscriptionSubscriber.setStatus(StatusSubscription.FRIEND);
        }
        return UserMapper.toSubscriptionDto(subscription);
    }

    @Transactional
    @Override
    public SubscriptionDto confirmSubscribe(Long userId, Long subscriberId) {
        validateUsers(userId, subscriberId);
        var subscription = subscriptionRepository.findByUserIdAndSubscriberId(userId, subscriberId);
        var subscriptionSubscriber = subscriptionRepository.findByUserIdAndSubscriberId(subscriberId, userId);
        if (subscription == null || !subscription.getStatus().equals(StatusSubscription.REQUEST)) {
            throw new ObjectNotFoundException("Заявок нет от пользователя " + subscriberId);
        }
        subscription.setStatus(StatusSubscription.FOLLOWER);
        subscriptionSubscriber.setStatus(StatusSubscription.FOLLOWING);
        return UserMapper.toSubscriptionDto(subscription);
    }

    @Transactional
    @Override
    public void rejectSubscribe(Long userId, Long subscriberId) {
        validateUsers(userId, subscriberId);
        var subscription = subscriptionRepository.findByUserIdAndSubscriberId(userId, subscriberId);
        if (subscription == null || !subscription.getStatus().equals(StatusSubscription.REQUEST)) {
            throw new ObjectNotFoundException("Заявок нет от пользователя " + subscriberId);
        }
        subscriptionRepository.deleteByUserIdAndSubscriberId(userId, subscriberId);
        subscriptionRepository.deleteByUserIdAndSubscriberId(subscriberId, userId);
    }

    @Transactional
    @Override
    public void deleteSubscribe(Long userId, Long subscriberId) {
        validateUsers(userId, subscriberId);
        var subscription = subscriptionRepository.findByUserIdAndSubscriberId(userId, subscriberId);
        var subscriptionSubscriber = subscriptionRepository.findByUserIdAndSubscriberId(subscriberId, userId);
        if (subscription == null || subscription.getStatus().equals(StatusSubscription.REQUEST) ||
                subscription.getStatus().equals(StatusSubscription.FOLLOWER)) {
            throw new ObjectNotFoundException("Вы не подписаны на пользователя " + subscriberId);
        }
        if (!subscription.getStatus().equals(StatusSubscription.FRIEND)) {
            subscriptionRepository.deleteByUserIdAndSubscriberId(userId, subscriberId);
            subscriptionRepository.deleteByUserIdAndSubscriberId(subscriberId, userId);
        } else {
            subscription.setStatus(StatusSubscription.FOLLOWER);
            subscriptionSubscriber.setStatus(StatusSubscription.FOLLOWING);
        }
    }

    @Override
    public List<SubscriptionDto> getFollowing(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден " + userId));
        Optional<Subscription> subscription = subscriptionRepository.findByUserId(userId);
        if (subscription.isEmpty()) {
            throw new ObjectNotFoundException("Подписок у пользователя нет " + userId);
        }
        return subscription
                .stream()
                .map(UserMapper::toSubscriptionDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> getPublishEvent(Long userId, Long subscriberId, int from, int size) {
        validateUsers(userId, subscriberId);
        Subscription subscription = subscriptionRepository.findByUserIdAndSubscriberId(userId, subscriberId);
        if (subscription == null || (!subscription.getStatus().equals(StatusSubscription.FOLLOWING) &&
                !subscription.getStatus().equals(StatusSubscription.FRIEND))) {
            throw new ObjectNotFoundException("Вы не подписаны на пользователя " + subscriberId);
        }
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> events = eventRepository.findAllByInitiatorIdAndState(subscriberId, State.PUBLISHED, pageable).getContent();
        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> getParticipantEvent(Long userId, Long subscriberId, int from, int size) {
        validateUsers(userId, subscriberId);
        Subscription subscription = subscriptionRepository.findByUserIdAndSubscriberId(userId, subscriberId);
        if (subscription == null || !subscription.getStatus().equals(StatusSubscription.FRIEND)) {
            throw new ObjectNotFoundException("Вы не являетесь другом пользователя " + subscriberId);
        }
        Pageable pageable = PageRequest.of(from / size, size);
        List<Request> requests = requestRepository.findByRequesterIdAndStatusIs(subscriberId, RequestStatus.CONFIRMED, pageable).getContent();
        List<Long> eventsIds = new ArrayList<>();
        for (Request request : requests) {
            eventsIds.add(request.getEvent().getId());
        }
        List<Event> events = eventRepository.findAllByIdIn(eventsIds);
        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    public void validateUsers(Long userId, Long subscriberId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден " + userId));
        userRepository.findById(subscriberId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден " + subscriberId));
    }
}
