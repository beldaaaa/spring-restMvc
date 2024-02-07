package springframework.spring6restmvc.services;

import springframework.spring6restmvc.model.Beer;

import java.util.List;
import java.util.UUID;

public interface BeerService {
    List<Beer> beerList();

    Beer getBeerById(UUID id);

    Beer saveNewBeer(Beer beer);

    void updateBeerById(UUID beerId, Beer beer);

    void deleteById(UUID beerId);

    void patchById(UUID beerId, Beer beer);
}
