package springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import springframework.spring6restmvc.entities.Beer;
import springframework.spring6restmvc.mappers.BeerMapper;
import springframework.spring6restmvc.model.BeerDTO;
import springframework.spring6restmvc.model.BeerStyle;
import springframework.spring6restmvc.repositories.BeerRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        //this is going to inject Spring WAC into that and then I return build
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        //it is setting up the MockMVC environment with the Spring Data repositories injected into the server
        //=> it is full SB test
    }
    //in previous tests I have been working directly with controller object, but JPA violation are going to bubble up differently,
    //so this is focused on JPA layer
    //instead of using MockMVC I want to test constraint violation coming up from the DB in JPA layer
    //I need to use MockMVC slightly differently (hold of the WebApplicationContext) and init it in setup method

    @Test
    void listBeersByNameAndStyleShowInventoryTruePage2() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "true")
                        .queryParam("pageNumber", "2")
                        .queryParam("pageSize", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(50)))
                .andExpect(jsonPath("$.[0].quantityOnHand").value(IsNull.notNullValue()));
    }

    @Test
    void listBeersByNameStyleShowInventoryFalse() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "FALSE")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(310)))
                .andExpect(jsonPath("$.[0].quantityOnHand").value(IsNull.notNullValue()));
    }

    @Test
    void listBeersByNameAndStyle() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(310)));
    }

    @Test
    void listBeersByStyle() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerStyle", BeerStyle.IPA.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(548)));
    }

    @Test
    void listBeersByName() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName", "IPA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(336)));
    }

    @Test
    void patchBadBeerName() throws Exception {
        Beer beer = beerRepository.findAll().getFirst();//Get an existing beer from the DB
        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "NewName6546851246512468552616716462168271687216572165415642165416545621657216576542176256825878651467154157617621515646524213");
        beerMap.put("price", new BigDecimal("100"));
        MvcResult mvcResult = mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isBadRequest())
                //Because I am failing validation on JPA layer that is causing the transaction to roll back and bubble up
                //and right now my controller is not handling that property
                //=> error handler to handle this transaction system exception bubbling up from constraint/validation violation
                //because DB max name length = 50 only
                //=>add in another exception handler in CustomErrorController
                .andExpect(jsonPath("$.length()", is(4)))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

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
        List<BeerDTO> beerDTOList = beerController.beerList(null, null, false, 1, 22);


        assertThat(beerDTOList.size()).isEqualTo(2413);
    }

    @Transactional
    @Test
    void emptyList() {
        beerRepository.deleteAll();//because here I am going through the controller layer, SB is actually not going to
        //to do a rollback, usually it would automatically do, but not with controller layer
        //test splices would make it only for this test, so it would not influence any other test
        // => solution is to use @Transaction
        List<BeerDTO> beerDTOList = beerController.beerList(null, null, false, 1, 22);

        assertThat(beerDTOList.size()).isEqualTo(0);
    }
}