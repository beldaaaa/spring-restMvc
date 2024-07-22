package springframework.spring6restmvc.mappers;

import org.mapstruct.Mapper;
import springframework.spring6restmvc.entities.Customer;
import springframework.springrestmvcapi.model.CustomerDTO;

@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDTO dto);

    CustomerDTO customerToCustomerDto(Customer customer);
}
