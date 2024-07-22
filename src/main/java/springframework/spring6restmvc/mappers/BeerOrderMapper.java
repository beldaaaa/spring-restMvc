package springframework.spring6restmvc.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import springframework.spring6restmvc.entities.BeerOrder;
import springframework.spring6restmvc.entities.BeerOrderLine;
import springframework.spring6restmvc.entities.BeerOrderShipment;
import springframework.springrestmvcapi.model.BeerOrderDTO;
import springframework.springrestmvcapi.model.BeerOrderLineDTO;
import springframework.springrestmvcapi.model.BeerOrderShipmentDTO;

@Mapper
public interface BeerOrderMapper {

    @Mapping(target = "beerOrder", ignore = true)
    BeerOrderShipment beerOrderShipmentDtoToBeerOrderShipment(BeerOrderShipmentDTO beerOrderShipmentDTO);

    BeerOrderShipmentDTO beerOrderShipmentToBeerOrderShipmentDto(BeerOrderShipment beerOrderShipment);

    @Mapping(target = "beerOrder", ignore = true)
    BeerOrderLine beerOrderLineDtoToBeerOrderLine(BeerOrderLineDTO beerOrderLineDTO);

    BeerOrderLineDTO beerOrderLideToBeerOrderLineDto(BeerOrderLine beerOrderLine);

    BeerOrder beerOrderDtoToBeerOrder(BeerOrderDTO beerOrderDTO);

    BeerOrderDTO beerOrderToBeerOrderDto(BeerOrder beerOrder);
}
