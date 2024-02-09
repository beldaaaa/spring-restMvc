package springframework.spring6restmvc.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import springframework.spring6restmvc.model.Beer;
import springframework.spring6restmvc.services.BeerService;
import springframework.spring6restmvc.services.BeerServiceImpl;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BeerController.class)//I want to limit this to BeerController class
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;//Now I want to bring in a dependency of autowired on MockMvc
    @MockBean//tells Mockito to provide a mock of this into the Spring context
    //without this mock bean I would get an exception saying that I dont have that dependency there
    //and I would have to provide it manually
    BeerService beerService;
    BeerServiceImpl beerServiceImpl = new BeerServiceImpl();

    @Test
    void testBeerList() throws Exception {
        given(beerService.beerList()).willReturn(beerServiceImpl.beerList());

        mockMvc.perform(get("/api/v1/beer")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));

    }

    @Test
    void getBearById() throws Exception {
        Beer testBeer = beerServiceImpl.beerList().getFirst();

        given(beerService.getBeerById(testBeer.getId())).willReturn(testBeer);//configuring Mockito to go ahead
        // and return back that testBeer object. So I get the testBeer object from the service implementation and then
        //tell Mockito to return it
        mockMvc.perform(get("/api/v1/beer/" + testBeer.getId())//I want ot perform get against URL
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())//and I should get back an OK status
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
        //this gets a beer object to return back from my Mock MVC controller
    }
}