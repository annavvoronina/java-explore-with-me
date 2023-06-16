package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatisticRequestDto;
import ru.practicum.client.StatisticClient;

@Validated
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping
public class StatController {
    private final StatisticClient statisticClient;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/hit")
    public ResponseEntity<Object> createStat(@RequestBody StatisticRequestDto statisticRequestDto) {
        return statisticClient.createStat(statisticRequestDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStat(@RequestParam(name = "start") String start,
                                          @RequestParam(name = "end") String end,
                                          @RequestParam(name = "uris", required = false) String[] uris,
                                          @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
        return statisticClient.getStat(start, end, uris, unique);
    }
}
