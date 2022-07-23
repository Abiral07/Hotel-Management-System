package com.SpringBootProject.hms.service;

import com.SpringBootProject.hms.dto.requestDto.ReservationRequest;
import com.SpringBootProject.hms.dto.responseDto.ReservationResponse;
import com.SpringBootProject.hms.exceptions.CustomException;

import java.util.List;

public interface ReservationService {
    ReservationResponse addReservation(ReservationRequest reservationDto,String token) throws CustomException;

    ReservationResponse updateReservation(Long id, ReservationRequest reservationDto) throws CustomException;

    List<ReservationResponse> getAllReservation();

    ReservationResponse getReservationById(Long id);

    List<ReservationResponse> getReservationOfUser(String userName);

    List<ReservationResponse> getReservationOfToday();
}
