package marketing.digital.rj.apiagendfy.scheduling.repository;

import marketing.digital.rj.apiagendfy.Enterprise.model.enterpriseModel;
import marketing.digital.rj.apiagendfy.scheduling.model.AvailabilityRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface AvailabilityRuleRepository extends JpaRepository<AvailabilityRule,Long> {
    List<AvailabilityRule> findByEnterpriseIdAndWeekday(enterpriseModel enterpriseId, Integer weekday);

    List<AvailabilityRule> findByEnterprise_IdAndWeekday(UUID enterpriseId, int weekday);
}