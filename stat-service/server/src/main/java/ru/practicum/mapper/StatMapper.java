package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.StatisticRequestDto;
import ru.practicum.StatisticResponseDto;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class StatMapper {
    public static final DateTimeFormatter DATA_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatisticResponseDto toStatDto(Stat stat) {
        return StatisticResponseDto.builder()
                .app(stat.getApp())
                .uri(stat.getUri())
                .build();
    }

    public Stat toStat(StatisticRequestDto statDto) {
        return new Stat(
                statDto.getId(),
                statDto.getApp(),
                statDto.getUri(),
                statDto.getIp(),
                LocalDateTime.now().withNano(0));
    }
}
