package springframework.spring6restmvc.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springframework.spring6restmvc.entities.Beer;
import springframework.spring6restmvc.model.BeerDTO;
import springframework.spring6restmvc.repositories.BeerRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest//this is complete test with full context
class BeerControllerIT {//IT = integration test
    @Autowired
    BeerController beerController;
    @Autowired
    BeerRepository beerRepository;


    @Test
    void beerIdNotFound() {
        assertThrows(NotFoundException.class, () -> {//this is going to be captured by SpringMVC framework to convert
            //that into a 404 not found error
            beerController.getBearById(UUID.randomUUID());
        });
    }

    @Test
    void getById() {
        Beer beer = beerRepository.findAll().getFirst();//every time this test runs, it creates a new object and UUID changes
        //so that's not persistent, ts its in-memory DB

        BeerDTO dto = beerController.getBearById(beer.getId());//this is normal operation

        assertThat(dto).isNotNull();
    }

    //now I want to test the controller and its interaction with JPA data layer

    // I am not looking for web context. I am looking at testing the interaction of the controller with underlying service
    @Test
    void listBeers() {
        List<BeerDTO> beerDTOList = beerController.beerList();

        assertThat(beerDTOList.size()).isEqualTo(3);
    }

    @Transactional
    @Test
    void emptyList() {
        beerRepository.deleteAll();//because here I am going through the controller layer, SB is actually not going to
        //to do a rollback, usually it would automatically do, but not with controller layer
        //test splices would make it only for this test, so it would not influence any other test
        // => solution is to use @Transaction
        List<BeerDTO> beerDTOList = beerController.beerList();

        assertThat(beerDTOList.size()).isEqualTo(0);
    }
}