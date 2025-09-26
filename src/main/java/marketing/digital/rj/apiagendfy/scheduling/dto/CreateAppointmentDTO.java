// marketing.digital.rj.apiagendfy.scheduling.dto.CreateAppointmentDTO
package marketing.digital.rj.apiagendfy.scheduling.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateAppointmentDTO(
        UUID enterpriseId,
        OffsetDateTime startAt,
        OffsetDateTime endAt,
        String customerName,
        String customerEmail,
        String customerPhone,
        Long serviceId,        // opcional
        Long collaboratorId    // opcional
) {}