package gov_service_api.repository.cache;

import gov_service_api.dto.user.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class InvoiceGetDTOCache {

    private static final int MAX_SIZE = 1;

    private final Map<String, List<InvoiceDTO>> cache = new LinkedHashMap<String, List<InvoiceDTO>>(MAX_SIZE, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, List<InvoiceDTO>> eldest) {
            return size() > MAX_SIZE;
        }
    };


    public List<InvoiceDTO> getFromCache(String id) {
        return cache.get(id);
    }

    public void putInCache(String id, List<InvoiceDTO> invoiceDTOs) {
        cache.put(id, invoiceDTOs);
    }

    public boolean isPresent(String id) {
        return cache.containsKey(id);
    }

    public void clearCache(String id) {
        cache.remove(id);
    }
}
