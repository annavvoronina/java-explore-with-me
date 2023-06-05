package ru.practicum.events.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Range(min = -90, max = 90)
    private Float lat;
    @Range(min = -180, max = 180)
    private Float lon;
}
