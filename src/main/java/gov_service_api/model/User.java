package gov_service_api.model;

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
    private String fullName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false) // Сделаем поле nullable, если оно не передается в запросе
    private Double balance = 0.0;

    @OneToMany(mappedBy = "user")
    private List<Invoice> invoices = new ArrayList<>();

    public User(String personalId, String fullName, String phoneNumber,
                String address, String password) {
        this.personalId = personalId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.password = password;
    }

    public User() {}
}
