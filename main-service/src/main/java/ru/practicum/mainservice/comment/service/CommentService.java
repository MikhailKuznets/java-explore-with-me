package ru.practicum.mainservice.comment.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.comment.dto.CommentDto;
import ru.practicum.mainservice.comment.dto.NewCommentDto;
import ru.practicum.mainservice.comment.dto.UpdateCommentDto;
import ru.practicum.mainservice.comment.mapper.CommentMapper;
import ru.practicum.mainservice.comment.model.Comment;
import ru.practicum.mainservice.comment.model.QComment;
import ru.practicum.mainservice.comment.repository.CommentRepository;
import ru.practicum.mainservice.controllers.parameters.comment.AdminCommentRequestParameters;
import ru.practicum.mainservice.controllers.parameters.comment.BaseCommentRequestParameters;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.QEvent;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.exception.InvalidIdException;
import ru.practicum.mainservice.exception.comment.IncorrectAuthorCommentException;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private static final Sort COMMENT_ID_DESC_SORT = Sort.by(Sort.Direction.ASC, "id");
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentMapper commentMapper;

    // PUBLIC-CONTROLLER
    public CommentDto getCommentByPublic(Long commentId) {
        Comment comment = findComment(commentId);
        return commentMapper.toCommentDto(comment);
    }

    public List<CommentDto> getEventCommentsByPublic(Long eventId) {
        findEvent(eventId);
        return commentRepository.findAllByEvent_Id(eventId).stream()
                .sorted(Comparator.comparing(Comment::getId))
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    // ADMIN-CONTROLLER
    public CommentDto deleteCommentByAdmin(Long commentId) {
        Comment deletedComment = findComment(commentId);
        commentRepository.deleteById(commentId);
        return commentMapper.toCommentDto(deletedComment);
    }

    public CommentDto updateCommentByAdmin(UpdateCommentDto updateCommentDto) {
        Long commentId = updateCommentDto.getCommentId();
        Comment selectedComment = findComment(commentId);
        String newText = updateCommentDto.getText();
        selectedComment.setText(newText);
        return commentMapper.toCommentDto(commentRepository.save(selectedComment));
    }

    public Collection<CommentDto> getCommentForAdmin(AdminCommentRequestParameters parameters,
                                                     Integer from,
                                                     Integer size) {
        parameters.checkTime();
        PageRequest pageRequest = PageRequest.of(from, size, COMMENT_ID_DESC_SORT);
        BooleanBuilder predicate = getAdminPredicate(parameters);

        Page<Comment> comments = commentRepository.findAll(predicate, pageRequest);
        return comments.stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    // PRIVATE-CONTROLLER

    public CommentDto createComment(Long userId, NewCommentDto newCommentDto) {
        User author = findUser(userId);
        Long eventId = newCommentDto.getEventId();
        Event event = findEvent(eventId);

        Comment newComment = Comment.builder()
                .text(newCommentDto.getText())
                .event(event)
                .author(author)
                .created(LocalDateTime.now())
                .build();

        return commentMapper.toCommentDto(commentRepository.save(newComment));
    }

    public CommentDto getCommentById(Long userId, Long commentId) {
        findUser(userId);
        return commentMapper.toCommentDto(findComment(commentId));
    }

    public List<CommentDto> getEventComments(Long userId, Long eventId) {
        findEvent(eventId);
        findUser(userId);
        Collection<Comment> comments = commentRepository.findAllByEvent_Id(eventId);
        return comments.stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getAllUserComments(Long userId) {
        findUser(userId);

        Collection<Comment> comments = commentRepository.findAllByAuthor_Id(userId);
        return comments.stream()
                .map(commentMapper::toCommentDto)
                .sorted(Comparator.comparing(CommentDto::getId))
                .collect(Collectors.toList());
    }

    public CommentDto updateUserComment(Long userId, UpdateCommentDto updateCommentDto) {
        findUser(userId);
        Long commentId = updateCommentDto.getCommentId();
        Comment selectedComment = findComment(commentId);
        User author = selectedComment.getAuthor();

        if (!author.getId().equals(userId)) {
            throw new IncorrectAuthorCommentException("You can't edit someone else's comment",
                    LocalDateTime.now());
        }

        String newText = updateCommentDto.getText();
        selectedComment.setText(newText);

        return commentMapper.toCommentDto(commentRepository.save(selectedComment));
    }

    public CommentDto deleteUserCommentById(Long userId, Long commentId) {
        findUser(userId);
        Comment selectedComment = findComment(commentId);
        User author = selectedComment.getAuthor();

        if (!author.getId().equals(userId)) {
            throw new IncorrectAuthorCommentException("You can't delete someone else's comment",
                    LocalDateTime.now());
        }
        commentRepository.deleteById(commentId);
        return commentMapper.toCommentDto(selectedComment);
    }

    public Collection<CommentDto> getCommentForUser(Long userId,
                                                    BaseCommentRequestParameters parameters,
                                                    Integer from,
                                                    Integer size) {
        findUser(userId);
        PageRequest pageRequest = PageRequest.of(from, size, COMMENT_ID_DESC_SORT);
        BooleanBuilder predicate = getPredicate(parameters);
        Page<Comment> comments = commentRepository.findAll(predicate, pageRequest);
        return comments.stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    //                      PRIVATE METHODS

    private BooleanBuilder getAdminPredicate(AdminCommentRequestParameters parameters) {
        BooleanBuilder predicate = getPredicate(parameters);

        List<Long> userIds = parameters.getUserIds();
        if (!userIds.isEmpty()) {
            predicate.and(QComment.comment.author.id.in(userIds));
        }
        return predicate;
    }

    private BooleanBuilder getPredicate(BaseCommentRequestParameters parameters) {
        BooleanBuilder predicate = new BooleanBuilder();

        parameters.checkTime();
        String text = parameters.getText();
        List<Long> eventIds = parameters.getEventIds();
        LocalDateTime rangeStart = parameters.getRangeStart();
        LocalDateTime rangeEnd = parameters.getRangeEnd();

        if (text != null) {
            predicate.and(QComment.comment.text.containsIgnoreCase(text));
        }
        if (!eventIds.isEmpty()) {
            predicate.and(QComment.comment.event.id.in(eventIds));
        }

        predicate.and(QEvent.event.eventDate.after(rangeStart));
        predicate.and(QEvent.event.eventDate.before(rangeEnd));

        return predicate;
    }

    // Поиск Event / User / Category
    private Event findEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> {
            throw new InvalidIdException("Event", eventId, LocalDateTime.now());
        });
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            throw new InvalidIdException("User", userId, LocalDateTime.now());
        });
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> {
            throw new InvalidIdException("Comment", commentId, LocalDateTime.now());
        });
    }

}
