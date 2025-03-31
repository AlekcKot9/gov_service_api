package gov_service_api.dto;

import gov_service_api.model.*;
import lombok.*;

@Getter
@Setter
public class FacilityDTO {

    private Long id;
    private String name;
    private Double price;

    public FacilityDTO(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public FacilityDTO(Facility facility) {
        this.id = facility.getId();
        this.name = facility.getName();
        this.price = facility.getPrice();
    }

    public FacilityDTO() {}
}
