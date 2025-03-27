package gov_service_api.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddPaymentDTO {

    Long invoiceId;

    Double amount;
}
