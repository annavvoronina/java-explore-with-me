package ru.practicum.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.users.model.StatusSubscription;

@Data
@AllArgsConstructor
@Builder
public class SubscriptionDto {
    private Long userId;
    private Long subscriberId;
    private StatusSubscription status;
}
