package com.app.springtest.event.repository;

import com.app.springtest.event.constant.EventType;
import com.app.springtest.event.entity.Event;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    public Optional<Event> findById(Long id);

    public Optional<Event> findByUserIdAndType(Long userId, EventType eventType);


}
