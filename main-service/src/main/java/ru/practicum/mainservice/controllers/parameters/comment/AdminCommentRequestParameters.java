package ru.practicum.mainservice.controllers.admincontrollers.parameters;

import lombok.*;
import ru.practicum.mainservice.controllers.parameters.BaseCommentRequestParameters;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminCommentRequestParameters extends BaseCommentRequestParameters {
    private List<Long> userIds;

    public AdminCommentRequestParameters(String text,
                                         List<Long> userIds,
                                         List<Long> eventIds,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd) {
        super(text, eventIds, rangeStart, rangeEnd);
        this.userIds = userIds;
    }
}
