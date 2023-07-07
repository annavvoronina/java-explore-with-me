package ru.practicum.users.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.users.dto.SubscriptionDto;
import ru.practicum.users.model.StatusSubscription;
import ru.practicum.users.model.Subscription;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubscriptionMapper {
    public static Subscription toSubscription(Long userId, Long subscriberId, StatusSubscription status) {
        Subscription subscription = new Subscription();
        subscription.setUserId(userId);
        subscription.setSubscriberId(subscriberId);
        subscription.setStatus(status);
        return subscription;
    }

    public static SubscriptionDto toSubscriptionDto(Subscription subscription) {
        return new SubscriptionDto(subscription.getUserId(), subscription.getSubscriberId(), subscription.getStatus());
    }
}
