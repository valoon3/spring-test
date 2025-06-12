package com.app.springtest.reservation.entity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@AllArgsConstructor
@Getter
public class TimeSlice {
    @Column(name = "start_time", nullable = false)
    private LocalTime start;

    @Column(name = "end_time", nullable = false)
    private LocalTime end;

    private static final LocalTime LUNCH_START = LocalTime.of(12, 0);
    private static final LocalTime LUNCH_END = LocalTime.of(13, 0);

    private static final LocalTime DINNER_START = LocalTime.of(18, 0);
    private static final LocalTime DINNER_END = LocalTime.of(19, 0);

    private static final LocalTime SERVICE_START = LocalTime.of(8, 0);
    private static final LocalTime SERVICE_END = LocalTime.of(23, 0);

    public static TimeSlice create(LocalTime start, LocalTime end) {
        validateTimeSlice(start, end);
        return new TimeSlice(start, end);
    }

    public boolean overlaps(TimeSlice other) {
        return !(this.end.isBefore(other.start) || this.start.isAfter(other.end));
    }

    private static void validateTimeSlice(LocalTime start, LocalTime end) {
        validateReservationTimeUnit(start, end);
        validateServiceHours(start, end);
        validateReservationTime(start, end);
        validateNoOverlapWithBreaks(start, end);
    }



    private static void validateReservationTime(LocalTime start, LocalTime end) {
        if(end.isBefore(start)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
    }

    private static void validateServiceHours(LocalTime start, LocalTime end) {
        if (start.isBefore(SERVICE_START) || end.isAfter(SERVICE_END)) {
            throw new IllegalArgumentException("Reservation time must be between 08:00 and 23:00");
        }
    }

    private static void validateNoOverlapWithBreaks(LocalTime start, LocalTime end) {
        // 점심시간과 겹치는지 확인
        // 예: 11:30-12:30 (X), 12:30-13:30 (X), 11:30-13:30 (X)
        if (start.isBefore(LUNCH_END) && end.isAfter(LUNCH_START)) {
            throw new IllegalArgumentException("Reservation cannot overlap with lunch break (12:00 - 13:00)");
        }

        // 저녁시간과 겹치는지 확인
        // 예: 17:30-18:30 (X), 18:30-19:30 (X), 17:30-19:30 (X)
        if (start.isBefore(DINNER_END) && end.isAfter(DINNER_START)) {
            throw new IllegalArgumentException("Reservation cannot overlap with dinner break (18:00 - 19:00)");
        }
    }

    private static void validateReservationTimeUnit(LocalTime start, LocalTime end) {
        if (start.getMinute() % 30 != 0 || end.getMinute() % 30 != 0) {
            throw new IllegalArgumentException("Reservation time must be in 30-minute intervals");
        }
    }
}
