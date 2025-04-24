package gov_service_api.dto.user;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupDTO {

    @NotBlank(message = "user must have personal id")
    private String personalId;

    @NotBlank(message = "user must have first name")
    private String firstName;

    @NotBlank(message = "user must have last name")
    private String lastName;

    @NotBlank(message = "user must have phone number")
    private String phoneNumber;

    @NotBlank(message = "user must have address")
    private String address;

    @NotBlank(message = "user have to enter password")
    private String password;
}
