package ru.practicum.statserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.statserver.mapper.RequestHitDto2;
import ru.practicum.statserver.model.Hit;
import ru.practicum.statserver.service.StatService;

import java.time.LocalDateTime;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatController {
    private final StatService statService;

    @PostMapping("/hit")
    public ResponseEntity<Hit> saveHit(RequestHitDto2 requestHitDto) {
        log.info("POST-request was received at /hit . Data: {}", requestHitDto);
        return new ResponseEntity<>(statService.saveHit(requestHitDto), HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end,
            @RequestParam(required = false) String[] uris,
            @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("GET-request was received at /stats? start= {} &end= {} &uris= {} &unique= {} .",
                start, end, uris, unique);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
