package com.parking.parking_slot_service.service.impl;


import com.parking.parking_slot_service.dto.OccupancyStatusDTO;
import com.parking.parking_slot_service.dto.ParkingSlotDTO;
import com.parking.parking_slot_service.entity.ParkingSlot;
import com.parking.parking_slot_service.repository.ParkingSlotRepository;
import com.parking.parking_slot_service.service.ParkingSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParkingSlotServiceImpl implements ParkingSlotService {

    private final ParkingSlotRepository repository;

    @Override
    public ParkingSlotDTO addParkingSlot(ParkingSlotDTO dto) {
        ParkingSlot slot = ParkingSlot.builder()
                .type(dto.getType())
                .isOccupied(dto.isOccupied())
                .location(dto.getLocation())
                .build();
        ParkingSlot saved = repository.save(slot);
        dto.setSlotId(saved.getSlotId());
        return dto;
    }

    @Override
    public ParkingSlotDTO updateParkingSlot(Long slotId, ParkingSlotDTO dto) {
        ParkingSlot slot = repository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));
        slot.setType(dto.getType());
        slot.setOccupied(dto.isOccupied());
        slot.setLocation(dto.getLocation());
        repository.save(slot);
        dto.setSlotId(slotId);
        return dto;
    }

    @Override
    public void removeParkingSlot(Long slotId) {
        repository.deleteById(slotId);
    }

    @Override
    public List<ParkingSlotDTO> getAllSlots() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParkingSlotDTO> getAvailableSlots() {
        return repository.findByIsOccupied(false).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OccupancyStatusDTO getOccupancyStatus() {
        long occupied = repository.countByIsOccupied(true);
        long available = repository.countByIsOccupied(false);
        return new OccupancyStatusDTO(occupied, available);
    }

    @Override
    public List<ParkingSlotDTO> getSlotsByType(String type) {
        return repository.findByTypeIgnoreCase(type).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ParkingSlotDTO getSlotById(Long slotId) {
        ParkingSlot slot = repository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));
        return toDTO(slot);
    }

    @Override
    public ParkingSlotDTO changeOccupancyStatus(Long slotId, boolean isOccupied) {
        ParkingSlot slot = repository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));
        slot.setOccupied(isOccupied);
        ParkingSlot updated = repository.save(slot);
        return toDTO(updated);
    }

    // Helper method to convert entity to DTO
    private ParkingSlotDTO toDTO(ParkingSlot slot) {
        return ParkingSlotDTO.builder()
                .slotId(slot.getSlotId())
                .type(slot.getType())
                .isOccupied(slot.isOccupied())
                .location(slot.getLocation())
                .build();
    }
}