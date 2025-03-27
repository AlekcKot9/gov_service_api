package gov_service_api.dto;

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

    public FacilityDTO() {}
}
