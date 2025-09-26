package marketing.digital.rj.apiagendfy.Collaborator.repository;

import marketing.digital.rj.apiagendfy.Collaborator.model.CollaboratorModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CollaboratorRepository extends JpaRepository<CollaboratorModel,UUID> {
}
