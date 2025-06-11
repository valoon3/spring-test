package com.app.springtest.reservation.controller;

import com.app.springtest.reservation.dto.ReservationRequestDto;
import com.app.springtest.reservation.dto.ReservationResponseDto;
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

    @PostMapping()
    public void createReservation(@RequestBody final ReservationRequestDto request) {
        reservationService.createReservation(request);
    }

    @GetMapping("/users/{userId}")
    public List<ReservationResponseDto> getUserReservations(@PathVariable final long userId) {
        return reservationService.getUserReservations(userId);
    }

//    @GetMapping("/{roomId}")
//    public List<ReservationResponseDto> getRoomReservations(@PathVariable final long roomId) {
//        return roomReservationRepository.findByRoomId(roomId);
//    }
}
