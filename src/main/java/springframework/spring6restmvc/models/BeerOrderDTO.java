package springframework.spring6restmvc.models;

import lombok.Builder;
import lombok.Data;
import springframework.spring6restmvc.entities.Customer;

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
    private Customer customer;

    private Set<BeerOrderLineDTO> beerOrderLinesDto;
    private BeerOrderShipmentDTO beerOrderShipmentDto;
}
