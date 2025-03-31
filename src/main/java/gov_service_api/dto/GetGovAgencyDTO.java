package gov_service_api.dto;

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
}
