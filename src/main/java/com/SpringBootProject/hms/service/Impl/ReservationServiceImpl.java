package com.SpringBootProject.hms.service.Impl;

import com.SpringBootProject.hms.dto.requestDto.ReservationRequest;
import com.SpringBootProject.hms.dto.responseDto.ReservationResponse;
import com.SpringBootProject.hms.dtoToEntity.ReservationEntityToResponse;
import com.SpringBootProject.hms.entity.PaymentGateway;
import com.SpringBootProject.hms.entity.Reservation;
import com.SpringBootProject.hms.entity.ReservationStatus;
import com.SpringBootProject.hms.entity.Room;
import com.SpringBootProject.hms.exceptions.CustomException;
import com.SpringBootProject.hms.exceptions.ResourceNotFoundException;
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
    public ReservationResponse addReservation(ReservationRequest reservationDto, String token) throws CustomException {
        Reservation reservation = Reservation.builder()
                .inDate(reservationDto.getInDate())
                .outDate(reservationDto.getOutDate())
                .type(reservationDto.getType())
                .paymentGateway(reservationDto.getPaymentGateway())
                .build();
//        Room rooms = roomRepo.findByType(reservationDto.getType().toString());
        Room rooms = roomRepo.findEmptyRoom(reservationDto.getType().toString());
        if (Objects.nonNull(rooms)) { //TODO: check if room is empty
            reservation.setRoom(rooms);
        } else
            throw new CustomException("Sorry all the room of type " + reservationDto.getType() + " are currently unavailable.");
        int day = Period.between(reservationDto.getInDate(), reservationDto.getOutDate()).getDays();
        BigDecimal rate = reservation.getRoom().getPrice();
        BigDecimal total_price = rate.multiply(BigDecimal.valueOf(day));
        reservation.setTotalPrice(total_price);
        reservation.setReservationStatus(ReservationStatus.BOOKED);
        reservation.setUser(userRepo.findByUserName(jwtTokenUtil.getUsernameFromToken(token)));
        return reservationConvertor.entityToResponse(reservationRepo.save(reservation));
    }

    @Override
    @Transactional
    public ReservationResponse updateReservation(Long id, ReservationRequest reservation) throws CustomException {
        Reservation updatedReservation = reservationRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation", "Id", id));

        if (Objects.nonNull(reservation.getType()) && !"".equalsIgnoreCase(reservation.getType().toString())) {
            Room newroom = roomRepo.findEmptyRoom(reservation.getType().toString());
            if (Objects.nonNull(newroom)) { //TODO: check if room is empty Optional<Room> newroom, newroom.idPresent()
                //release old booked room if it was already booked
                if (updatedReservation.getRoom().getStatus())
                    roomRepo.updateRoomStatus(updatedReservation.getRoom().getRid(), Boolean.FALSE);
                updatedReservation.setRoom(newroom);
                updatedReservation.setType(reservation.getType());
                int day = Period.between(reservation.getInDate(), reservation.getOutDate()).getDays();
                BigDecimal rate = newroom.getPrice();
                BigDecimal total_price = rate.multiply(BigDecimal.valueOf(day));
                updatedReservation.setTotalPrice(total_price);
            } else
                throw new CustomException("Sorry all the room of type " + reservation.getType() + " are currently unavailable.");
        }
        if (Objects.nonNull(reservation.getInDate())) {
            if ((reservation.getInDate().compareTo(LocalDate.now())) >= 0) {
                updatedReservation.setInDate(reservation.getInDate());
            } else {
                throw new CustomException("INVALID!!! InDate cant be in past");
            }
        }
        if (Objects.nonNull(reservation.getOutDate())) {
            if ((reservation.getOutDate().compareTo(LocalDate.now())) > 0) {
                updatedReservation.setOutDate(reservation.getOutDate());
            } else {
                throw new CustomException("INVALID!!!   OutDate should be in future");
            }
        }
        if (Objects.nonNull(reservation.getPaymentGateway()) && !"".equalsIgnoreCase(reservation.getPaymentGateway().toString())) {
            updatedReservation.setPaymentGateway(reservation.getPaymentGateway());
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
        return reservationConvertor.entityToResponse(reservationRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation", "Id", id)));
    }

    @Override
    public List<ReservationResponse> getReservationOfUser(String userName) {
        return reservationConvertor.entityToResponse(reservationRepo.findByUser(userName));
    }

    @Override
    public List<ReservationResponse> getReservationOfToday() {
        return reservationConvertor.entityToResponse(reservationRepo.findReservationByDate(LocalDate.now()));
    }

    @Override
    @Transactional
    public ReservationResponse checkout(Long reservationId, Long roomId, Long userId) {
        Reservation reservation = reservationRepo.findReservation(reservationId, roomId, userId);
        if (reservation.getReservationStatus() == ReservationStatus.CHECKED_IN) {
            reservation.setReservationStatus(ReservationStatus.CHECKED_OUT);
            roomRepo.updateRoomStatus(roomId, Boolean.FALSE);
            return reservationConvertor.entityToResponse(reservationRepo.save(reservation));
        } else {
            throw new CustomException("user not checked in");
        }
    }

    @Override
    @Transactional
    public ReservationResponse checkIn(Long reservationId, Long roomId, Long userId) {
        Reservation reservation = reservationRepo.findReservation(reservationId, roomId, userId);
        Boolean paymentStatus = ValidatePayment(reservation.getTotalPrice(), reservation.getPaymentGateway());
        if (paymentStatus) {
            reservation.setPaymentStatus(true);
            reservation.setReservationStatus(ReservationStatus.CHECKED_IN);
            //book new room by setting status 1/true/booked
            roomRepo.updateRoomStatus(roomId, Boolean.TRUE);
        } else {
            reservation.setReservationStatus(ReservationStatus.CANCELED);
        }
        return reservationConvertor.entityToResponse(reservationRepo.save(reservation));
    }

    private Boolean ValidatePayment(BigDecimal totalPrice, PaymentGateway paymentGateway) {
        return totalPrice.compareTo(paymentGateway.getMoney()) <= 0;
    }
}
