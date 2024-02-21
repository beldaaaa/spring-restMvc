package springframework.spring6restmvc.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springframework.spring6restmvc.entities.Customer;
import springframework.spring6restmvc.model.CustomerDTO;
import springframework.spring6restmvc.repositories.CustomerRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CustomerControllerIT {

    @Autowired
    CustomerController customerController;
    @Autowired
    CustomerRepository customerRepository;


    @Test
    void customerIdNotFound() {
        assertThrows(NotFoundException.class, () -> customerController.getCustomerById(UUID.randomUUID()));
    }

    @Test
    void getById() {
        Customer customer = customerRepository.findAll().getFirst();

        CustomerDTO dto = customerController.getCustomerById(customer.getId());

        assertThat(dto).isNotNull();
    }

    @Test
    void listCustomers() {
        List<CustomerDTO> customerDTOList = customerController.customerList();

        assertThat(customerDTOList.size()).isEqualTo(3);
    }

    @Transactional
    @Test
    void emptyList() {
        customerRepository.deleteAll();
        List<CustomerDTO> customerDTOList = customerController.customerList();

        assertThat(customerDTOList.size()).isEqualTo(0);
    }


}