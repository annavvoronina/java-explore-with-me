package ru.practicum.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.mapper.UserMapper;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Transactional
    @Override
    public UserDto createUser(NewUserRequest newUser) {
        User existingUser = repository.findByEmail(newUser.getEmail());
        if (existingUser != null) {
            throw new ConflictException("Пользователь с таким email уже существует");
        }

        if (newUser.getName().length() < 2) {
            throw new BadRequestException("Слишком короткое имя пользователя");
        }

        if (newUser.getName().length() > 250) {
            throw new BadRequestException("Слишком длинное имя пользователя");
        }

        if (newUser.getEmail().length() < 6) {
            throw new BadRequestException("Слишком короткий email пользователя");
        }

        if (newUser.getEmail().length() > 254) {
            throw new BadRequestException("Слишком длинный email пользователя");
        }

        User createdUser = repository.save(UserMapper.toUser(newUser));
        return UserMapper.toUserDto(createdUser);
    }

    @Override
    public List<UserDto> getUsers(Long[] ids, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (ids != null && ids.length > 0) {
            return UserMapper.toListDto(repository.findUsersByIdIn(Arrays.asList(ids)));
        } else {
            return UserMapper.toListDto(repository.findAll(pageable).getContent());
        }
    }

    @Transactional
    @Override
    public void removeUser(Long id) {
        getById(id);
        repository.deleteById(id);
    }

    @Override
    public UserDto getById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден " + id));
        return UserMapper.toUserDto(user);
    }
}
