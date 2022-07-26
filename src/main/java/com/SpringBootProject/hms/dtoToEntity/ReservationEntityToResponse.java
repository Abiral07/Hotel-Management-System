package com.SpringBootProject.hms.dtoToEntity;

import com.SpringBootProject.hms.dto.responseDto.ReservationResponse;
import com.SpringBootProject.hms.entity.Reservation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationEntityToResponse {
    public ReservationResponse entityToResponse(Reservation savedEntity) {
        return ReservationResponse.builder()
                .reservationId(savedEntity.getReservationId())
                .userId(savedEntity.getUser().getUid())
                .roomId(savedEntity.getRoom().getRid())
                .inDate(savedEntity.getInDate())
                .outDate(savedEntity.getOutDate())
                .type(savedEntity.getType())
                .totalPrice(savedEntity.getTotalPrice())
                .paymentGateway(savedEntity.getPaymentGateway())
                .paymentStatus(savedEntity.getPaymentStatus())
                .createdAt(savedEntity.getCreatedAt())
                .updatedAt(savedEntity.getUpdatedAt())
                .build();
    }

    public List<ReservationResponse> entityToResponse(List<Reservation> savedEntities) {
        return savedEntities.stream().map(this::entityToResponse).collect(Collectors.toList());
    }
}
