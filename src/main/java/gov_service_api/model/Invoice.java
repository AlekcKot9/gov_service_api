package gov_service_api.model;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.*;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Double remainder;

    @Column(nullable = false)
    private String status;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "facility_id", nullable = true)
    private Facility facility;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    public Invoice(Double amount, String status) {
        this.amount = amount;
        this.status = status;
        this.remainder = amount;
        this.createdAt = LocalDateTime.now();
    }

    public Invoice() {}
}
