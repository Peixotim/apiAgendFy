package marketing.digital.rj.apiagendfy.scheduling.dto;

import marketing.digital.rj.apiagendfy.scheduling.model.Appointment;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AppointmentDTO(
        Long id,
        UUID enterpriseId,
        OffsetDateTime startAt,
        OffsetDateTime endAt,
        String status,
        String customerName,
        String customerEmail,
        String customerPhone
) {
    public static AppointmentDTO from(Appointment a) {
        return new AppointmentDTO(
                a.getId(),
                a.getEnterprise().getId(),
                a.getStartAt(),
                a.getEndAt(),
                a.getStatus() != null ? a.getStatus().name() : null,
                a.getCustomerName(),
                a.getCustomerEmail(),
                a.getCustomerPhone()
        );
    }
}