package ru.practicum.users.service;

import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(Long[] ids, int from, int size);

    UserDto createUser(NewUserRequest newUser);

    void removeUser(Long id);

    UserDto getById(Long id);
}
