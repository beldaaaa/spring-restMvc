package springframework.spring6restmvc.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import springframework.spring6restmvc.entities.Customer;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testCustomer() {
        Customer savedCustomer = customerRepository.save(Customer.builder()
                .customerName("Svarta")
                .build());

        assertThat(savedCustomer.getId()).isNotNull();
    }
}