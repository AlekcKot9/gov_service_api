package gov_service_api.dto;

import gov_service_api.model.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetGovAgencyDTO {

    String name;

    String type;

    String address;

    List<FacilityDTO> facilityies = new ArrayList<>();

    public GetGovAgencyDTO(GovAgency govAgency) {
        this.name = govAgency.getName();
        this.type = govAgency.getType();
        this.address = govAgency.getAddress();
    }
}
