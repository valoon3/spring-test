package com.app.springtest.reservation.repository;

import com.app.springtest.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByUserId(long userId);

    Optional<Reservation> findByRoomId(long roomId);

    List<Reservation> findAllByUserId(long userId);

}
