package springframework.spring6restmvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import springframework.spring6restmvc.configs.SpringSecurityConfig;
import springframework.spring6restmvc.services.BeerService;
import springframework.spring6restmvc.services.BeerServiceImpl;
import springframework.springrestmvcapi.BeerDTO;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//Spring MockMVC allows to test the controller interactions in a servlet context
//without the application running in an application server.
@WebMvcTest(BeerController.class)//I want to limit this to BeerController class
@Import(SpringSecurityConfig.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    BeerService beerService;
    @Autowired
    ObjectMapper objectMapper;
    BeerServiceImpl beerServiceImpl;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;// to have a reusable component, which means I no longer need a
    //declaration in delete test method

    @Captor
    ArgumentCaptor<BeerDTO> beerArgumentCaptor;

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    public static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwtRequestPostProcessor =
            jwt().jwt(jwt -> jwt.claims(claims -> {
                        claims.put("scope", "message-read");
                        claims.put("scope", "message-write");
                    })
                    .subject("messaging-client")
                    .notBefore(Instant.now().minusSeconds(5L)));

    @Test
    void updateBlankBeerName() throws Exception {

        BeerDTO beerDTO = beerServiceImpl.beerPage(null, null, null, null, null)
                .getContent().getFirst();
        beerDTO.setBeerName("");

        given(beerService.updateBeerById(any(), any()))
                .willReturn(Optional.of(beerDTO));

        mockMvc.perform(put(BeerController.BEER_PATH_ID, beerDTO.getId())
                        .with(jwtRequestPostProcessor)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void createNullBeerName() throws Exception {

        BeerDTO beerDTO = BeerDTO.builder().build();

        given(beerService.saveNewBeer(any(BeerDTO.class)))
                .willReturn(beerServiceImpl.beerPage(null, null, null, null, null)
                        .getContent().getFirst());

        MvcResult mvcResult = mockMvc.perform(post(BeerController.BEER_PATH)
                        .with(jwtRequestPostProcessor)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(6)))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void patchBeer() throws Exception {
        //to give my request a body (JSON of the beer map object)
        BeerDTO beer = beerServiceImpl.beerPage(null, null, null, null, null)
                .getContent().getFirst();
        //a little change: I don't need to give it a fully qualified object,
        //so I can just create a map for Jackson (put key becomes the JSON property)
        Map<String, Object> beerMap = new HashMap<>();
        //now I'll get a simple JSON object being passed in with just beerName(which is mimicking what clients would do)
        //they are not going to send out a fully qualified object (only properties they want to change)
        beerMap.put("beerName", "New Name");

        mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId())
                        .with(jwtRequestPostProcessor)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isNoContent());

        verify(beerService).patchById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(beerMap.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());
    }

    @Test
    void deleteBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.beerPage(null, null, null, null, null)
                .getContent().getFirst();

        given(beerService.deleteById(any())).willReturn(true);

        mockMvc.perform(delete(BeerController.BEER_PATH_ID, beer.getId())
                        .with(jwtRequestPostProcessor)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isNoContent());
        verify(beerService).deleteById(uuidArgumentCaptor.capture());//sit on that mock and listen for anything what is passed in
        //and ensure to pass proper UUID value
        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());//now with the value I can run assertions on it

        //so basically it means I am making sure that the UUID that was passed to the controller through the path variable
        // got parsed properly and sent to that
    }

    @Test
    void updateBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.beerPage(null, null, null, null, null)
                .getContent().getFirst();

        given(beerService.updateBeerById(any(), any()))
                .willReturn(Optional.of(beer));

        mockMvc.perform(put(BeerController.BEER_PATH_ID, beer.getId())
                        .with(jwtRequestPostProcessor)
                        .accept(MediaType.APPLICATION_JSON)//sets the Accept header of the HTTP request to application/json,
                        // indicating that the client (the test in this case) expects JSON in the response
                        .contentType(MediaType.APPLICATION_JSON)//sets the Content-Type header of the HTTP request to
                        // application/json, indicating that the body of the request is in JSON format
                        .content(objectMapper.writeValueAsString(beer)))//object to convert POJO to JSON representation
                //(converts the beer object to a JSON string and includes it in the body of the HTTP request)
                .andExpect(status().isNoContent());//assertion
        verify(beerService).updateBeerById(any(UUID.class), any(BeerDTO.class));
    }

    @Test
    void getBeerList() throws Exception {
        given(beerService.beerPage(null, null, null, null, null))
                .willReturn(beerServiceImpl.beerPage(null, null, null, null, null));

        mockMvc.perform(get(BeerController.BEER_PATH)
                        .with(jwtRequestPostProcessor)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()", is(3)));

    }

    @Test
    void getBeerByIdNotFound() throws Exception {

        given((beerService.getBeerById(any(UUID.class))))
                .willReturn(Optional.empty());

        mockMvc.perform(get(BeerController.BEER_PATH_ID, UUID.randomUUID())
                        .with(jwtRequestPostProcessor))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBearById() throws Exception {
        BeerDTO testBeer = beerServiceImpl.beerPage(null, null, null, null, null)
                .getContent().getFirst();

        given(beerService.getBeerById(testBeer.getId()))
                .willReturn(Optional.of(testBeer));//configuring Mockito to go ahead
        // and return that testBeer object.
        // So I get the testBeer object from the service implementation and then tell Mockito to return it
        mockMvc.perform(get(BeerController.BEER_PATH_ID, testBeer.getId())//I want ot perform get against URL
                        .with(jwtRequestPostProcessor)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }

    @Test
    void createNewBeer() throws Exception {
        BeerDTO beer = beerServiceImpl.beerPage(null, null, false, 1, 25)
                .getContent().getFirst();
        beer.setVersion(null);
        beer.setId(null);

        given(beerService.saveNewBeer(any(BeerDTO.class)))
                .willReturn(beerServiceImpl.beerPage(null, null, false, 1, 25)
                        .getContent().get(1));

        mockMvc.perform(post(BeerController.BEER_PATH)
                        .with(jwtRequestPostProcessor)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }
}