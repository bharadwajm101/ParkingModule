package com.parking.parking_slot_service.controller;



import com.parking.parking_slot_service.dto.OccupancyStatusDTO;
import com.parking.parking_slot_service.dto.ParkingSlotDTO;
import com.parking.parking_slot_service.service.ParkingSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
public class ParkingSlotController {

    private final ParkingSlotService service;

    @PostMapping
    public ResponseEntity<ParkingSlotDTO> addSlot(@RequestBody ParkingSlotDTO dto) {
        return ResponseEntity.ok(service.addParkingSlot(dto));
    }

    @PutMapping("/{slotId}")
    public ResponseEntity<ParkingSlotDTO> updateSlot(@PathVariable Long slotId, @RequestBody ParkingSlotDTO dto) {
        return ResponseEntity.ok(service.updateParkingSlot(slotId, dto));
    }

    @DeleteMapping("/{slotId}")
    public ResponseEntity<Void> removeSlot(@PathVariable Long slotId) {
        service.removeParkingSlot(slotId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ParkingSlotDTO>> getAllSlots() {
        return ResponseEntity.ok(service.getAllSlots());
    }

    @GetMapping("/available")
    public ResponseEntity<List<ParkingSlotDTO>> getAvailableSlots() {
        return ResponseEntity.ok(service.getAvailableSlots());
    }

    @GetMapping("/occupancy-status")
    public ResponseEntity<OccupancyStatusDTO> getOccupancyStatus() {
        return ResponseEntity.ok(service.getOccupancyStatus());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<ParkingSlotDTO>> getSlotsByType(@PathVariable String type) {
        return ResponseEntity.ok(service.getSlotsByType(type));
    }

    @GetMapping("/{slotId}")
    public ResponseEntity<ParkingSlotDTO> getSlotById(@PathVariable Long slotId) {
        return ResponseEntity.ok(service.getSlotById(slotId));
    }

    @PatchMapping("/{slotId}/occupancy")
    public ResponseEntity<ParkingSlotDTO> changeOccupancyStatus(
            @PathVariable Long slotId,
            @RequestParam boolean isOccupied) {
        return ResponseEntity.ok(service.changeOccupancyStatus(slotId, isOccupied));
    }
}