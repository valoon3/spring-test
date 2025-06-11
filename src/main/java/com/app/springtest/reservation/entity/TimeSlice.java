package com.app.springtest.reservation.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@AllArgsConstructor
@Getter
public class TimeSlice {
    private LocalTime start;
    private LocalTime end;
}
