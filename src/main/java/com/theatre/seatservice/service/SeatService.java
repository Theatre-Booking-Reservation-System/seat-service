package com.theatre.seatservice.service;

import com.theatre.seatservice.model.SeatCountResponse;
import com.theatre.seatservice.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatCountResponse getSeatCount() {
        return SeatCountResponse.builder()
                .total(seatRepository.count())
                .build();
    }
}
