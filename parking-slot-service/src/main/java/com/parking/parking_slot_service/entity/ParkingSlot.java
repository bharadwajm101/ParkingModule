package com.parking.parking_slot_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long slotId;

    @Column(nullable = false)
    private String type; // 2W or 4W

    @Column(nullable = false)
    private boolean isOccupied;

    @Column(nullable = false)
    private String location;
}