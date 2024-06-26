package springframework.spring6restmvc.models;


import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Builder
@Data
public class BeerOrderLineDTO {

    private UUID id;
    private Long version;
    private Timestamp createdDate;
    private Timestamp lastModifiedDate;
    private BeerDTO beerDTO;
    private Integer orderQuantity;
    private Integer quantityAllocated;

}
