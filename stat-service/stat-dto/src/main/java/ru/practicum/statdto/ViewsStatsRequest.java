package ru.practicum.statdto;

import lombok.Data;

@Data
public class ViewsStatsRequest {
    private String app;
    private String uri;
    private Integer hits;
}
