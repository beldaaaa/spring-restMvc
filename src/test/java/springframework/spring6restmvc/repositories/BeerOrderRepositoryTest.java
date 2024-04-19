package springframework.spring6restmvc.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springframework.spring6restmvc.entities.Beer;
import springframework.spring6restmvc.entities.BeerOrder;
import springframework.spring6restmvc.entities.BeerOrderShipment;
import springframework.spring6restmvc.entities.Customer;

//if I use @SpringBootTest instead of test splice(@DataJpaTest), customer count is 3 instead of 0 in my first temp test
//it's a full test, so it actually use my boostrap data
@SpringBootTest
class BeerOrderRepositoryTest {

    @Autowired
    BeerOrderRepository orderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerRepository beerRepository;

    Customer testCustomer;
    Beer testBeer;

    @BeforeEach
    void setUp() {
        testCustomer = customerRepository.findAll().getFirst();
        testBeer = beerRepository.findAll().getFirst();

    }

    @Transactional//if I am using repository methods, there is going to be an implicit transaction => annotation added
    //also because of Hibernate is doing lazy loading
    @Test
    void beerOrders() {
        BeerOrder beerOrder = BeerOrder.builder()
                .customerRef("test order")
                .customer(testCustomer)//establishes the relation to the beerOrder
                .beerOrderShipment(BeerOrderShipment.builder()
                        .trackingNumber("661")
                        .build())
                .build();
//after persistence operations I want to get the object being returned from the repository
        BeerOrder savedOrder = orderRepository.save(beerOrder);
        //added "...AndFlush" that tells Hibernate to persist this to the DB immediately and Hibernate will look int the DB
        //for the relationship of customer and beerOrders
        //=>in savedOrder-customer-beerOrders-0-id is reference to the beer order

        //on the other and saveAndFlush can cause a performance issue since it tells Hibernate to put everything in the DB
        System.out.printf(beerOrder.getCustomerRef());

    }

}