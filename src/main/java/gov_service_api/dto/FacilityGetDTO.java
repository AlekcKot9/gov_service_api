package gov_service_api.dto;

import gov_service_api.dto.user.*;
import gov_service_api.model.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
public class FacilityGetDTO {

    @NotNull(message = "facility have to have id")
    private Long id;

    @NotBlank(message = "facility have to have name")
    private String name;

    @NotNull(message = "price shouldn't be null")
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
