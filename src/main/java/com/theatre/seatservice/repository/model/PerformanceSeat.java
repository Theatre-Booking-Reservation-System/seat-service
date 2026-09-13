package com.theatre.seatservice.repository.model;

import com.theatre.seatservice.util.SeatStatus;
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
@Table(name = "performance_seat", uniqueConstraints = {
        @UniqueConstraint(name = "uk_performance_seat", columnNames = {"performance_id", "seat_id"})
})
@Getter
@Setter
@NoArgsConstructor
public class PerformanceSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "perf_seat_id", updatable = false, nullable = false)
    private UUID perfSeatId;

    // Soft reference to catalogue_db performance (no DB-level FK across services)
    @Column(name = "performance_id", nullable = false)
    private UUID performanceId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private SeatStatus status = SeatStatus.AVAILABLE;

    @Column(name = "held_by_session", length = 128)
    private String heldBySession;

    @Column(name = "held_until")
    private LocalDateTime heldUntil;

    @Column(name = "added_by")
    private String addedBy;

    @Column(name = "added_date")
    private LocalDateTime addedDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;
}
