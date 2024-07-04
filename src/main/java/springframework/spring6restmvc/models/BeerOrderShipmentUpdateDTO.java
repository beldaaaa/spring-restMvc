package springframework.spring6restmvc.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
//2 ctors added due to 400 error in updateBeerOrder
@AllArgsConstructor
@NoArgsConstructor
public class BeerOrderShipmentUpdateDTO {

    @NotBlank
    private String trackingNumber;

}
