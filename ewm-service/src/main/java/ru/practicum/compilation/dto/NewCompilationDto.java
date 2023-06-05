package ru.practicum.compilation.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class NewCompilationDto {
    private Long id;
    private List<Long> events;
    private Boolean pinned;
    @NotBlank
    private String title;
}
