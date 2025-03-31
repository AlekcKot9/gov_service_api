package gov_service_api.model;

import gov_service_api.dto.user.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String personalId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Double balance = 0.0;

    @OneToMany(mappedBy = "user")
    private List<Invoice> invoices = new ArrayList<>();

    public User(SignupDTO signupDTO) {
        this.personalId = signupDTO.getPersonalId();
        this.firstName = signupDTO.getFirstName();
        this.lastName = signupDTO.getLastName();
        this.phoneNumber = signupDTO.getPhoneNumber();
        this.address = signupDTO.getAddress();
        this.password = signupDTO.getPassword();
    }

    public User() {}
}
