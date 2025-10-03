package marketing.digital.rj.apiagendfy.scheduling.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;


public record AvailabilityResponseDTO(
        String enterpriseId,
        String date,
        String timezone,
        List<SlotDTO> slots
) {
    public static AvailabilityResponseDTO empty(java.util.UUID enterpriseId, LocalDate date, ZoneId zone) {
        return new AvailabilityResponseDTO(
                enterpriseId.toString(),
                date.toString(),
                zone.getId(),
                java.util.List.of()
        );
    }
}