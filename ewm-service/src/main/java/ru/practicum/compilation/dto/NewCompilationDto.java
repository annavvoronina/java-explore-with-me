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
    private boolean pinned;
    @Size(min = 20, message = "минимальная длина 20 символов")
    @Size(max = 255, message = "максимальная длина 255 символов")
    private String title;
}
