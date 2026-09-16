package com.theatre.seatservice.controller;

import com.theatre.seatservice.model.SeatZoneListResponse;
import com.theatre.seatservice.service.SeatZoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seat-zones")
@RequiredArgsConstructor
@Tag(name = "Seat Zones", description = "Catalogue of seat zones within the theatre")
public class SeatZoneController {

    private final SeatZoneService seatZoneService;

    @Operation(summary = "List all seat zones",
            description = "Returns every seat zone defined for the theatre.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Seat zones returned"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT")
    })
    @GetMapping
    public ResponseEntity<SeatZoneListResponse> getAllSeatZones() {
        return ResponseEntity.ok(seatZoneService.getAllSeatZones());
    }
}
