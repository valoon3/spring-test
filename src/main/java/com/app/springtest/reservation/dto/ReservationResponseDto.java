package com.app.springtest.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDto {
    private long id;
    private long userId;
    private long roomId;
    private String from;
    private String to;
}
