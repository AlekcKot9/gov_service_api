package gov_service_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "facilities")
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invoice> invoices = new ArrayList<>();

    @ManyToMany(mappedBy = "facilities")
    private List<GovAgency> govAgencies = new ArrayList<>();

    public Facility(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public Facility() {}
}
