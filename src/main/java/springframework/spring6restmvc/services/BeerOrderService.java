package springframework.spring6restmvc.services;

import org.springframework.data.domain.Page;
import springframework.spring6restmvc.entities.BeerOrder;
import springframework.spring6restmvc.models.BeerOrderCreateDTO;
import springframework.spring6restmvc.models.BeerOrderDTO;
import springframework.spring6restmvc.models.BeerOrderUpdateDTO;

import java.util.Optional;
import java.util.UUID;

public interface BeerOrderService {

    Optional<BeerOrderDTO> findById(UUID beerOrderId);

    Page<BeerOrderDTO> listOrders(Integer pageNumber, Integer pageSize);

    BeerOrder createBeerOrder(BeerOrderCreateDTO beerOrderCreateDTO);

    BeerOrderDTO updateOrder(UUID beerOrderId, BeerOrderUpdateDTO beerOrderUpdateDTO);

    void deleteOrder(UUID beerOrderId);

}
