package ru.practicum.events.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import ru.practicum.category.model.Category;
import ru.practicum.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "annotation", length = 3000)
    private String annotation;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Formula("(SELECT COUNT(r.id) FROM Request r WHERE r.event_id = id AND r.status = 'CONFIRMED')")
    private Long confirmedRequests;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "description", length = 7000)
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;
    @Column(name = "lat")
    private Float lat;
    @Column(name = "lon")
    private Float lon;
    @Column(name = "paid")
    private Boolean paid;
    @Column(name = "participant_limit")
    private Long participantLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;
    @Column(name = "title", length = 120)
    private String title;
    @Transient
    private Long views;
}
