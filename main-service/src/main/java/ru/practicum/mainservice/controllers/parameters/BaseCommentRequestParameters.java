package ru.practicum.mainservice.controllers.parameters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseCommentRequestParameters implements TimeCheckable {
    protected String text;
    protected List<Long> eventIds;
    protected LocalDateTime rangeStart;
    protected LocalDateTime rangeEnd;

    @Override
    public void checkTime() {
        if (this.rangeStart == null) {
            this.rangeStart = LocalDateTime.now();
        }
        if (this.rangeEnd == null) {
            // Say Hello WarHammer 40.000 =)
            this.rangeEnd = LocalDateTime.now().plusYears(40_000);
        }
    }

}