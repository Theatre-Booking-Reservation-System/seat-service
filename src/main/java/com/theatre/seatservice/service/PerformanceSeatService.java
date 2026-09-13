package com.theatre.seatservice.service;

import com.theatre.seatservice.model.PerformanceSeatItem;
import com.theatre.seatservice.model.PerformanceSeatListResponse;
import com.theatre.seatservice.repository.PerformanceSeatRepository;
import com.theatre.seatservice.repository.model.PerformanceSeat;
import com.theatre.seatservice.repository.model.Seat;
import com.theatre.seatservice.repository.model.SeatZone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PerformanceSeatService {

    private final PerformanceSeatRepository performanceSeatRepository;

    public PerformanceSeatListResponse getSeatsByPerformanceId(UUID performanceId) {
        List<PerformanceSeatItem> seats = performanceSeatRepository.findByPerformanceId(performanceId)
                .stream()
                .map(this::toPerformanceSeatItem)
                .toList();

        return PerformanceSeatListResponse.builder()
                .performanceId(performanceId)
                .seats(seats)
                .build();
    }

    private PerformanceSeatItem toPerformanceSeatItem(PerformanceSeat performanceSeat) {
        Seat seat = performanceSeat.getSeat();
        SeatZone zone = seat.getZone();

        return PerformanceSeatItem.builder()
                .perfSeatId(performanceSeat.getPerfSeatId())
                .seatId(seat.getSeatId())
                .zoneId(zone != null ? zone.getZoneId() : null)
                .section(seat.getSection())
                .zoneName(zone != null ? zone.getZoneName() : null)
                .rowLabel(seat.getRowLabel())
                .seatNumber(seat.getSeatNumber())
                .wheelchairSpace(seat.getWheelchairSpace())
                .status(performanceSeat.getStatus())
                .heldUntil(performanceSeat.getHeldUntil())
                .build();
    }
}
