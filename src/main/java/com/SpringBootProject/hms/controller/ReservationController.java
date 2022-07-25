package com.SpringBootProject.hms.controller;

import com.SpringBootProject.hms.constants.PathConstant;
import com.SpringBootProject.hms.dto.requestDto.ReservationRequest;
import com.SpringBootProject.hms.dto.responseDto.ReservationResponse;
import com.SpringBootProject.hms.exceptions.CustomException;
import com.SpringBootProject.hms.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.List;
import java.util.Set;

@RestController
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @PostMapping(PathConstant.ADD_RESERVATION)
    public ResponseEntity<ReservationResponse> addReservation(@RequestBody ReservationRequest reservationDto, @RequestHeader("Authorization") String bearerToken) throws CustomException {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<ReservationRequest>> violations = validator.validate(reservationDto);
            for (ConstraintViolation<ReservationRequest> violation : violations) {
                throw new CustomException(violation.getMessage());
            }
        }

        return ResponseEntity.ok(reservationService.addReservation(reservationDto, bearerToken.substring(7)));
    }
    @PostMapping(PathConstant.UPDATE_RESERVATION)
    public ResponseEntity<ReservationResponse> updateReservation(@PathVariable("id")Long id,@Valid @RequestBody ReservationRequest reservation) throws CustomException {
        return ResponseEntity.ok(reservationService.updateReservation(id,reservation));
    }

    @GetMapping(PathConstant.GET_ALL_RESERVATION)
    public ResponseEntity<List<ReservationResponse>> getAllReservation(){
        return ResponseEntity.ok(reservationService.getAllReservation());
    }
    @GetMapping(PathConstant.GET_RESERVATION_BY_ID)
    public ResponseEntity<ReservationResponse> getReservationById(@PathVariable("id")Long id){
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }
    @GetMapping(PathConstant.GET_RESERVATION_OF_USER)
    public ResponseEntity<List<ReservationResponse>> getReservationOfUser(@PathVariable("name")String userName){
        return ResponseEntity.ok(reservationService.getReservationOfUser(userName));
    }
    @GetMapping(PathConstant.GET_RESERVATION_OF_TODAY)
    public ResponseEntity<List<ReservationResponse>> getReservationOfToday(){
        return ResponseEntity.ok(reservationService.getReservationOfToday());
    }
}
