package springframework.spring6restmvc.bootstrap;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import springframework.spring6restmvc.entities.Beer;
import springframework.spring6restmvc.entities.Customer;
import springframework.spring6restmvc.model.BeerStyle;
import springframework.spring6restmvc.repositories.BeerRepository;
import springframework.spring6restmvc.repositories.CustomerRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class BootStrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        loadBeerData();
        loadCustomerData();
    }

    private void loadBeerData() {
        if (beerRepository.count() == 0) {
            Beer beer1 = Beer.builder()
                    .beerName("12")
                    .beerStyle(BeerStyle.PILSNER)
                    .upc("456")
                    .price(new BigDecimal("25.90"))
                    .quantityOnHand(40)
                    .createdData(LocalDateTime.now())
                    .updateData(LocalDateTime.now())
                    .build();
            Beer beer2 = Beer.builder()
                    .beerName("10")
                    .beerStyle(BeerStyle.STOUT)
                    .upc("444")
                    .price(new BigDecimal("12.90"))
                    .quantityOnHand(120)
                    .createdData(LocalDateTime.now())
                    .updateData(LocalDateTime.now())
                    .build();
            Beer beer3 = Beer.builder()
                    .beerName("1000")
                    .beerStyle(BeerStyle.IPA)
                    .upc("99")
                    .price(new BigDecimal("50.90"))
                    .quantityOnHand(12)
                    .createdData(LocalDateTime.now())
                    .updateData(LocalDateTime.now())
                    .build();

            beerRepository.saveAll(Arrays.asList(beer1, beer2, beer3));
        }
    }

    private void loadCustomerData() {
        if (customerRepository.count() == 0) {
            Customer customer1 = Customer.builder()
                    .customerName("Franta")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            Customer customer2 = Customer.builder()
                    .customerName("Ferenc")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            Customer customer3 = Customer.builder()
                    .customerName("Fanda")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            customerRepository.saveAll(Arrays.asList(customer1, customer2, customer3));
        }
    }
}
