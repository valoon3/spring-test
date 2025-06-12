package com.app.springtest.reservation.controller;

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
            // Given
            String requestBody = """
                    {
                        "userId": 1,
                        "roomId": 1,
                        "from": "11:30",
                        "to": "12:30"
                    }
                    """;

            // When & Then
            ResultActions resultActions = mockMvc.perform(post(URL)
                            .contentType("application/json")
                            .content(requestBody)
            );

            resultActions.andExpect(status().isInternalServerError());
        }
    }

    @Nested
    class GetUserReservationsTests {

    }
}