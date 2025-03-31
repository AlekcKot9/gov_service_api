package gov_service_api.repository;

import gov_service_api.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);

    User findByPersonalId(String personalId);

    boolean existsByPersonalIdAndPassword(String personalId, String password);

    boolean existsByPersonalId(String personalId);
}
