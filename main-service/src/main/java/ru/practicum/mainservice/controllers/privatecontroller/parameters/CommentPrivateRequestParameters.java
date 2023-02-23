package ru.practicum.mainservice.controllers.privatecontroller.parameters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentPrivateRequestParameters {
    private String text;
    private List<Long> userIds;
    private List<Long> eventIds;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
}
