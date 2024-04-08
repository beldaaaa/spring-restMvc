package springframework.spring6restmvc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import springframework.spring6restmvc.entities.Beer;
import springframework.spring6restmvc.model.BeerStyle;

import java.util.List;
import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {
    //JpaRepository is useful for findAll,saveAll, saveAndFlush,...

    List<Beer> findAllByBeerNameIsLikeIgnoreCase(String beerName);

    List<Beer> findAllByBeerStyle(BeerStyle beerStyle);

    List<Beer> findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(String beerName, BeerStyle beerStyle);

   // List<Beer> findAllByBeerNameIsLikeIgnoreCaseAndBeerStyleAndShowInventory(String beerName, BeerStyle beerStyle, Boolean showInventory);
}
