package springframework.spring6restmvc.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Builder
@Data
public class BeerOrderDTO {

    private UUID id;
    private Long version;
    private Timestamp createdDate;
    private Timestamp lastModifiedDate;
    private String customerRef;
    @NotBlank
    @NotBlank
    private CustomerDTO customer;

    private Set<BeerOrderLineDTO> beerOrderLinesDTO;
    private BeerOrderShipmentDTO beerOrderShipmentDTO;
    private BigDecimal paymentAmount;
}
