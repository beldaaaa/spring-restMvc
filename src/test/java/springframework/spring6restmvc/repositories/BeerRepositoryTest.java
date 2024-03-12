package springframework.spring6restmvc.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import springframework.spring6restmvc.entities.Beer;
import springframework.spring6restmvc.model.BeerStyle;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest//SB Test splice again(it brings up a minimal config for testing the SDJPA components)
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testBeer() {
        Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName("šnyt")
                .beerStyle(BeerStyle.GOSE)
                .upc("12")
                .price(BigDecimal.valueOf(44))
                .build());

        beerRepository.flush();//after adding some @NotNull,... annotations to Beer entity, it throws an error
        //because flush tells Hibernate to immediately write to the DB, but due to missing params in test it fails
        //=> add all required params to test
        //it is a test splice, but hibernate would do only some "lazy write" without using flush

        assertThat(savedBeer.getId()).isNotNull();
    }
}