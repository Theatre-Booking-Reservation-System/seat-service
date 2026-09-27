package com.theatre.seatservice.model;

import com.theatre.seatservice.util.SeatStatus;
import com.theatre.seatservice.util.ZoneSection;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PerformanceSeatItem {
    private UUID seatId;
    private UUID zoneId;
    private ZoneSection section;
    private String zoneName;
    private String rowLabel;
    private Short seatNumber;
    private Boolean wheelchairSpace;
    // Derived by joining the reference seat with the performance's bookings:
    // BOOKED when a confirmed/pending booking holds the seat, otherwise AVAILABLE.
    private SeatStatus status;
}
