package com.theatre.seatservice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class SeatZoneListResponse extends CommonResponse {
    private List<SeatZoneItem> seatZones;
}
