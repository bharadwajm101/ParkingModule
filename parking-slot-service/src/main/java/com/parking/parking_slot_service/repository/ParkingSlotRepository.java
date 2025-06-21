package com.parking.parking_slot_service.repository;

import com.parking.parking_slot_service.entity.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {
    List<ParkingSlot> findByIsOccupied(boolean isOccupied);
    long countByIsOccupied(boolean isOccupied);
    List<ParkingSlot> findByTypeIgnoreCase(String type);
}