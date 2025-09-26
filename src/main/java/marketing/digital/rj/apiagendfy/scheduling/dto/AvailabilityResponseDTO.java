package marketing.digital.rj.apiagendfy.scheduling.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record AvailabilityResponseDTO(
        java.util.UUID enterpriseId,
        String date,          // "2025-09-23"
        String timezone,      // "America/Sao_Paulo"
        List<Slot> slots
) {
    public record Slot(OffsetDateTime start, OffsetDateTime end, int capacity, int booked) {
        public boolean available() { return booked < capacity; }
    }
}