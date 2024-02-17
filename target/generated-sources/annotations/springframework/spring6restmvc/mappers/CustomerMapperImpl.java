package springframework.spring6restmvc.mappers;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import springframework.spring6restmvc.entities.Customer;
import springframework.spring6restmvc.model.CustomerDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-17T12:05:47+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Azul Systems, Inc.)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public Customer customerDtoToCustomer(CustomerDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Customer.CustomerBuilder customer = Customer.builder();

        customer.id( dto.getId() );
        customer.customerName( dto.getCustomerName() );
        customer.version( dto.getVersion() );
        customer.createdDate( dto.getCreatedDate() );
        customer.lastModifiedDate( dto.getLastModifiedDate() );

        return customer.build();
    }

    @Override
    public CustomerDTO customerToCustomerDto(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        CustomerDTO.CustomerDTOBuilder customerDTO = CustomerDTO.builder();

        customerDTO.id( customer.getId() );
        customerDTO.customerName( customer.getCustomerName() );
        customerDTO.version( customer.getVersion() );
        customerDTO.createdDate( customer.getCreatedDate() );
        customerDTO.lastModifiedDate( customer.getLastModifiedDate() );

        return customerDTO.build();
    }
}
