package com.parking.parking_slot_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OccupancyStatusDTO {
    private long occupiedCount;
    private long availableCount;
}
