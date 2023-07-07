package ru.practicum.users.service;

import ru.practicum.events.dto.EventFullDto;
import ru.practicum.users.dto.SubscriptionDto;

import java.util.List;

public interface SubscriptionService {
    SubscriptionDto createSubscribe(Long userId, Long subscriberId);

    SubscriptionDto confirmSubscribe(Long userId, Long subscriberId);

    void rejectSubscribe(Long userId, Long subscriberId);

    void deleteSubscribe(Long userId, Long subscriberId);

    List<SubscriptionDto> getFollowing(Long userId);

    List<EventFullDto> getPublishEvent(Long userId, Long subscriberId, int from, int size);

    List<EventFullDto> getParticipantEvent(Long userId, Long subscriberId, int from, int size);
}
