package marketing.digital.rj.apiagendfy.Users.repository;

import marketing.digital.rj.apiagendfy.Users.model.usersModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface usersRepository extends JpaRepository<usersModel, UUID> {
    Optional<usersModel> findByEmail(String email);
}
