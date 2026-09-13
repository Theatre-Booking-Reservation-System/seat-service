package com.theatre.seatservice.model;

import com.theatre.seatservice.util.SeatStatus;
import com.theatre.seatservice.util.ZoneSection;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PerformanceSeatItem {
    private UUID perfSeatId;
    private UUID seatId;
    private UUID zoneId;
    private ZoneSection section;
    private String zoneName;
    private String rowLabel;
    private Short seatNumber;
    private Boolean wheelchairSpace;
    private SeatStatus status;
    private LocalDateTime heldUntil;
}
