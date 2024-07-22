package springframework.spring6restmvc.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import springframework.spring6restmvc.bootstrap.BootStrapData;
import springframework.spring6restmvc.entities.Beer;
import springframework.spring6restmvc.services.BeerCsvServiceImpl;
import springframework.springrestmvcapi.model.BeerStyle;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import({BootStrapData.class, BeerCsvServiceImpl.class})//again because of test splice characteristic,
// I have to import this to not have a zero result in test because of not loaded components
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void getBeerListByName() {
        Page<Beer> page = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%", null);//wildcard to find anything including IPA string

        assertThat(page.getContent().size()).isEqualTo(336);
    }

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