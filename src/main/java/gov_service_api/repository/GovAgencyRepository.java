package gov_service_api.repository;

import gov_service_api.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface GovAgencyRepository extends JpaRepository<GovAgency, Long> {

    Optional<GovAgency> findById(Long id);

    Optional<GovAgency> findByName(String name);

    boolean existsByName(String name);
}
