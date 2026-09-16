package com.theatre.seatservice.controller;

import com.theatre.seatservice.model.PerformanceSeatListResponse;
import com.theatre.seatservice.service.PerformanceSeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/performances")
@RequiredArgsConstructor
@Tag(name = "Performance Seats", description = "Seat availability for a specific performance")
public class PerformanceSeatController {

    private final PerformanceSeatService performanceSeatService;

    @Operation(summary = "List seats for a performance",
            description = "Returns the seats and their current availability for the given performance.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Seats returned"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "404", description = "No performance exists for the given id")
    })
    @GetMapping("/{id}/seats")
    public ResponseEntity<PerformanceSeatListResponse> getSeatsByPerformanceId(
            @Parameter(description = "Unique identifier of the performance") @PathVariable UUID id) {
        return ResponseEntity.ok(performanceSeatService.getSeatsByPerformanceId(id));
    }
}
