package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statistic.dto.StatisticRequestDto;
import ru.practicum.statistic.dto.StatisticResponseDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping
public class StatController {
    private final StatService statService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/hit")
    public StatisticResponseDto createStat(@RequestBody StatisticRequestDto statRequestDto) {
        return statService.createQuery(statRequestDto);
    }

    @GetMapping("/stats")
    public List<StatisticResponseDto> getStat(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                              @RequestParam(name = "uris") List<String> uris,
                                              @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
        if (start.isAfter(end)) {
            throw new BadRequestException("Дата начала не может быть позже даты окончания");
        }

        return statService.getStat(start, end, uris, unique);
    }
}
