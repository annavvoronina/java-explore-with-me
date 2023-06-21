package ru.practicum.compilation.dto;

import lombok.*;

import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class NewCompilationDto {
    private Long id;
    private List<Long> events;
    private boolean pinned = false;
    @Size(min = 15, message = "минимальная длина 15 символов")
    @Size(max = 50, message = "максимальная длина 50 символов")
    private String title;
}
