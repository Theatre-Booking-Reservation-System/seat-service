package com.theatre.seatservice.controller;

import com.theatre.seatservice.model.PerformanceSeatListResponse;
import com.theatre.seatservice.service.PerformanceSeatService;
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
public class PerformanceSeatController {

    private final PerformanceSeatService performanceSeatService;

    @GetMapping("/{id}/seats")
    public ResponseEntity<PerformanceSeatListResponse> getSeatsByPerformanceId(@PathVariable UUID id) {
        return ResponseEntity.ok(performanceSeatService.getSeatsByPerformanceId(id));
    }
}
