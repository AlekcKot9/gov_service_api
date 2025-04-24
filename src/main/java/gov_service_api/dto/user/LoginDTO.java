package gov_service_api.dto.user;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class LoginDTO {

    @NotBlank(message = "user have to enter personal id")
    private String personalId;

    @NotBlank(message = "user have to enter password")
    private String password;

    public LoginDTO(String personalId, String password) {
        this.personalId = personalId;
        this.password = password;
    }

    public LoginDTO() {}
}
