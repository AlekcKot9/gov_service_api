package gov_service_api.dto.user;

import lombok.*;

@Getter
@Setter
public class LoginDTO {

    private String personalId;

    private String password;

    public LoginDTO(String personalId, String password) {
        this.personalId = personalId;
        this.password = password;
    }

    public LoginDTO() {}
}
