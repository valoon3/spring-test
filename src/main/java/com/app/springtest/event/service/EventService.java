package com.app.springtest.event.service;

import com.app.springtest.event.constant.EventType;
import com.app.springtest.event.dto.UseEventResponseDto;
import com.app.springtest.event.entity.Event;
import com.app.springtest.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
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

    @Transactional
    public UseEventResponseDto useEvent(long userId, long price) {
        validOriginalPrice(price);

        List<Event> events = eventRepository.findAllByUserId(userId);

        List<Event> validEvents = getValidEvents(price, events);
        if(validEvents.isEmpty()) {
            throw new IllegalArgumentException("사용 가능한 쿠폰이 없습니다. userId=" + userId);
        }

        Event bestEvent = getBestEvent(price, events);
        long discountAmount = bestEvent.getType().calculateDiscount(price);

        eventRepository.delete(bestEvent);

        return new UseEventResponseDto(
                price,
                discountAmount,
                price - discountAmount,
                bestEvent.getType()
        );
    }

    private void validOriginalPrice(long price) {
        if (price < 10) {
            throw new IllegalArgumentException("최소 금액은 10원 이상이어야 합니다.");
        }
        if (price % 10 != 0) {
            throw new IllegalArgumentException("금액은 10원 단위여야 합니다.");
        }
    }

    private List<Event> getValidEvents(long price, List<Event> events) {
        return events.stream()
                .filter(e -> e.getType().calculateDiscount(price) != -1)
                .toList();
    }

    private Event getBestEvent(long price, List<Event> events) {
        return events.stream()
                .sorted(
                        Comparator.comparingInt((Event e) -> e.getType().calculateDiscount(price))
                                .reversed()
                                .thenComparing(Event::getIssuedAt)
                )
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("사용 가능한 쿠폰이 없습니다."));
    }
}
