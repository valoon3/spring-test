package com.app.springtest.reservation.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TimeSliceTest {

    @Nested
    class ServiceTimeTests {
        @Test
        @DisplayName("운영시간 내에 예약이 가능하다")
        void 운영시간_내에_예약이_가능하다() {
            // Given (주어진 상황)
            LocalTime start = LocalTime.of(8, 0);
            LocalTime end = LocalTime.of(10, 30);
            // When (무엇을 할 때)

            TimeSlice timeSlice = TimeSlice.create(start, end);

            // Then (결과)
            assertNotNull(timeSlice);
        }

        @Test
        @DisplayName("운영시간이 아닌경우 예외가 발생한다")
        void 운영시간_이외에_예약시_예외가_발생한다() {
            // Given (주어진 상황)
            LocalTime start = LocalTime.of(7, 0);
            LocalTime end = LocalTime.of(10, 30);

            // When (무엇을 할 때)
            Exception exception = assertThrows(IllegalArgumentException.class, () -> TimeSlice.create(start, end));

            // Then (결과)
            assertEquals("Reservation time must be between 08:00 and 23:00", exception.getMessage());
        }

        @Test
        @DisplayName("점심시간이 포함된 예약은 예외가 발생한다")
        void 점심시간이_포함된_예약은_예외가_발생한다() {
            // Given (주어진 상황)
            LocalTime start = LocalTime.of(11, 30);
            LocalTime end = LocalTime.of(12, 30);

            // When (무엇을 할 때)
            Exception exception = assertThrows(IllegalArgumentException.class, () -> TimeSlice.create(start, end));

            // Then (결과)
            assertEquals("Reservation cannot overlap with lunch break (12:00 - 13:00)", exception.getMessage());
        }

        @Test
        @DisplayName("점심시간과 완전히 겹치는 예약은 예외가 발생한다")
        void 점심시간과_완전히_겹치는_예약은_예외가_발생한다() {
            // Given (주어진 상황)
            LocalTime start = LocalTime.of(12, 0);
            LocalTime end = LocalTime.of(13, 0);

            // When (무엇을 할 때)
            Exception exception = assertThrows(IllegalArgumentException.class, () -> TimeSlice.create(start, end));

            // Then (결과)
            assertEquals("Reservation cannot overlap with lunch break (12:00 - 13:00)", exception.getMessage());
        }

        @Test
        @DisplayName("점심시간이 포함되는 예약은 예외가 발생한다")
        void 점심시간이_포함되는_예약은_예외가_발생한다() {
            // Given (주어진 상황)
            LocalTime start = LocalTime.of(10, 00);
            LocalTime end = LocalTime.of(13, 30);

            // When (무엇을 할 때)
            Exception exception = assertThrows(IllegalArgumentException.class, () -> TimeSlice.create(start, end));

            // Then (결과)
            assertThatThrownBy(() -> TimeSlice.create(start, end))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Reservation cannot overlap with lunch break (12:00 - 13:00)");
        }
    }

}