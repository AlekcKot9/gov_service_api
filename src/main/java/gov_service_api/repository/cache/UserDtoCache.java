package gov_service_api.repository.cache;

import gov_service_api.dto.user.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDtoCache {

    private final Map<String, UserGetDTO> cache = new HashMap<>();

    public UserGetDTO getFromCache(String id) {
        return cache.get(id);
    }

    public void putInCache(String id, UserGetDTO userGetDTO) {
        cache.put(id, userGetDTO);
    }

    public boolean isPresent(String id) {
        return cache.containsKey(id);
    }

    public void clearCache() {
        cache.clear();
    }
}

