package springframework.spring6restmvc.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import springframework.spring6restmvc.services.BeerService;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;//for some reason this static import has to be done manually
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;//same here


@WebMvcTest(BeerController.class)//I want to limit this to BeerController class
class BeerControllerTest {

@Autowired
MockMvc mockMvc;//Now I want to bring in a dependency of autowired on MockMvc
@MockBean//tells Mockito to provide a mock of this into the Spring context
        //without this mock bean I would get an exception saying that I dont have that dependency there
        //and I woud have to provide it manually
BeerService beerService;
    @Test
    void getBearById() throws Exception {

        mockMvc.perform(get("/api/v1/beer/"+ UUID.randomUUID())//I want ot perform get against URL
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());//and I should get back an OK status
    }
}