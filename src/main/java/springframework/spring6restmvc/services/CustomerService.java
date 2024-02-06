package springframework.spring6restmvc.services;

import springframework.spring6restmvc.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    List<Customer> customerList();

    Customer getCustomerById(UUID id);

    Customer saveNewCustomer(Customer customer);
}
