package springframework.spring6restmvc.services;

import org.springframework.data.domain.Page;
import springframework.spring6restmvc.models.BeerDTO;
import springframework.spring6restmvc.models.BeerStyle;

import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    Page<BeerDTO> beerPage(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize);

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO saveNewBeer(BeerDTO beer);

    Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer);

    Boolean deleteById(UUID beerId);

    Optional<BeerDTO> patchById(UUID beerId, BeerDTO beer);
}
