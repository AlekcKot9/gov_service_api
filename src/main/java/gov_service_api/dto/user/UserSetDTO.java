package gov_service_api.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSetDTO {

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String address;

    private String password;
}
