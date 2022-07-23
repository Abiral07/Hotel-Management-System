package com.SpringBootProject.hms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roomId_generator")
    @SequenceGenerator(name = "roomId_generator", initialValue = 100, allocationSize = 1,sequenceName = "room_sequence")
    private Long rid;
    @Enumerated(EnumType.STRING)
    private RoomType type;
    private BigDecimal price;
    @Column(columnDefinition = "BIT(1) default '0'")
    private Boolean status;
}
