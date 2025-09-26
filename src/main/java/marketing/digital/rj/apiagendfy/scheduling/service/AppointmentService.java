package marketing.digital.rj.apiagendfy.scheduling.service;

import jakarta.transaction.Transactional;
import marketing.digital.rj.apiagendfy.Enterprise.model.enterpriseModel;
import marketing.digital.rj.apiagendfy.Enterprise.repository.enterpriseRepository;
import marketing.digital.rj.apiagendfy.scheduling.dto.AppointmentDTO;
import marketing.digital.rj.apiagendfy.scheduling.model.Appointment;
import marketing.digital.rj.apiagendfy.scheduling.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

    private final AppointmentRepository apptRepo;
    private final enterpriseRepository enterpriseRepo;

    public AppointmentService(AppointmentRepository apptRepo,
                              enterpriseRepository enterpriseRepo) {
        this.apptRepo = apptRepo;
        this.enterpriseRepo = enterpriseRepo;
    }

    @Transactional
    public AppointmentDTO create(AppointmentDTO dto) {
        // validações básicas
        if (dto.startAt() == null || dto.endAt() == null || !dto.endAt().isAfter(dto.startAt())) {
            throw new IllegalArgumentException("Período inválido (endAt deve ser após startAt).");
        }

        // empresa existe?
        enterpriseModel enterprise = enterpriseRepo.findById(dto.enterpriseId())
                .orElseThrow(() -> new IllegalArgumentException("Enterprise não encontrada."));

        // conflito de horário?
        boolean hasOverlap = apptRepo.existsByEnterprise_IdAndEndAtGreaterThanAndStartAtLessThan(
                enterprise.getId(), dto.startAt(), dto.endAt()
        );
        if (hasOverlap) {
            throw new IllegalStateException("Horário indisponível: já existe agendamento no intervalo.");
        }

        // montar entidade
        Appointment a = new Appointment();
        a.setEnterprise(enterprise);
        a.setStartAt(dto.startAt());
        a.setEndAt(dto.endAt());
        a.setStatus(Appointment.Status.BOOKED); // default na criação
        a.setCustomerName(dto.customerName());
        a.setCustomerEmail(dto.customerEmail());
        a.setCustomerPhone(dto.customerPhone());
        // se tiver relationship com service/collaborator, setar aqui

        // salvar
        Appointment saved = apptRepo.save(a);
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