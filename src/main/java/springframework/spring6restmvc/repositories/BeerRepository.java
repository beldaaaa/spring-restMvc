package springframework.spring6restmvc.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import springframework.spring6restmvc.entities.Beer;
import springframework.spring6restmvc.models.BeerStyle;

import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {
    //JpaRepository is useful for findAll,saveAll, saveAndFlush,...

    Page<Beer> findAllByBeerNameIsLikeIgnoreCase(String beerName, Pageable pageable);

    Page<Beer> findAllByBeerStyle(BeerStyle beerStyle, Pageable pageable);

    Page<Beer> findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(String beerName, BeerStyle beerStyle, Pageable pageable);

}
