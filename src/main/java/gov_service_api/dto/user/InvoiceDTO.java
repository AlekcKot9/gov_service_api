package gov_service_api.dto.user;

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

    public InvoiceDTO() {}
}
