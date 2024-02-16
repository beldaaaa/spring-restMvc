package springframework.spring6restmvc.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import springframework.spring6restmvc.entities.Beer;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest//SB Test splice again(it brings up a minimal config for testing the SDJPA components)
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testBeer() {
        Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName("šnyt")
                .build());

        assertThat(savedBeer.getId()).isNotNull();
    }
}