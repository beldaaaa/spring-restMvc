package springframework.spring6restmvc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import springframework.spring6restmvc.entities.Beer;

import java.util.List;
import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {
    //JpaRepository is useful for findAll,saveAll, saveAndFlush,...

    List<Beer> findAllByBeerName(String beerName);
}
