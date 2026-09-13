package com.theatre.seatservice.controller;

import com.theatre.seatservice.model.SeatZoneListResponse;
import com.theatre.seatservice.service.SeatZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seat-zones")
@RequiredArgsConstructor
public class SeatZoneController {

    private final SeatZoneService seatZoneService;

    @GetMapping
    public ResponseEntity<SeatZoneListResponse> getAllSeatZones() {
        return ResponseEntity.ok(seatZoneService.getAllSeatZones());
    }
}
