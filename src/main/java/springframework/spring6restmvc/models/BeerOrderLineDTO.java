package springframework.spring6restmvc.models;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

    @Min(value = 1,message = "Minimum quantity must be at least 1")
    private Integer orderQuantity;
    @NotBlank
    private Integer quantityAllocated;

}
