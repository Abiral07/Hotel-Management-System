package com.SpringBootProject.hms.dto.responseDto;

import com.SpringBootProject.hms.entity.PaymentGateway;
import com.SpringBootProject.hms.entity.ReservationStatus;
import com.SpringBootProject.hms.entity.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {
    private Long reservationId;
    private Long userId;
    private Long roomId;
    @FutureOrPresent
    private LocalDate inDate;
    @Future
    private LocalDate outDate;
    @Enumerated(EnumType.STRING)
    private RoomType type;
    private BigDecimal totalPrice;
    @Enumerated(EnumType.STRING)
    private PaymentGateway paymentGateway;
    private Boolean paymentStatus;
    private ReservationStatus reservationStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
