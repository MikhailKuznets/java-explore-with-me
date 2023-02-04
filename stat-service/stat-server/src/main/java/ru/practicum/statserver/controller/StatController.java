package ru.practicum.statserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statdto.RequestHitDto;
import ru.practicum.statdto.RequestViewStatDto;
import ru.practicum.statserver.model.Hit;
import ru.practicum.statserver.service.StatService;

import java.time.LocalDateTime;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatController {
    private final StatService statService;

    @PostMapping("/hit")
    public ResponseEntity<Hit> saveHit(@RequestBody RequestHitDto requestHitDto) {
        log.info("POST-request was received at /hit . Data: {}", requestHitDto);
        return new ResponseEntity<>(statService.saveHit(requestHitDto, LocalDateTime.now()),
                HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStat(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end,
            @RequestParam(required = false) String[] uris,
            @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("GET-request was received at /stats? start= {} &end= {} &uris= {} &unique= {} .",
                start, end, uris, unique);
        return new ResponseEntity<>(statService.getStat(RequestViewStatDto.builder()
                .start(start)
                .end(end)
                .uris(uris)
                .unique(unique).build()),
                HttpStatus.OK);
    }

}
