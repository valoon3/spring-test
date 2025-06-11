package com.app.springtest.reservation.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDto {
    private long roomId;
    private long userId;
    private String from;
    private String to;
}
