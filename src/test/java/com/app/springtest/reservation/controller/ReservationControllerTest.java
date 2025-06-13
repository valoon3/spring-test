package com.app.springtest.reservation.controller;

import com.app.springtest.reservation.dto.ReservationRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class CreateReservationTests {
        private final String URL = "/api/v1/reservations";

        @Test
        @DisplayName("점심시간에 예약할 경우 500 에러가 발생한다.")
        void 점심시간에_예약할_경우_500에러가_발생한다() throws Exception {

            ReservationRequestDto requestDto = new ReservationRequestDto(1L, 1L, "11:30", "12:30");
            String requestBody = objectMapper.writeValueAsString(requestDto);

            // When & Then
            ResultActions resultActions = mockMvc.perform(post(URL)
                            .contentType("application/json")
                            .content(requestBody)
            );

            resultActions.andExpect(status().isInternalServerError());
        }

        @Test
        @DisplayName("예약 시작시간과 종료시간이 같을 경우 500 에러가 발생한다.")
        void 예약_시작시간과_종료시간이_같을_경우_500에러가_발생한다() throws Exception {

            ReservationRequestDto requestDto = new ReservationRequestDto(1L, 1L, "10:00", "10:00");
            String requestBody = objectMapper.writeValueAsString(requestDto);

            // When & Then
            ResultActions resultActions = mockMvc.perform(post(URL)
                            .contentType("application/json")
                            .content(requestBody)
            );

            resultActions.andExpect(status().isInternalServerError());
        }

        @Test
        @DisplayName("예약은 30분 단위여야한다.")
        void 예약은_30분_단위여야한다() throws Exception {

            ReservationRequestDto requestDto = new ReservationRequestDto(1L, 1L, "10:00", "10:45");
            String requestBody = objectMapper.writeValueAsString(requestDto);

            // When & Then
            ResultActions resultActions = mockMvc.perform(post(URL)
                            .contentType("application/json")
                            .content(requestBody)
            );

            resultActions.andExpect(status().isInternalServerError());
        }

        @Test
        @DisplayName("예약의 시작시간은 0분 또는 30분이어야 한다. (실패)")
        void 예약의_시작시간은_0분_또는_30분이어야한다_실패() throws Exception {

            ReservationRequestDto requestDto = new ReservationRequestDto(1L, 1L, "10:15", "10:45");
            String requestBody = objectMapper.writeValueAsString(requestDto);

            // When & Then
            ResultActions resultActions = mockMvc.perform(post(URL)
                            .contentType("application/json")
                            .content(requestBody)
            );

            resultActions.andExpect(status().isInternalServerError());
        }

        @Test
        @DisplayName("예약의 시작시간은 0분 또는 30분이어야 한다. (성공)")
        void 예약의_시작시간은_0분_또는_30분이어야한다_성공() throws Exception {

            ReservationRequestDto requestDto = new ReservationRequestDto(1L, 1L, "10:00", "10:30");
            String requestBody = objectMapper.writeValueAsString(requestDto);

            // When & Then
            ResultActions resultActions = mockMvc.perform(post(URL)
                    .contentType("application/json")
                    .content(requestBody)
            );

            resultActions.andExpect(status().isOk());
        }

        @Test
        @DisplayName("겹치는 예약은 할 수 없습니다.")
        void 겹치는_예약은_할_수_없습니다() throws Exception {

            ReservationRequestDto requestDto1 = new ReservationRequestDto(1L, 1L, "10:00", "11:30");
            String requestBody1 = objectMapper.writeValueAsString(requestDto1);
            mockMvc.perform(post(URL)
                    .contentType("application/json")
                    .content(requestBody1))
                    .andExpect(status().isOk());

            ReservationRequestDto requestDto2 = new ReservationRequestDto(1L, 1L, "11:00", "11:30");
            String requestBody2 = objectMapper.writeValueAsString(requestDto2);

            // When & Then
            ResultActions resultActions = mockMvc.perform(post(URL)
                            .contentType("application/json")
                            .content(requestBody2)
            );

            resultActions.andExpect(status().isInternalServerError());
        }
    }

    @Nested
    class GetUserReservationsTests {
        @Test
        @DisplayName("사용자의 예약 목록을 조회할 수 있다.")
        void 사용자의_예약_목록을_조회할_수_있다() throws Exception {
            // Given
            long userId = 1L; // 예시 사용자 ID

            // When
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/reservations/users/" + userId)
                            .contentType("application/json")
            );

            // Then
            resultActions.andExpect(status().isOk());
        }

        @Test
        @DisplayName("예약한 회의실이 조회된다")
        void 예약한_회의실이_조회된다() throws Exception {
            // Given
            long userId = 1L; // 예시 사용자 ID

            // 예약 생성
            ReservationRequestDto requestDto1 = new ReservationRequestDto(userId, 1L, "10:00", "10:30");
            String requestBody1 = objectMapper.writeValueAsString(requestDto1);
            mockMvc.perform(
                    post("/api/v1/reservations")
                            .contentType("application/json")
                            .content(requestBody1)
            ).andExpect(status().isOk());

            ReservationRequestDto requestDto2 = new ReservationRequestDto(userId, 1L, "14:00", "15:30");
            String requestBody2 = objectMapper.writeValueAsString(requestDto2);
            mockMvc.perform(
                    post("/api/v1/reservations")
                            .contentType("application/json")
                            .content(requestBody2)
            ).andExpect(status().isOk());

            // When
            ResultActions resultActions = mockMvc.perform(
                    get("/api/v1/reservations/users/" + userId)
                            .contentType("application/json")
            );

            // Then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        String responseBody = result.getResponse().getContentAsString();

                        assertTrue(responseBody.contains("10:00"));
                        assertTrue(responseBody.contains("10:30"));
                        assertTrue(responseBody.contains("14:00"));
                        assertTrue(responseBody.contains("15:30"));
                    });
        }
    }
}