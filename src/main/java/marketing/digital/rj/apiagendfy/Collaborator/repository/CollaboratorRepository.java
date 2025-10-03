// marketing.digital.rj.apiagendfy.Collaborator.repository.CollaboratorRepository
package marketing.digital.rj.apiagendfy.Collaborator.repository;

import marketing.digital.rj.apiagendfy.Collaborator.model.CollaboratorModel;
import marketing.digital.rj.apiagendfy.Enterprise.model.enterpriseModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CollaboratorRepository extends JpaRepository<CollaboratorModel, UUID> {
    List<CollaboratorModel> findByEnterpriseIdAndActiveTrueOrderByNameAsc(enterpriseModel enterprise);

    List<CollaboratorModel> findByEnterprise_IdAndActiveTrueOrderByNameAsc(UUID enterpriseId);
}