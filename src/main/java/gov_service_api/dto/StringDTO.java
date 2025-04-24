package gov_service_api.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StringDTO {

    @NotBlank(message = "String should not be empty")
    private String message;
}
