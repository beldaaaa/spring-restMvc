package springframework.spring6restmvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import springframework.spring6restmvc.entities.Beer;
import springframework.spring6restmvc.entities.BeerOrder;
import springframework.spring6restmvc.entities.Customer;
import springframework.spring6restmvc.mappers.BeerOrderMapper;
import springframework.spring6restmvc.repositories.BeerOrderRepository;
import springframework.spring6restmvc.repositories.BeerRepository;
import springframework.spring6restmvc.repositories.CustomerRepository;
import springframework.springrestmvcapi.model.*;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springframework.spring6restmvc.controllers.BeerControllerTest.jwtRequestPostProcessor;

@SpringBootTest
class BeerOrderControllerIT {

    @Autowired
    WebApplicationContext wac;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    BeerOrderMapper beerOrderMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    void listBeerOrders() throws Exception {
        mockMvc.perform(get(BeerOrderController.BEER_ORDER_PATH)
                        .with(jwtRequestPostProcessor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", greaterThan(0)));
    }

    @Test
    void getBeerOrderById() throws Exception {
        BeerOrder beerOrder = beerOrderRepository.findAll().getFirst();

        mockMvc.perform(get(BeerOrderController.BEER_ORDER_PATH_ID, beerOrder.getId())
                        .with(jwtRequestPostProcessor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(beerOrder.getId().toString())));
    }

    @Test
    void createBeerOrder() throws Exception {
        Customer customer = customerRepository.findAll().getFirst();
        Beer beer = beerRepository.findAll().getFirst();
        BeerOrderCreateDTO beerOrder = BeerOrderCreateDTO.builder()
                .customerId(customer.getId())
                .beerOrderLinesCreateDTO(Set.of(BeerOrderLineCreateDTO.builder()
                        .beerId(beer.getId())
                        .orderQuantity(9000)
                        .build())
                ).build();

        mockMvc.perform(post(BeerOrderController.BEER_ORDER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerOrder))
                        .with(jwtRequestPostProcessor))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Transactional
    @Test
    void updateBeerOrder() throws Exception {
        BeerOrder beerOrder = beerOrderRepository.findAll().getFirst();
        Set<BeerOrderLineUpdateDTO> beerOrderLines = new HashSet<>();
        beerOrder.getBeerOrderLines().forEach(beerOrderLine ->
                beerOrderLines.add(BeerOrderLineUpdateDTO.builder()
                        .lineId(beerOrderLine.getId())
                        .beerId(beerOrderLine.getBeer().getId())
                        .orderQuantity(beerOrderLine.getOrderQuantity())
                        .quantityAllocated(beerOrderLine.getQuantityAllocated())
                        .build()));
        BeerOrderUpdateDTO beerOrderUpdateDTO = BeerOrderUpdateDTO.builder()
                .customerId(beerOrder.getCustomer().getId())
                .customerRef("randomReferenceToSomethingReallyImportant")
                .shipmentUpdateDTO(BeerOrderShipmentUpdateDTO.builder()
                        .trackingNumber("661")
                        .build())
                .beerOrderLinesUpdateDTO(beerOrderLines)
                .build();

        mockMvc.perform(put(BeerOrderController.BEER_ORDER_PATH_ID, beerOrder.getId())
                        .with(jwtRequestPostProcessor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerOrderUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerRef", is("randomReferenceToSomethingReallyImportant")));
    }

    @Test
    void deleteOrder() throws Exception {
        BeerOrder beerOrder = beerOrderRepository.findAll().getFirst();

        mockMvc.perform(delete(BeerOrderController.BEER_ORDER_PATH_ID, beerOrder.getId())
                        .with(jwtRequestPostProcessor))
                .andExpect(status().isNoContent());

        assertTrue(beerOrderRepository.findById(beerOrder.getId()).isEmpty());

        mockMvc.perform(delete(BeerOrderController.BEER_ORDER_PATH_ID, beerOrder.getId())
                        .with(jwtRequestPostProcessor))
                .andExpect(status().isNotFound());
    }
}