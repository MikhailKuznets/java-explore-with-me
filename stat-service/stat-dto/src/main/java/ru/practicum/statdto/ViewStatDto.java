package ru.practicum.statdto;

import lombok.Data;

@Data
public class ViewStatDto {
    private String app;
    private String uri;
    private Integer hits;
}