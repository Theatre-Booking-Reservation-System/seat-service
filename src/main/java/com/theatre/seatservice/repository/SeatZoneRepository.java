package com.theatre.seatservice.repository;

import com.theatre.seatservice.repository.model.SeatZone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SeatZoneRepository extends JpaRepository<SeatZone, UUID> {
}
