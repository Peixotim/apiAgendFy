package marketing.digital.rj.apiagendfy.scheduling.repository;

import marketing.digital.rj.apiagendfy.Enterprise.model.enterpriseModel;
import marketing.digital.rj.apiagendfy.scheduling.model.AvailabilityException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AvailabilityExceptionRepository extends JpaRepository<AvailabilityException,Long> {
    List<AvailabilityException> findByEnterpriseIdAndDate(enterpriseModel enterpriseId, LocalDate date);

    List<AvailabilityException> findByEnterprise_IdAndDate(UUID enterpriseId, LocalDate date);

}
