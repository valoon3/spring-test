package com.app.springtest.event.dto;

import com.app.springtest.event.constant.EventType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UseEventResponseDto {
    private long originalPrice;
    private long discountAmount;
    private long finalPrice;
    private EventType couponType;
}
