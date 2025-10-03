package marketing.digital.rj.apiagendfy.scheduling.repository;

import marketing.digital.rj.apiagendfy.scheduling.model.WorkingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface WorkingHoursRepository extends JpaRepository<WorkingHours, Long> {
    List<WorkingHours> findByEnterpriseIdAndDayOfWeek(UUID enterpriseId, int dayOfWeek);
}