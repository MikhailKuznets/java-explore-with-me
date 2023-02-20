package ru.practicum.mainservice.controllers.publiccontrollers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import java.util.Collection;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class CompilationPublicController {
    private final CompilationService compilationService;

    @GetMapping("/compilations")
    public ResponseEntity<Collection<CompilationDto>> getPublicAllCompilation(
            @RequestParam(required = false) Boolean pinned) {
        log.info("GET-request was received at '/compilations' . " +
                "Get public information about all compilations with pinned = {}.", pinned);
        return new ResponseEntity<>(compilationService.getPublicAllCompilation(pinned), HttpStatus.OK);
    }

    @GetMapping("/compilations/{compId}")
    public ResponseEntity<CompilationDto> getPublicCompilationById(
            @PathVariable @Positive Long compId) {
        log.info("GET-request was received at '/compilations/{}' . " +
                "Get public information about the compilation with compId = {}.", compId, compId);
        return new ResponseEntity<>(compilationService.getPublicCompilationById(compId), HttpStatus.OK);
    }

}
