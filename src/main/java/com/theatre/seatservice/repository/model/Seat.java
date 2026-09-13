package com.theatre.seatservice.repository.model;

import com.theatre.seatservice.util.ZoneSection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "seat", uniqueConstraints = {
        @UniqueConstraint(name = "uk_seat_section_row_number", columnNames = {"section", "row_label", "seat_number"})
})
@Getter
@Setter
@NoArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "seat_id", updatable = false, nullable = false)
    private UUID seatId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "zone_id", nullable = false)
    private SeatZone zone;

    @Enumerated(EnumType.STRING)
    @Column(name = "section", nullable = false, length = 15)
    private ZoneSection section;

    @Column(name = "row_label", nullable = false, length = 5)
    private String rowLabel;

    @Column(name = "seat_number", nullable = false)
    private Short seatNumber;

    @Column(name = "is_wheelchair_space", nullable = false)
    private Boolean wheelchairSpace = Boolean.FALSE;

    @Column(name = "added_by")
    private String addedBy;

    @Column(name = "added_date")
    private LocalDateTime addedDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;
}
