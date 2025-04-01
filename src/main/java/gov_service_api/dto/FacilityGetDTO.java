package gov_service_api.dto;

import gov_service_api.model.*;
import lombok.*;

@Getter
@Setter
public class FacilityGetDTO {

    private Long id;
    private String name;
    private Double price;

    public FacilityGetDTO(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public FacilityGetDTO(Facility facility) {
        this.id = facility.getId();
        this.name = facility.getName();
        this.price = facility.getPrice();
    }

    public FacilityGetDTO() {}
}
