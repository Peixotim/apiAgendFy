package marketing.digital.rj.apiagendfy.Enterprise.repository;

import marketing.digital.rj.apiagendfy.Enterprise.model.enterpriseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface enterpriseRepository extends JpaRepository<enterpriseModel, UUID> {
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByNameIgnoreCase(String name);
    boolean existsBycnpjIgnoreCase(String cnpj);
}
