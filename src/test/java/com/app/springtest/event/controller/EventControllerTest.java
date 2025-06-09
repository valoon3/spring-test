package com.app.springtest.event.controller;

import com.app.springtest.event.constant.EventType;
import com.app.springtest.event.dto.IssueEventRequestDto;
import com.app.springtest.event.entity.Event;
import com.app.springtest.event.repository.EventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EventControllerTest {

    @Autowired
    private EventController eventController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventRepository eventRepository;

    // 각 테스트가 끝난 후 데이터베이스를 정리합니다.
    @AfterEach
    void tearDown() {
        eventRepository.deleteAllInBatch();
    }


    @Test
    @DisplayName("이벤트 요청이 동시에 들어와도 한 번만 성공해야 한다.")
    public void 같은_이벤트를_여러번_참석할_경우_한번만_지원_성공() throws InterruptedException {
        // Arrange (준비)
        final int numberOfThreads = 100;
        final long userId = 1L;
        final EventType eventType = EventType.DISCOUNT_1000_WON;

        // 동시 실행을 위한 ExecutorService 와 스레드 수를 설정합니다.
        final ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        // 모든 스레드가 작업을 마칠 때까지 대기하기 위한 CountDownLatch 입니다.
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        IssueEventRequestDto request = new IssueEventRequestDto(eventType);

        // Act (실행)
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    // MockMvc를 사용하여 API 호출을 시도합니다.
                    mockMvc.perform(post("/api/v1/event/users/" + userId + "/issue")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    );
                } catch (Exception e) {
                    // 예외가 발생하면 테스트를 실패로 처리합니다.
                    fail("API 호출 중 예외 발생: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        // 모든 스레드가 끝날 때까지 대기합니다.
        latch.await();

        // Assert (단언)
        long issuedCouponCount = eventRepository.count();
        assertThat(issuedCouponCount).isEqualTo(1);
    }

    @Test
    @DisplayName("이벤트가 사용된 이후에는 동일한 이벤트에 지원할 수 있다.")
    public void 이벤트가_사용된_이후에는_동일한_이벤트에_지원할_수_있다() throws Exception {
        // Arrange (준비)
        final long userId = 1L;
        final EventType eventType = EventType.DISCOUNT_10_PERCENT;

        IssueEventRequestDto request = new IssueEventRequestDto(eventType);

        // Act (실행)
        mockMvc.perform(post("/api/v1/event/users/" + userId + "/issue")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // 발급된 이벤트 삭제
        Event submittedEvent = eventRepository.findByUserIdAndType(userId, eventType).get();
        eventRepository.delete(submittedEvent);

        assertDoesNotThrow(() -> {
            mockMvc.perform(post("/api/v1/event/users/" + userId + "/issue")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            );
        });


        // Assert (단언)
        long totalCouponCount = eventRepository.count();
        assertThat(totalCouponCount).isEqualTo(1);
    }

}