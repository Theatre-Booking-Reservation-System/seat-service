package com.theatre.seatservice.service;

import com.theatre.seatservice.model.SeatZoneItem;
import com.theatre.seatservice.model.SeatZoneListResponse;
import com.theatre.seatservice.repository.SeatZoneRepository;
import com.theatre.seatservice.repository.model.SeatZone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeatZoneService {

    private final SeatZoneRepository seatZoneRepository;

    public SeatZoneListResponse getAllSeatZones() {
        List<SeatZoneItem> seatZones = seatZoneRepository.findAll()
                .stream()
                .map(this::toSeatZoneItem)
                .toList();

        return SeatZoneListResponse.builder()
                .seatZones(seatZones)
                .build();
    }

    private SeatZoneItem toSeatZoneItem(SeatZone zone) {
        return SeatZoneItem.builder()
                .zoneId(zone.getZoneId())
                .section(zone.getSection())
                .zoneName(zone.getZoneName())
                .matineePct(zone.getMatineePct())
                .eveningPct(zone.getEveningPct())
                .build();
    }
}
