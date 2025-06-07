package com.app.springtest.event.service;

import com.app.springtest.event.constant.EventType;
import com.app.springtest.event.entity.Event;
import com.app.springtest.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public void issueEvent(long userId, EventType eventType) {
        Optional<Event> optionalEvent = eventRepository.findByUserIdAndType(userId, eventType);

        if (optionalEvent.isPresent()) {
            throw new IllegalArgumentException("이미 발급된 쿠폰입니다. userId=" + userId + ", type=" + eventType);
        }

        // 2. 쿠폰을 생성하고 저장합니다.
        eventRepository.save(Event.create(userId, eventType));
    }
}
