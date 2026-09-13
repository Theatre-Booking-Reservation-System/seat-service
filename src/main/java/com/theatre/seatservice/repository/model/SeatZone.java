package com.theatre.seatservice.repository.model;

import com.theatre.seatservice.util.ZoneSection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "seat_zone", uniqueConstraints = {
        @UniqueConstraint(name = "uk_seat_zone_section_name", columnNames = {"section", "zone_name"})
})
@Getter
@Setter
@NoArgsConstructor
public class SeatZone {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "zone_id", updatable = false, nullable = false)
    private UUID zoneId;

    @Enumerated(EnumType.STRING)
    @Column(name = "section", nullable = false, length = 15)
    private ZoneSection section;

    @Column(name = "zone_name", nullable = false, length = 100)
    private String zoneName;

    @Column(name = "matinee_pct", nullable = false, precision = 6, scale = 2)
    private BigDecimal matineePct;

    @Column(name = "evening_pct", nullable = false, precision = 6, scale = 2)
    private BigDecimal eveningPct;

    @Column(name = "added_by")
    private String addedBy;

    @Column(name = "added_date")
    private LocalDateTime addedDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;
}
