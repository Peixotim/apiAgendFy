// marketing.digital.rj.apiagendfy.scheduling.repository.AppointmentRepository
package marketing.digital.rj.apiagendfy.scheduling.repository;

import marketing.digital.rj.apiagendfy.scheduling.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("""
        select count(a)
        from Appointment a
        where a.enterprise.id = :enterpriseId
          and a.status <> marketing.digital.rj.apiagendfy.scheduling.model.Appointment.Status.CANCELLED
          and a.startAt < :end
          and a.endAt   > :start
    """)
    long countOverlapping(@Param("enterpriseId") UUID enterpriseId,
                          @Param("start") OffsetDateTime start,
                          @Param("end") OffsetDateTime end);

    boolean existsByEnterprise_IdAndEndAtGreaterThanAndStartAtLessThan(
            UUID enterpriseId, OffsetDateTime startAt, OffsetDateTime endAt);

    List<Appointment> findByEnterprise_IdAndStartAtBetween(
            UUID enterpriseId, OffsetDateTime start, OffsetDateTime end);
}