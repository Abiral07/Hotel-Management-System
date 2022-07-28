package com.SpringBootProject.hms.service;

import com.SpringBootProject.hms.dto.requestDto.ReservationRequest;
import com.SpringBootProject.hms.dto.responseDto.ReservationResponse;
import com.SpringBootProject.hms.exceptions.CustomException;

import java.util.List;

public interface ReservationService {
    /**
     * @param reservationDto :reservationdto
     * @param token          :to extract current user
     * @return :reservation details
     * @throws CustomException ex
     */
    ReservationResponse addReservation(ReservationRequest reservationDto, String token) throws CustomException;

    /**
     * @param id             :id of reservation to be updated
     * @param reservationDto :updated value
     * @return updated reservation details
     * @throws CustomException ex
     */
    ReservationResponse updateReservation(Long id, ReservationRequest reservationDto) throws CustomException;

    /**
     * @return all reservation
     */
    List<ReservationResponse> getAllReservation();

    /**
     * @param id :id reservation
     * @return reservation details
     */
    ReservationResponse getReservationById(Long id);

    /**
     * @param userName fullname of user
     * @return user's reservation
     */
    List<ReservationResponse> getReservationOfUser(String userName);

    /**
     * @return reservation details
     */
    List<ReservationResponse> getReservationOfToday();

    /**
     * @param roomId :id of the room
     * @return :updated reservation details
     */
    ReservationResponse checkout(Long reservationId, Long roomId, Long userId);

    ReservationResponse checkIn(Long reservationId, Long roomId, Long userI);
}
