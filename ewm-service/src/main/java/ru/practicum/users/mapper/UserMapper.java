package ru.practicum.users.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.SubscriptionDto;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.dto.UserShortDto;
import ru.practicum.users.model.StatusSubscription;
import ru.practicum.users.model.Subscription;
import ru.practicum.users.model.User;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static User toUser(NewUserRequest userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }

    public static List<UserDto> toListDto(List<User> users) {
        return users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

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
