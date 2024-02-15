package springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import springframework.spring6restmvc.model.Beer;
import springframework.spring6restmvc.services.BeerService;
import springframework.spring6restmvc.services.BeerServiceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//Why use Spring MockMVC?
//Spring MockMVC allows you to test the controller interactions in a servlet context
//without the application running in an application server.
@WebMvcTest(BeerController.class)//I want to limit this to BeerController class
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;//Now I want to bring in a dependency of autowired on MockMvc
    @MockBean//tells Mockito to provide mock of this into the Spring context
    //without this mock bean I would get an exception saying that I don't have that dependency there,
    //and I would have to provide it manually
    BeerService beerService;
    @Autowired
    ObjectMapper objectMapper;
    BeerServiceImpl beerServiceImpl;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;// to have a reusable component, which means I no longer need
    //declaration in delete test method

    @Captor
    ArgumentCaptor<Beer> beerArgumentCaptor;

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    void patchBeer() throws Exception {
        Beer beer = beerServiceImpl.beerList().getFirst();//to give my request a body (JSON of the beer map object)
        //a little change: I don't need to give it a fully qualified object,
        //so I can just create a map for Jackson (put key becomes the JSON property)
        Map<String, Object> beerMap = new HashMap<>();
        //now I'll get a simple JSON object being passed in with just beerName(which is mimicking what clients would do)
        //they are not going to send out a fully qualified object (only properties they want to change)
        beerMap.put("beerName", "New Name");

        mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isNoContent());

        verify(beerService).patchById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(beerMap.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());
    }

    @Test
    void deleteBeer() throws Exception {//delete operation is probably the simplest REST operation
        Beer beer = beerServiceImpl.beerList().getFirst();

        mockMvc.perform(delete(BeerController.BEER_PATH_ID, beer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)//doesn't work without
                        .content(objectMapper.writeValueAsString(beer)))//those 2 lines (but it should?)
                .andExpect(status().isNoContent());
//commented after ArgumentCaptor was defined above;
        //ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);//argument captor
        //created as Mockito class
        //Using argument captor can compare the beer's id is equal to the one that is passed as a parameter to the URL.
        verify(beerService).deleteById(uuidArgumentCaptor.capture());//sit on that mock and listen for anything what is passed in
        //and ensure to pass proper UUID value
        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());//now with the value I can run assertions on it

        //so basically it means I am making sure that the UUID that was passed to the controller through the path variable
        // got parsed properly and sent to that
    }

    @Test
    void updateBeer() throws Exception {
        Beer beer = beerServiceImpl.beerList().getFirst();//retrieves the first Beer object from the list of beers
        // returned by the beerList() method of beerServiceImpl

        mockMvc.perform(put(BeerController.BEER_PATH_ID, beer.getId())
                        .accept(MediaType.APPLICATION_JSON)//sets the Accept header of the HTTP request to application/json,
                        // indicating that the client (the test in this case) expects JSON in the response
                        .contentType(MediaType.APPLICATION_JSON)//sets the Content-Type header of the HTTP request to
                        // application/json, indicating that the body of the request is in JSON format
                        .content(objectMapper.writeValueAsString(beer)))//object to convert POJO to JSON representation
                //(converts the beer object to a JSON string and includes it in the body of the HTTP request)
                .andExpect(status().isNoContent());//assertion
        verify(beerService).updateBeerById(any(UUID.class), any(Beer.class));//argument captor is used here (more later)
    }

    @Test
    void getBeerList() throws Exception {
        given(beerService.beerList()).willReturn(beerServiceImpl.beerList());

        mockMvc.perform(get(BeerController.BEER_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));

    }

    @Test
    void getBeerByIdNotFound() throws Exception {

        given((beerService.getBeerById(any(UUID.class)))).willThrow(NotFoundException.class);

        mockMvc.perform(get(BeerController.BEER_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBearById() throws Exception {
        Beer testBeer = beerServiceImpl.beerList().getFirst();

        given(beerService.getBeerById(testBeer.getId())).willReturn(testBeer);//configuring Mockito to go ahead
        // and return that testBeer object.
        // So I get the testBeer object from the service implementation and then tell Mockito to return it
        mockMvc.perform(get(BeerController.BEER_PATH_ID, testBeer.getId())//I want ot perform get against URL
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())//and I should get back an OK status
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
        //this gets a beer object to return from my Mock MVC controller
    }

    @Test
    void createNewBeer() throws Exception {
        Beer beer = beerServiceImpl.beerList().getFirst();
        beer.setVersion(null);
        beer.setId(null);

        given(beerService.saveNewBeer(any(Beer.class))).willReturn(beerServiceImpl.beerList().get(1));

        mockMvc.perform(post(BeerController.BEER_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isCreated());
//                .andExpect(header().exists("Location"));//doesn't work
    }

}