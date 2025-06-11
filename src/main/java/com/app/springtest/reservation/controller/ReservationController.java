package com.app.springtest.reservation.controller;

import com.app.springtest.reservation.dto.ReservationRequestDto;
import com.app.springtest.reservation.entity.Reservation;
import com.app.springtest.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/api/reservations")
    public void createReservation(@RequestBody final ReservationRequestDto request) {
        reservationService.createReservation(request);
    }

    @GetMapping("/api/reservations/users/{userId}")
    public List<Reservation> getUserReservations(@PathVariable final long userId) {
        return reservationService.getUserReservations(userId);
    }

    @GetMapping("/api/reservations/rooms/{roomId}")
    public List<RoomReservation> getRoomReservations(@PathVariable final long roomId) {
        /** !!!!!!! 해당 API는 절대 수정하지 마세요 !!!!!!! */
        /** !!!!!!! 해당 회의실의 예약 현황을 조회 하기 위한 코드이니 수정하지 마세요 !!!!!!! */
        return roomReservationRepository.findByRoomId(roomId);
    }
}
