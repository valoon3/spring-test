package com.app.springtest.event.dto;

import com.app.springtest.event.constant.EventType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueEventRequestDto {

    private EventType eventType;

}
