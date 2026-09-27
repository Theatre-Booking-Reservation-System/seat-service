package com.theatre.seatservice.service;

import com.theatre.seatservice.model.PerformanceSeatItem;
import com.theatre.seatservice.model.PerformanceSeatListResponse;
import com.theatre.seatservice.repository.SeatRepository;
import com.theatre.seatservice.repository.model.Seat;
import com.theatre.seatservice.repository.model.SeatZone;
import com.theatre.seatservice.util.SeatStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PerformanceSeatService {

    private final SeatRepository seatRepository;
    private final BookingClient bookingClient;

    public PerformanceSeatListResponse getSeatsByPerformanceId(UUID performanceId, String bearerToken) {
        Set<UUID> bookedSeatIds = bookingClient.getBookedSeatIds(performanceId, bearerToken);

        List<PerformanceSeatItem> seats = seatRepository
                .findAllByOrderBySectionAscRowLabelAscSeatNumberAsc()
                .stream()
                .map(seat -> toPerformanceSeatItem(seat, bookedSeatIds))
                .toList();

        return PerformanceSeatListResponse.builder()
                .performanceId(performanceId)
                .seats(seats)
                .build();
    }

    private PerformanceSeatItem toPerformanceSeatItem(Seat seat, Set<UUID> bookedSeatIds) {
        SeatZone zone = seat.getZone();
        SeatStatus status = bookedSeatIds.contains(seat.getSeatId())
                ? SeatStatus.BOOKED
                : SeatStatus.AVAILABLE;

        return PerformanceSeatItem.builder()
                .seatId(seat.getSeatId())
                .zoneId(zone != null ? zone.getZoneId() : null)
                .section(seat.getSection())
                .zoneName(zone != null ? zone.getZoneName() : null)
                .rowLabel(seat.getRowLabel())
                .seatNumber(seat.getSeatNumber())
                .wheelchairSpace(seat.getWheelchairSpace())
                .status(status)
                .build();
    }
}
