package ru.practicum.mainservice.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.mainservice.comment.dto.CommentDto;
import ru.practicum.mainservice.comment.dto.NewCommentDto;
import ru.practicum.mainservice.comment.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toComment(NewCommentDto newCommentRequestDto);

    @Mapping(target = "authorName", source = "author.name")
    @Mapping(target = "eventId", source = "event.id")
    CommentDto toCommentDto(Comment comment);
}
