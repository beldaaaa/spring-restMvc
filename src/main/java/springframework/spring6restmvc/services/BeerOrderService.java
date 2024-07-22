package springframework.spring6restmvc.services;

import org.springframework.data.domain.Page;
import springframework.spring6restmvc.entities.BeerOrder;
import springframework.springrestmvcapi.model.BeerOrderCreateDTO;
import springframework.springrestmvcapi.model.BeerOrderDTO;
import springframework.springrestmvcapi.model.BeerOrderUpdateDTO;

import java.util.Optional;
import java.util.UUID;

public interface BeerOrderService {

    Optional<BeerOrderDTO> findById(UUID beerOrderId);

    Page<BeerOrderDTO> listOrders(Integer pageNumber, Integer pageSize);

    BeerOrder createBeerOrder(BeerOrderCreateDTO beerOrderCreateDTO);

    BeerOrderDTO updateOrder(UUID beerOrderId, BeerOrderUpdateDTO beerOrderUpdateDTO);

    void deleteOrder(UUID beerOrderId);

}
