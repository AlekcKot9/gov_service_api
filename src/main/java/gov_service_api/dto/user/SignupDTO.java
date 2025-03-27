package gov_service_api.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupDTO {

    private String personalId;

    private String fullName;

    private String phoneNumber;

    private String address;

    private String password;
}
