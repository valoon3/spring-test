package com.app.springtest.reservation.service;

import com.app.springtest.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final  ReservationRepository roomReservationRepository;

}
