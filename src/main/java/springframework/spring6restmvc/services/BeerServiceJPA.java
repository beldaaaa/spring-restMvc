package springframework.spring6restmvc.services;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import springframework.spring6restmvc.mappers.BeerMapper;
import springframework.spring6restmvc.model.BeerDTO;
import springframework.spring6restmvc.repositories.BeerRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public List<BeerDTO> beerList() {
        return null;
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.empty();
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        return null;
    }

    @Override
    public void updateBeerById(UUID beerId, BeerDTO beer) {

    }

    @Override
    public void deleteById(UUID beerId) {

    }

    @Override
    public void patchById(UUID beerId, BeerDTO beer) {

    }
}
