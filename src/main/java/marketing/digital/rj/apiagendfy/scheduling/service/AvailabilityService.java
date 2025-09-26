package marketing.digital.rj.apiagendfy.scheduling.service;

import marketing.digital.rj.apiagendfy.scheduling.dto.AvailabilityResponseDTO;
import marketing.digital.rj.apiagendfy.scheduling.model.AvailabilityException;
import marketing.digital.rj.apiagendfy.scheduling.model.AvailabilityRule;
import marketing.digital.rj.apiagendfy.scheduling.model.Appointment;
import marketing.digital.rj.apiagendfy.scheduling.repository.AvailabilityExceptionRepository;
import marketing.digital.rj.apiagendfy.scheduling.repository.AvailabilityRuleRepository;
import marketing.digital.rj.apiagendfy.scheduling.repository.AppointmentRepository;
import org.springframework.stereotype.Service;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AvailabilityService {

    private final AvailabilityRuleRepository ruleRepo;
    private final AvailabilityExceptionRepository exRepo;
    private final AppointmentRepository apptRepo;

    public AvailabilityService(AvailabilityRuleRepository ruleRepo,
                               AvailabilityExceptionRepository exRepo,
                               AppointmentRepository apptRepo) {
        this.ruleRepo = ruleRepo;
        this.exRepo = exRepo;
        this.apptRepo = apptRepo;
    }

    public AvailabilityResponseDTO getDay(UUID enterpriseId, LocalDate date, String tzOpt) {
        // 1) Fuso padrão
        String tz = (tzOpt == null || tzOpt.isBlank()) ? "America/Sao_Paulo" : tzOpt;
        ZoneId zone = ZoneId.of(tz);

        // 2) Dia da semana (0 = domingo)
        int weekday = date.getDayOfWeek().getValue() % 7;

        // 3) Carrega regras e exceções do dia
        List<AvailabilityRule> rules =
                ruleRepo.findByEnterprise_IdAndWeekday(enterpriseId , weekday);

        List<AvailabilityException> exs =
                exRepo.findByEnterprise_IdAndDate(enterpriseId, date);

        // Dia bloqueado por exceção de dia inteiro?
        boolean dayBlocked = exs.stream().anyMatch(x ->
                x.getType() == AvailabilityException.Type.BLOCK
                        && x.getStartTime() == null
                        && x.getEndTime() == null
        );
        if (dayBlocked || rules.isEmpty()) {
            return new AvailabilityResponseDTO(enterpriseId, date.toString(), tz, List.of());
        }

        // 4) Agendamentos existentes no dia
        OffsetDateTime startOfDay = date.atStartOfDay(zone).toOffsetDateTime();
        OffsetDateTime endOfDay   = date.plusDays(1).atStartOfDay(zone).toOffsetDateTime();

        List<Appointment> appts =
                apptRepo.findByEnterprise_IdAndStartAtBetween(enterpriseId, startOfDay, endOfDay);

        // 5) Gera slots a partir das regras
        List<AvailabilityResponseDTO.Slot> out = new ArrayList<>();

        for (AvailabilityRule r : rules) {
            LocalTime cur = r.getStartTime();

            while (!cur.plusMinutes(r.getIntervalMinutes()).isAfter(r.getEndTime())) {
                LocalTime next = cur.plusMinutes(r.getIntervalMinutes());

                // Bloqueio parcial? (há sobreposição com alguma exceção BLOCK)
                LocalTime finalCur = cur;
                boolean blocked = exs.stream().anyMatch(x ->
                        x.getType() == AvailabilityException.Type.BLOCK
                                && x.getStartTime() != null
                                && x.getEndTime() != null
                                // sobreposição quando !(next <= ex.start || cur >= ex.end)
                                && !( !next.isAfter(x.getStartTime()) || !finalCur.isBefore(x.getEndTime()) )
                );

                if (!blocked) {
                    // Converte para OffsetDateTime respeitando o fuso
                    OffsetDateTime s = OffsetDateTime.of(
                            date, cur, zone.getRules().getOffset(LocalDateTime.of(date, cur))
                    );
                    OffsetDateTime e = OffsetDateTime.of(
                            date, next, zone.getRules().getOffset(LocalDateTime.of(date, next))
                    );

                    // Conta agendamentos que se sobrepõem ao slot [s, e)
                    int booked = (int) appts.stream().filter(a ->
                            !(a.getEndAt().isEqual(s) || a.getEndAt().isBefore(s)   // termina antes/ao início
                                    || a.getStartAt().isEqual(e) || a.getStartAt().isAfter(e)) // começa no fim/depois
                    ).count();

                    out.add(new AvailabilityResponseDTO.Slot(s, e, r.getCapacity(), booked));
                }

                cur = next;
            }
        }

        return new AvailabilityResponseDTO(enterpriseId, date.toString(), tz, out);
    }
}