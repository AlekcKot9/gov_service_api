package gov_service_api.dto;

import gov_service_api.model.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetGovAgencyByFacIdDTO {

    String name;

    String type;

    String address;

    public GetGovAgencyByFacIdDTO(GovAgency govAgency) {
        this.name = govAgency.getName();
        this.type = govAgency.getType();
        this.address = govAgency.getAddress();
    }
}
