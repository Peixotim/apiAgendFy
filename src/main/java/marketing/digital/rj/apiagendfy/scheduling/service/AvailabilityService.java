// AvailabilityService.java
package marketing.digital.rj.apiagendfy.scheduling.service;

import marketing.digital.rj.apiagendfy.scheduling.dto.AvailabilityResponseDTO;
import marketing.digital.rj.apiagendfy.scheduling.dto.SlotDTO;
import marketing.digital.rj.apiagendfy.scheduling.repository.AppointmentRepository;
import marketing.digital.rj.apiagendfy.Collaborator.model.CollaboratorModel;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AvailabilityService {

    private final AppointmentRepository appointmentRepository;
    private final RotationService rotationService;

    public AvailabilityService(
            AppointmentRepository appointmentRepository,
            RotationService rotationService
    ) {
        this.appointmentRepository = appointmentRepository;
        this.rotationService = rotationService;
    }

    public AvailabilityResponseDTO getDay(UUID enterpriseId, LocalDate date, String tz) {
        ZoneId zone = ZoneId.of((tz == null || tz.isBlank()) ? "America/Sao_Paulo" : tz);

        // Janelas padrão (manhã + tarde)
        List<Window> windows = buildWindows(date.getDayOfWeek());
        if (windows.isEmpty()) {
            return AvailabilityResponseDTO.empty(enterpriseId, date, zone);
        }

        final int stepMinutes = 30; // ⏱️ mesmo step usado na rotação
        final int capacity = 1;

        List<SlotDTO> out = new ArrayList<>();

        for (Window w : windows) {
            ZonedDateTime zStart = ZonedDateTime.of(date, w.start(), zone);
            ZonedDateTime zEnd   = ZonedDateTime.of(date, w.end(),   zone);

            for (ZonedDateTime cur = zStart; cur.isBefore(zEnd); cur = cur.plusMinutes(stepMinutes)) {
                ZonedDateTime curEnd = cur.plusMinutes(stepMinutes);

                // Contagem de colisões (OffsetDateTime)
                long booked = appointmentRepository.countOverlapping(
                        enterpriseId,
                        cur.toOffsetDateTime(),
                        curEnd.toOffsetDateTime()
                );

                // 🎯 colaborador da vez (mesma regra usada no POST /appointments)
                CollaboratorModel collab = rotationService.pickForSlot(enterpriseId, cur, stepMinutes);

                out.add(new SlotDTO(
                        cur.toOffsetDateTime().toString(),     // ex: 2025-10-10T09:00:00-03:00
                        curEnd.toOffsetDateTime().toString(),
                        capacity,
                        (int) booked,
                        collab.getId().toString(),
                        collab.getName(),
                        collab.getPhotoUrl()
                ));
            }
        }

        return new AvailabilityResponseDTO(
                enterpriseId.toString(),
                date.toString(),
                zone.getId(),
                out
        );
    }

    private List<Window> buildWindows(DayOfWeek dow) {
        List<Window> windows = new ArrayList<>();
        if (dow == DayOfWeek.SUNDAY) {
            // fechado
        } else if (dow == DayOfWeek.SATURDAY) {
            windows.add(new Window(LocalTime.of(9, 0), LocalTime.of(13, 0)));
        } else {
            windows.add(new Window(LocalTime.of(9, 0),  LocalTime.of(12, 0)));
            windows.add(new Window(LocalTime.of(14, 0), LocalTime.of(18, 0)));
        }
        return windows;
    }

    private record Window(LocalTime start, LocalTime end) {}
}