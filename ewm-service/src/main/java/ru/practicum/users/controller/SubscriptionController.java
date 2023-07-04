package ru.practicum.users.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.users.dto.SubscriptionDto;
import ru.practicum.users.service.SubscriptionService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/subscriptions/users/{userId}")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping("/subscriber/{subscriberId}/subscribe")
    public SubscriptionDto createSubscribe(@PathVariable Long userId, @PathVariable Long subscriberId) {
        return subscriptionService.createSubscribe(userId, subscriberId);
    }

    @PatchMapping("/subscriber/{subscriberId}/subscribe")
    public SubscriptionDto confirmSubscribe(@PathVariable Long userId, @PathVariable Long subscriberId) {
        return subscriptionService.confirmSubscribe(userId, subscriberId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/subscriber/{subscriberId}/unsubscribe")
    void rejectSubscribe(@PathVariable Long userId, @PathVariable Long subscriberId) {
        subscriptionService.rejectSubscribe(userId, subscriberId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/subscriber/{subscriberId}/delete")
    public void deleteSubscribe(@PathVariable Long userId, @PathVariable Long subscriberId) {
        subscriptionService.deleteSubscribe(userId, subscriberId);
    }

    @GetMapping
    public List<SubscriptionDto> getFollowing(@PathVariable Long userId) {
        return subscriptionService.getFollowing(userId);
    }

    @GetMapping("/subscriber/{subscriberId}/event")
    public List<EventFullDto> getPublishEvent(@PathVariable Long userId, @PathVariable Long subscriberId,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return subscriptionService.getPublishEvent(userId, subscriberId, from, size);
    }

    @GetMapping("/subscriber/{subscriberId}/participant")
    public List<EventFullDto> getParticipantEvent(@PathVariable Long userId, @PathVariable Long subscriberId,
                                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                  @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return subscriptionService.getParticipantEvent(userId, subscriberId, from, size);
    }
}
