package com.app.springtest.event.service;


import com.app.springtest.event.constant.EventType;
import com.app.springtest.event.entity.Event;
import com.app.springtest.event.repository.EventRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    // @Mock: Mockito가 가짜(Mock) 객체를 생성합니다.
    @Mock
    private EventRepository eventRepository;

    // @InjectMocks: @Mock으로 생성된 가짜 객체를 실제 객체에 주입합니다.
    @InjectMocks
    private EventService eventService;

    @Test
    public void EventService_가_정상적으로_주입되어야한다() {
        assertNotNull(eventService);
    }

    @Test
    public void 하나의_이벤트에_중복참여할경우_한번만_성공해야한다() {
        // Arrange (준비)
        Event firstEvent = Event.create(1l, EventType.DISCOUNT_10_PERCENT);
        Event secondEvent = Event.create(1l, EventType.DISCOUNT_10_PERCENT);

        // Act (실행)

        // Assert (단언)
    }

}