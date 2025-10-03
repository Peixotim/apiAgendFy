package marketing.digital.rj.apiagendfy.scheduling.service;

import jakarta.transaction.Transactional;
import marketing.digital.rj.apiagendfy.Enterprise.model.enterpriseModel;
import marketing.digital.rj.apiagendfy.Enterprise.repository.enterpriseRepository;
import marketing.digital.rj.apiagendfy.infra.exception.ApiErrorCode;
import marketing.digital.rj.apiagendfy.infra.exception.BusinessException;
import marketing.digital.rj.apiagendfy.scheduling.dto.AppointmentDTO;
import marketing.digital.rj.apiagendfy.scheduling.model.Appointment;
import marketing.digital.rj.apiagendfy.scheduling.repository.AppointmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.UUID;

@Service
public class AppointmentService {

    private final AppointmentRepository apptRepo;
    private final enterpriseRepository enterpriseRepo;
    private final RotationService rotationService;
    public AppointmentService(AppointmentRepository apptRepo,
                              enterpriseRepository enterpriseRepo,
                              RotationService service) {
        this.apptRepo = apptRepo;
        this.enterpriseRepo = enterpriseRepo;
        this.rotationService = service;
    }

    // AppointmentService.java (trecho)


    @Transactional
    public AppointmentDTO create(AppointmentDTO dto) {
        var enterprise = enterpriseRepo.getReferenceById(UUID.fromString(String.valueOf(dto.enterpriseId())));
        var start = dto.startAt();
        var end   = dto.endAt();

        if (apptRepo.existsByEnterprise_IdAndEndAtGreaterThanAndStartAtLessThan(
                enterprise.getId(), start, end)) {
            throw new BusinessException(ApiErrorCode.BUSINESS_RULE, "Horário indisponível.", HttpStatus.CONFLICT);
        }

        var slotStartZ = start.atZoneSameInstant(ZoneId.of("America/Sao_Paulo"));
        var collaborator = rotationService.pickForSlot(enterprise.getId(), slotStartZ, 30);

        var appt = new Appointment();
        appt.setEnterprise(enterprise);
        appt.setCollaborator(collaborator);
        appt.setStartAt(start);
        appt.setEndAt(end);
        appt.setStatus(Appointment.Status.BOOKED);
        appt.setCustomerName(dto.customerName());
        appt.setCustomerEmail(dto.customerEmail());
        appt.setCustomerPhone(dto.customerPhone());

        var saved = apptRepo.save(appt);
        return AppointmentDTO.from(saved);
    }


    @Transactional
    public AppointmentDTO cancel(Long id) {
        Appointment a = apptRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado."));
        a.setStatus(Appointment.Status.CANCELLED);
        return AppointmentDTO.from(apptRepo.save(a));
    }
}