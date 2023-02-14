package ru.practicum.mainservice.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.mainservice.user.dto.RequestUserDto;
import ru.practicum.mainservice.user.dto.ResponseUserDto;
import ru.practicum.mainservice.user.dto.UserShortDto;
import ru.practicum.mainservice.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toUser(RequestUserDto requestUserDto);

    UserShortDto toUserShortDto(User user);

    ResponseUserDto toUserDto(User user);
}
