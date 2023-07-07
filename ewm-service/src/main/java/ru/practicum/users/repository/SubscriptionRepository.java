package ru.practicum.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.users.model.Subscription;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription findByUserIdAndSubscriberId(Long userId, Long subscriberId);

    Optional<Subscription> findByUserId(Long userId);

    void deleteByUserIdAndSubscriberId(Long userId, Long subscriberId);
}
