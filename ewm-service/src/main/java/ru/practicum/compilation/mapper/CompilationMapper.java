package ru.practicum.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.events.mapper.EventMapper;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {
    public CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(compilation.getEvents() != null
                        ? compilation.getEvents().stream().map(EventMapper::toEventShortDto).collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }

    public Compilation toCompilation(Compilation compilation, NewCompilationDto newCompilationDto) {
        compilation.setId(newCompilationDto.getId());
        Optional.ofNullable(newCompilationDto.getTitle()).ifPresent(compilation::setTitle);
        compilation.setPinned(newCompilationDto.isPinned());

        return compilation;
    }
}
