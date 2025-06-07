package com.app.springtest.event.controller;

import com.app.springtest.event.dto.IssueEventRequestDto;
import com.app.springtest.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event")
public class EventController {

    private final EventService eventService;

    @PostMapping("/users/{userId}/issue")
    public void issueEvent(
            @PathVariable final long userId,
            @RequestBody final IssueEventRequestDto request
    ) {
        eventService.issueEvent(userId, request.getEventType());
    }

}
