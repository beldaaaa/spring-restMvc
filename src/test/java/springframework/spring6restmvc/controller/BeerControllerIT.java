package springframework.spring6restmvc.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import springframework.spring6restmvc.entities.Beer;
import springframework.spring6restmvc.mappers.BeerMapper;
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

    //controller is dealing with DTOs and DB with entities
    //=> use mapper to help with some conversions
    @Autowired
    BeerMapper beerMapper;

    @Test
    void deleteByIdNotFound() {
        assertThrows(NotFoundException.class, () -> beerController.deleteById(UUID.randomUUID()));
    }

    @Rollback
    @Transactional
    @Test
    void deleteById() {
        Beer beer = beerRepository.findAll().getFirst();

        var responseEntity = beerController.deleteById(beer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(beerRepository.findById(beer.getId()).isEmpty());

    }

    @Test
    void updateBeerNotFound() {
        assertThrows(NotFoundException.class, () ->
                beerController.updateById(UUID.randomUUID(), BeerDTO.builder().build()));
    }

    @Rollback
    @Transactional
    @Test
    void updateBeerFound() {
        Beer beer = beerRepository.findAll().getFirst();
        BeerDTO beerDTO = beerMapper.beerToBeerDto(beer);
        beerDTO.setId(null);
        beerDTO.setVersion(null);
        final String beerName = "updatovanePivo";
        beerDTO.setBeerName(beerName);

        var responseEntity = beerController.updateById(beer.getId(), beerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Beer updatedBeer = beerRepository.findById(beer.getId()).get();
        assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);

    }

    @Rollback//to not interfere with any other test
    @Transactional
    @Test
    void saveNewBeer() {
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("Policka")
                .build();

        var responseEntity = beerController.handlePost(beerDTO);
        //it takes JSON object, parse it to a BeerDTO object and then SMVC calls controller method with populated DTO

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");//to get URI
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Beer beer = beerRepository.findById(savedUUID).get();//now fetch the beer from the repository
        assertThat(beer).isNotNull();
        //in this test created BeerDTO passes through the controller, got persisted to the DB, and then I go outside
        //the controller using the repository to enquire the DB again to verify that ID property returned by controller
        //is really in the DB
    }


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