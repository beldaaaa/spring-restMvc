package springframework.spring6restmvc.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class BeerControllerTest {
    @Autowired
    BeerController beerController;

    @Test
    void getBearById() {
        System.out.println(beerController.getBearById(UUID.randomUUID()));
    }
}