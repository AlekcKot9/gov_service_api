package gov_service_api.repository.cache;

import gov_service_api.dto.user.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class InvoiceGetDTOCache {

    private static final int MAX_SIZE = 1;

    private final Map<Long, List<InvoiceDTO>> cache = new LinkedHashMap<Long,
            List<InvoiceDTO>>(MAX_SIZE, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Long, List<InvoiceDTO>> eldest) {
            return size() > MAX_SIZE;
        }
    };


    public List<InvoiceDTO> getFromCache(Long id) {
        return cache.get(id);
    }

    public void putInCache(Long id, List<InvoiceDTO> invoiceDTOs) {
        cache.put(id, invoiceDTOs);
    }

    public boolean isPresent(Long id) {
        return cache.containsKey(id);
    }

    public void clearCache(Long id) {
        cache.remove(id);
    }
}
