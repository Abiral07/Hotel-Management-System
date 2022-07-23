package com.SpringBootProject.hms.dto.requestDto;

import com.SpringBootProject.hms.entity.PaymentGateway;
import com.SpringBootProject.hms.entity.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequest {
    @FutureOrPresent
    @NotNull(message = "inDate cant be null")
    private LocalDate inDate = LocalDate.now();

    @Future
    @NotNull(message = "outDate cant be null")
    private LocalDate outDate;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Room type cant be null")
    private RoomType type;

    @NotNull(message = "paymentGateway cant be null")
    @Enumerated(EnumType.STRING)
    private PaymentGateway paymentGateway;
}
