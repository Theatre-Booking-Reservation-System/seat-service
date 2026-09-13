package com.theatre.seatservice.repository;

import com.theatre.seatservice.repository.model.PerformanceSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PerformanceSeatRepository extends JpaRepository<PerformanceSeat, UUID> {
    List<PerformanceSeat> findByPerformanceId(UUID performanceId);
}
