package marketing.digital.rj.apiagendfy.scheduling.service;

import marketing.digital.rj.apiagendfy.Collaborator.model.CollaboratorModel;
import marketing.digital.rj.apiagendfy.Collaborator.repository.CollaboratorRepository;
import marketing.digital.rj.apiagendfy.infra.exception.ApiErrorCode;
import marketing.digital.rj.apiagendfy.infra.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class RotationService {

    private final CollaboratorRepository collaboratorRepository;

    public RotationService(CollaboratorRepository collaboratorRepository) {
        this.collaboratorRepository = collaboratorRepository;
    }

    /** Escolhe colaborador rotativo para um slot */
    public CollaboratorModel pickForSlot(UUID enterpriseId, ZonedDateTime slotStart, int stepMinutes) {
        List<CollaboratorModel> list =
                collaboratorRepository.findByEnterprise_IdAndActiveTrueOrderByNameAsc(enterpriseId);

        if (list.isEmpty()) {
            throw new BusinessException(
                    ApiErrorCode.BUSINESS_RULE,
                    "Nenhum colaborador ativo para esta empresa.",
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }

        int minutesOfYear = slotStart.getDayOfYear() * 24 * 60 + slotStart.getHour() * 60 + slotStart.getMinute();
        int idx = Math.floorMod(minutesOfYear / stepMinutes, list.size());
        return list.get(idx);
    }
}