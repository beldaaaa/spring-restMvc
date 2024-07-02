package springframework.spring6restmvc.models;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Builder
@Data
public class BeerOrderCreateDTO {

    @NotNull
    private UUID customerId;
    private String customerRef;

    private Set<BeerOrderLineCreateDTO> beerOrderLinesCreateDTO;
}
