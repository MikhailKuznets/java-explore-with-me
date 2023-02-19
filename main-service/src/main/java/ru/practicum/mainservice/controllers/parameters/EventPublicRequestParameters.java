package ru.practicum.mainservice.controllers.parameters;

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
public class EventPublicRequestParameters {
    private String text;
    private List<Long> catIds;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private EventRequestSort sort;

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

