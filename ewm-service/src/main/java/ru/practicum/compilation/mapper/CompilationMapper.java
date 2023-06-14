package ru.practicum.compilation.mapper;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.events.mapper.EventMapper;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(compilation.getEvents() != null
                        ? compilation.getEvents().stream().map(EventMapper::toEventShortDto).collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }

    public static Compilation toCompilation(Compilation compilation, NewCompilationDto newCompilationDto) {
        compilation.setId(newCompilationDto.getId());
        Optional.ofNullable(newCompilationDto.getTitle()).ifPresent(compilation::setTitle);
        compilation.setPinned(newCompilationDto.isPinned());

        return compilation;
    }
}
