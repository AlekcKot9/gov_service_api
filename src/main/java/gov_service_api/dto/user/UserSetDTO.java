package gov_service_api.dto.user;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSetDTO {

    @NotBlank(message = "first name should not be empty")
    private String firstName;

    @NotBlank(message = "second name should not be empty")
    private String lastName;

    @NotBlank(message = "phone number should not be empty")
    private String phoneNumber;

    @NotBlank(message = "address should not be empty")
    private String address;

    @NotBlank(message = "password should not be empty")
    private String password;
}
