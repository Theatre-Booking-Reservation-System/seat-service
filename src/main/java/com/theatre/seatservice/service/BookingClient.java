package com.theatre.seatservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingClient {

    private final RestClient restClient;

    public BookingClient(RestClient.Builder restClientBuilder,
                         @Value("${clients.booking.base-url:http://localhost:8084/booking-service}")
                         String bookingBaseUrl) {
        this.restClient = restClientBuilder.baseUrl(bookingBaseUrl).build();
    }

    public Set<UUID> getBookedSeatIds(UUID performanceId, String bearerToken) {
        try {
            BookedSeatsPayload payload = restClient.get()
                    .uri("/performances/{id}/bookings", performanceId)
                    .headers(headers -> {
                        if (bearerToken != null && !bearerToken.isBlank()) {
                            headers.set(HttpHeaders.AUTHORIZATION, bearerToken);
                        }
                    })
                    .retrieve()
                    .body(BookedSeatsPayload.class);

            if (payload == null || payload.bookedSeatIds() == null) {
                return Set.of();
            }
            return payload.bookedSeatIds().stream().collect(Collectors.toSet());
        } catch (Exception e) {
            return Set.of();
        }
    }

    // Minimal projection of booking-service's PerformanceBookedSeatsResponse.
    private record BookedSeatsPayload(UUID performanceId, List<UUID> bookedSeatIds, List<String> bookedSeatRefs) {
    }
}
