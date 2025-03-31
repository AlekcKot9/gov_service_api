package gov_service_api.repository;

import gov_service_api.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findById(Long id);

    @Query(value = "SELECT * FROM invoices WHERE user_id = :userId AND status = :status", nativeQuery = true)
    List<Invoice> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);
}
