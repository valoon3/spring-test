package com.app.springtest.reservation.service;

import com.app.springtest.reservation.dto.ReservationRequestDto;
import com.app.springtest.reservation.dto.ReservationResponseDto;
import com.app.springtest.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final  ReservationRepository roomReservationRepository;

    public void createReservation(ReservationRequestDto request) {

    }

    public List<ReservationResponseDto> getUserReservations(long userId) {
        return null;
    }
}
