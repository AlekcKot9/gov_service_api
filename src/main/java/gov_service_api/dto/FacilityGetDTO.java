package gov_service_api.dto;

import gov_service_api.dto.user.*;
import gov_service_api.model.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
public class FacilityGetDTO {

    private Long id;
    private String name;
    private Double price;
    private List<InvoiceDTO> invoicesDTO;

    public FacilityGetDTO(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public FacilityGetDTO(Facility facility) {
        this.id = facility.getId();
        this.name = facility.getName();
        this.price = facility.getPrice();
        this.invoicesDTO = new ArrayList<>();
    }

    public FacilityGetDTO() {}
}
