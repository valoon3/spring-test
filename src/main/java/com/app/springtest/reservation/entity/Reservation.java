package com.app.springtest.reservation.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
        name = "reservation",
        indexes = {

        }
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private long roomId;

    @Embedded
    private TimeSlice time;

    public static Reservation create(long userId, long roomId, TimeSlice time) {
        Reservation reservation = new Reservation();
        reservation.userId = userId;
        reservation.roomId = roomId;
        reservation.time = time;
        return reservation;
    }

}
