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

//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(ReservationController).build();
//    }


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
    }

    @Nested
    class GetUserReservationsTests {

    }
}