package marketing.digital.rj.apiagendfy.scheduling.repository;

import marketing.digital.rj.apiagendfy.Enterprise.model.enterpriseModel;
import marketing.digital.rj.apiagendfy.scheduling.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    List<Appointment> findByEnterpriseIdAndStartAtBetween(enterpriseModel enterpriseId, OffsetDateTime startOfDay, OffsetDateTime endOfDay);

    boolean existsByEnterprise_IdAndEndAtGreaterThanAndStartAtLessThan(
            UUID enterpriseId, OffsetDateTime startAt, OffsetDateTime endAt);
    List<Appointment> findByEnterprise_IdAndStartAtBetween(UUID enterpriseId, OffsetDateTime start, OffsetDateTime end);
}
