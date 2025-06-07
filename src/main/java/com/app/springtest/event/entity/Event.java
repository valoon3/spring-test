package com.app.springtest.event.entity;

import com.app.springtest.event.constant.EventType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(
        name = "event",
        indexes = {

        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_event_user_id_type",
                        columnNames = {"userId", "type"}
                )
        }
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType type;

    private final LocalDateTime issuedAt = LocalDateTime.now();

    public static Event create(long userId, EventType type) {
        Event event = new Event();
        event.userId = userId;
        event.type = type;
        return event;
    }

}
