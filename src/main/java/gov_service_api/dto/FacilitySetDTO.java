package gov_service_api.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class FacilitySetDTO {

    @NotBlank(message = "user must have personal id")
    private String name;

    @NotBlank(message = "user must have personal id")
    private Double prise;

    public FacilitySetDTO(String name, Double prise) {
        this.name = name;
        this.prise = prise;
    }

    public FacilitySetDTO() {}
}
