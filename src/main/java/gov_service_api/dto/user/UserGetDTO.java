package gov_service_api.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserGetDTO {

    private String personalId;

    private String fullName;

    private String phoneNumber;

    private String address;

    private Double balance = 0.0;
}
