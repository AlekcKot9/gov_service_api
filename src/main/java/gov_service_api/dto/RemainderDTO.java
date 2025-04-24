package gov_service_api.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RemainderDTO {

    @NotBlank(message = "user must have personal id")
    String remainder;
}
