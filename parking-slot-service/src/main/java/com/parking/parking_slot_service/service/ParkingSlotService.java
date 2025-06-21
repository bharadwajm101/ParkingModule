package com.parking.parking_slot_service.service;
import com.parking.parking_slot_service.dto.ParkingSlotDTO;
import com.parking.parking_slot_service.dto.OccupancyStatusDTO;
import java.util.List;

public interface ParkingSlotService {
    ParkingSlotDTO addParkingSlot(ParkingSlotDTO parkingSlotDTO);
    ParkingSlotDTO updateParkingSlot(Long slotId, ParkingSlotDTO parkingSlotDTO);
    void removeParkingSlot(Long slotId);
    List<ParkingSlotDTO> getAllSlots();
    List<ParkingSlotDTO> getAvailableSlots();
    OccupancyStatusDTO getOccupancyStatus();
    List<ParkingSlotDTO> getSlotsByType(String type);
    ParkingSlotDTO getSlotById(Long slotId);
    ParkingSlotDTO changeOccupancyStatus(Long slotId, boolean isOccupied);
}