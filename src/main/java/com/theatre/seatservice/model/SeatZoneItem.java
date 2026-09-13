package com.theatre.seatservice.model;

import com.theatre.seatservice.util.ZoneSection;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class SeatZoneItem {
    private UUID zoneId;
    private ZoneSection section;
    private String zoneName;
    private BigDecimal matineePct;
    private BigDecimal eveningPct;
}
