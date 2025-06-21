package com.parking.parking_slot_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSlotDTO {
    private Long slotId;
    private String type;
    private boolean isOccupied;
    private String location;
}