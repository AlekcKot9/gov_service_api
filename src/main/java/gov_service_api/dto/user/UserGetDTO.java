package gov_service_api.dto.user;

import gov_service_api.model.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserGetDTO {

    private String personalId;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String address;

    private Double balance = 0.0;

    public UserGetDTO(User user) {
        this.personalId = user.getPersonalId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.balance = user.getBalance();
    }
}
