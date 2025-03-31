package gov_service_api.repository;

import gov_service_api.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {

    Optional<Facility> findById(Long id);

    Optional<Facility> findByName(String name);
}
