package ru.practicum.compilation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.events.model.Event;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "compilation")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private Set<Event> events;
    @Column(name = "pinned", nullable = false)
    private Boolean pinned;
    @Column(name = "title", nullable = false, length = 2000)
    private String title;
}
