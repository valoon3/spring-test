package com.app.springtest.reservation.service;

import com.app.springtest.reservation.dto.ReservationRequestDto;
import com.app.springtest.reservation.dto.ReservationResponseDto;
import com.app.springtest.reservation.entity.Reservation;
import com.app.springtest.reservation.entity.TimeSlice;
import com.app.springtest.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository roomReservationRepository;

    @Transactional
    public void createReservation(ReservationRequestDto request) {
        List<Reservation> reservationList = roomReservationRepository.findAllByUserId(request.getUserId());
        LocalTime startTime = LocalTime.parse(request.getFrom(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime endTime = LocalTime.parse(request.getTo(), DateTimeFormatter.ofPattern("HH:mm"));
        TimeSlice timeSlice = TimeSlice.create(startTime, endTime);

        for (Reservation reservation : reservationList) {
            if (timeSlice.overlaps(reservation.getTime())) {
                throw new IllegalArgumentException("예약 시간이 겹칩니다.");
            }
        }

        Reservation reservation = Reservation.create(request.getUserId(), request.getRoomId(), timeSlice);
        roomReservationRepository.save(reservation);
    }

    public List<ReservationResponseDto> getUserReservations(long userId) {

        List<Reservation> reservations = roomReservationRepository.findAllByUserId(userId);

        return reservations.stream()
                .map(reservation -> new ReservationResponseDto(
                        reservation.getId(),
                        reservation.getUserId(),
                        reservation.getRoomId(),
                        reservation.getTime().getStart().format(DateTimeFormatter.ofPattern("HH:mm")),
                        reservation.getTime().getEnd().format(DateTimeFormatter.ofPattern("HH:mm"))
                ))
                .toList();
    }
}
