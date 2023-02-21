package ru.practicum.mainservice.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.exception.InvalidIdException;
import ru.practicum.mainservice.user.dto.NewUserRequest;
import ru.practicum.mainservice.user.dto.UserDto;
import ru.practicum.mainservice.user.mapper.UserMapper;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto createUser(NewUserRequest newUserRequest) {
        User newUser = userMapper.toUser(newUserRequest);
        return userMapper.toUserDto(userRepository.save(newUser));
    }

    public Collection<UserDto> getUsers(long[] ids, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from, size);

        if (ids.length == 0) {
            return userRepository.findAll(pageRequest).stream()
                    .map(userMapper::toUserDto)
                    .collect(Collectors.toList());
        }

        List<Long> longIds = Arrays.stream(ids)
                .boxed()
                .collect(Collectors.toList());

        return userRepository.findAllById(longIds).stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }


    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            throw new InvalidIdException("User", userId, LocalDateTime.now());
        });
    }
}
