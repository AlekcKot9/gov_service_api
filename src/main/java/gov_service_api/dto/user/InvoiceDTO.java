package gov_service_api.dto.user;

import gov_service_api.model.*;
import lombok.*;

import java.time.*;

@Getter
@Setter
public class InvoiceDTO {

    private Long id;

    private Double amount;

    private Double remainder;

    private String status;

    private LocalDateTime createdAt;

    public InvoiceDTO(
            Long id, Double amount, Double remainder, String status, LocalDateTime createdAt) {
        this.id = id;
        this.amount = amount;
        this.remainder = remainder;
        this.status = status;
        this.createdAt = createdAt;
    }

    public InvoiceDTO(Invoice invoice) {
        this.id = invoice.getId();
        this.amount = invoice.getAmount();
        this.remainder = invoice.getRemainder();
        this.status = invoice.getStatus();
        this.createdAt = invoice.getCreatedAt();
    }

    public InvoiceDTO() {}
}
