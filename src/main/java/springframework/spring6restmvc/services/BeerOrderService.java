package springframework.spring6restmvc.services;

import org.springframework.data.domain.Page;
import springframework.spring6restmvc.models.BeerOrderDTO;

import java.util.Optional;
import java.util.UUID;

public interface BeerOrderService {

    Optional<BeerOrderDTO> findById(UUID beerOrderId);

    Page<BeerOrderDTO> listOrders(Integer pageNumber, Integer pageSize);
}
