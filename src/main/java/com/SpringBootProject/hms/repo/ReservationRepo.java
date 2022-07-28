package com.SpringBootProject.hms.repo;

import com.SpringBootProject.hms.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Long> {
    @Query("select r from Reservation r join r.user u where u.userName = :userName")
    List<Reservation> findByUser(String userName);

    @Query(value = "SELECT * FROM hms.reservation as rv WHERE in_date BETWEEN DATE_ADD(:today, INTERVAL -10 HOUR) AND DATE_ADD(:today, INTERVAL 1 DAY)", nativeQuery = true)
    List<Reservation> findReservationByDate(LocalDate today);

    @Query(value = "select * from hms.reservation as r where r.reservation_id = :reservationId and r.rid = :roomId and r.uid = :userId", nativeQuery = true)
    Reservation findReservation(Long reservationId, Long roomId, Long userId);
}
