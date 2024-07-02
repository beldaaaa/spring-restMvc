package springframework.spring6restmvc.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class BeerOrderLineCreateDTO {

    @NotNull
    private UUID beerId;
    @Min(value = 1, message = "Minimum quantity must be at least 1")
    private Integer orderQuantity;
}
