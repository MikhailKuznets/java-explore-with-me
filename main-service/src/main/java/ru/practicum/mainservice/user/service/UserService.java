package ru.practicum.mainservice.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.mainservice.user.dto.RequestUserDto;
import ru.practicum.mainservice.user.dto.ResponseUserDto;

import java.util.Collection;

@Service
public class UserService {
    public ResponseUserDto createUser(RequestUserDto requestUserDto) {
        return null;
    }

    public Collection<ResponseUserDto> getAllUsers() {
        return null;
    }

    public void deleteUserById(Long userId) {
    }
}
