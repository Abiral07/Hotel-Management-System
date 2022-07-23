package com.SpringBootProject.hms.service.Impl;

import com.SpringBootProject.hms.dto.requestDto.ReservationRequest;
import com.SpringBootProject.hms.dto.responseDto.ReservationResponse;
import com.SpringBootProject.hms.dtoToEntity.ReservationEntityToResponse;
import com.SpringBootProject.hms.entity.Reservation;
import com.SpringBootProject.hms.entity.Room;
import com.SpringBootProject.hms.exceptions.CustomException;
import com.SpringBootProject.hms.repo.ReservationRepo;
import com.SpringBootProject.hms.repo.RoomRepo;
import com.SpringBootProject.hms.repo.UserRepo;
import com.SpringBootProject.hms.service.ReservationService;
import com.SpringBootProject.hms.utils.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationRepo reservationRepo;
    @Autowired
    private ReservationEntityToResponse reservationConvertor;
    @Autowired
    private RoomRepo roomRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    @Transactional
    public ReservationResponse addReservation(ReservationRequest reservationDto, String token) throws CustomException {
        Reservation reservation = Reservation.builder()
                .inDate(reservationDto.getInDate())
                .outDate(reservationDto.getOutDate())
                .type(reservationDto.getType())
                .paymentGateway(reservationDto.getPaymentGateway())
                .build();
        Room rooms = roomRepo.findByType(reservationDto.getType().toString());
        if(Objects.nonNull(rooms)) { //TODO: check if room is empty
            reservation.setRoom(rooms);
        }
        else
            throw new CustomException("Sorry all the room of type "+reservationDto.getType()+" are currently unavailable.");
        int day = Period.between(reservationDto.getInDate(),reservationDto.getOutDate()).getDays();
        BigDecimal rate = reservation.getRoom().getPrice();
        BigDecimal total_price = rate.multiply(BigDecimal.valueOf(day));
        reservation.setTotalPrice(total_price);
        reservation.setPaymentStatus(true);

        reservation.setUser(userRepo.findByUserName(jwtTokenUtil.getUsernameFromToken(token)));
        roomRepo.updateRoomStatus(reservation.getRoom().getRid(), Boolean.TRUE);
        return reservationConvertor.entityToResponse(reservationRepo.save(reservation));
    }

    @Override
    @Transactional
    public ReservationResponse updateReservation(Long id, ReservationRequest reservation) throws CustomException {
        Reservation updatedReservation = reservationRepo.findById(id).get();

        if (Objects.nonNull(reservation.getType()) && !"".equalsIgnoreCase(reservation.getType().toString())) {
            Room newroom = roomRepo.findByType(reservation.getType().toString());
            if(Objects.nonNull(newroom)) { //TODO: check if room is empty Optional<Room> newroom, newroom.idPresent()
                //release old booked room and add new room
                roomRepo.updateRoomStatus(updatedReservation.getRoom().getRid(),Boolean.FALSE);
                updatedReservation.setRoom(newroom);
                //book new room by setting status 1/true/booked
                roomRepo.updateRoomStatus(newroom.getRid(),Boolean.TRUE);
                updatedReservation.setType(reservation.getType());
                int day = Period.between(reservation.getInDate(),reservation.getOutDate()).getDays();
                BigDecimal rate = newroom.getPrice();
                BigDecimal total_price = rate.multiply(BigDecimal.valueOf(day));
                updatedReservation.setTotalPrice(total_price);
            }
            else
                throw new CustomException("Sorry all the room of type "+reservation.getType()+" are currently unavailable.");
        }
        if (Objects.nonNull(reservation.getInDate())) {
            if ((reservation.getInDate().compareTo(LocalDate.now()))>=0){
                updatedReservation.setInDate(reservation.getInDate());
            }else {
                throw new CustomException("INVALID!!! InDate cant be in past");
            }
        }
        if (Objects.nonNull(reservation.getOutDate())) {
            if ((reservation.getOutDate().compareTo(LocalDate.now()))>0){
                updatedReservation.setOutDate(reservation.getOutDate());
            }else {
                throw new CustomException("INVALID!!!   OutDate should be in future");
            }
        }
        if (Objects.nonNull(reservation.getPaymentGateway()) && !"".equalsIgnoreCase(reservation.getPaymentGateway().toString())) {
            updatedReservation.setPaymentGateway(reservation.getPaymentGateway());
            updatedReservation.setPaymentStatus(true);
        }
//        --------------FOR CHECKING TRANSACTION ROLL BACK UNCOMMENT BELOW----------------------
//        updatedReservation = null;
//        updatedReservation.setType(reservation.getType());

        return reservationConvertor.entityToResponse(reservationRepo.save(updatedReservation));
    }

    @Override
    public List<ReservationResponse> getAllReservation() {
        return reservationConvertor.entityToResponse(reservationRepo.findAll());
    }

    @Override
    public ReservationResponse getReservationById(Long id) {
        return reservationConvertor.entityToResponse(reservationRepo.findById(id).get());
    }

    @Override
    public List<ReservationResponse> getReservationOfUser(String userName) {
        return reservationConvertor.entityToResponse(reservationRepo.findByUser(userName));
    }

    @Override
    public List<ReservationResponse> getReservationOfToday() {
        return reservationConvertor.entityToResponse(reservationRepo.findReservationByDate(LocalDate.now()));
    }
}
