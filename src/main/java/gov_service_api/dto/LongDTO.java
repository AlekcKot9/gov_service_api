package gov_service_api.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LongDTO {

    @NotBlank(message = "user must have personal id")
    private Long id;
}
