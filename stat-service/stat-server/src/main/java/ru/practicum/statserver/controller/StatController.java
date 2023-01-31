package ru.practicum.statserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@Slf4j
public class StatController {
    @PostMapping("/hit")
    public ResponseEntity<Object> saveHit(Object object) {
        log.info("POST-request was received at /hit . Data: {}", object);
        return new ResponseEntity<>(HttpStatus.CREATED);
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
