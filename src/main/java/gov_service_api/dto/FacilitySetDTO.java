package gov_service_api.dto;

import lombok.*;

@Getter
@Setter
public class FacilitySetDTO {

    private String name;

    private Double prise;

    public FacilitySetDTO(String name, Double prise) {
        this.name = name;
        this.prise = prise;
    }
}
