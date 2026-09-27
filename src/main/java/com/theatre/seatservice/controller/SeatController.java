package com.theatre.seatservice.controller;

import com.theatre.seatservice.model.SeatCountResponse;
import com.theatre.seatservice.service.SeatService;
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
@RequestMapping("/seats")
@RequiredArgsConstructor
@Tag(name = "Seats", description = "Reference seat data for the theatre")
public class SeatController {

    private final SeatService seatService;

    @Operation(summary = "Count all reference seats",
            description = "Returns the total number of seats in the theatre. Used to compute "
                    + "per-performance availability.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Seat count returned"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT")
    })
    @GetMapping("/count")
    public ResponseEntity<SeatCountResponse> getSeatCount() {
        return ResponseEntity.ok(seatService.getSeatCount());
    }
}
