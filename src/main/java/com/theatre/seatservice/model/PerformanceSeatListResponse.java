package com.theatre.seatservice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class PerformanceSeatListResponse extends CommonResponse {
    private UUID performanceId;
    private List<PerformanceSeatItem> seats;
}
