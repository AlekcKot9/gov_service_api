package gov_service_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "gov_agencies")
public class GovAgency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String address;

    @ManyToMany
    @JoinTable(
            name = "gov_agency_facility",
            joinColumns = @JoinColumn(name = "gov_agency_id"),
            inverseJoinColumns = @JoinColumn(name = "facility_id")
    )
    private List<Facility> facilities = new ArrayList<>();

    public GovAgency(String name, String type, String address) {
        this.name = name;
        this.type = type;
        this.address = address;
    }

    public GovAgency() {}
}
