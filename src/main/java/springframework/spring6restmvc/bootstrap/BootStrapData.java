package springframework.spring6restmvc.bootstrap;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import springframework.spring6restmvc.entities.Beer;
import springframework.spring6restmvc.entities.BeerOrder;
import springframework.spring6restmvc.entities.BeerOrderLine;
import springframework.spring6restmvc.entities.Customer;
import springframework.spring6restmvc.models.BeerCSVRecord;
import springframework.spring6restmvc.models.BeerStyle;
import springframework.spring6restmvc.repositories.BeerOrderRepository;
import springframework.spring6restmvc.repositories.BeerRepository;
import springframework.spring6restmvc.repositories.CustomerRepository;
import springframework.spring6restmvc.services.BeerCsvService;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class BootStrapData implements CommandLineRunner {

    private final BeerCsvService beerCsvService;
    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;
    private final BeerOrderRepository beerOrderRepository;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        loadBeerData();
        loadCsvData();
        loadCustomerData();
        loadBeerOrderData();
    }

    private void loadCsvData() throws FileNotFoundException {
        if (beerRepository.count() < 10) {
            File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");
            List<BeerCSVRecord> recordList = beerCsvService.convertCSV(file);

            recordList.forEach(beerCSVRecord -> {
                BeerStyle beerStyle = switch (beerCSVRecord.getStyle()) {
                    case "American Pale Lager" -> BeerStyle.LAGER;
                    case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale" ->
                            BeerStyle.ALE;
                    case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> BeerStyle.IPA;
                    case "American Porter" -> BeerStyle.PORTER;
                    case "Oatmeal Stout", "American Stout" -> BeerStyle.STOUT;
                    case "Saison / Farmhouse Ale" -> BeerStyle.SAISON;
                    case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> BeerStyle.WHEAT;
                    case "English Pale Ale" -> BeerStyle.PALE_ALE;
                    default -> BeerStyle.PILSNER;
                };

                beerRepository.save(Beer.builder()
                        .beerName(StringUtils.abbreviate(beerCSVRecord.getBeer(), 50))
                        .beerStyle(beerStyle)
                        .price(BigDecimal.TEN)
                        .upc(beerCSVRecord.getRow().toString())
                        .quantityOnHand(beerCSVRecord.getCount())
                        .build());
            });
        }
    }


    private void loadBeerData() {
        if (beerRepository.count() == 0) {
            Beer beer1 = Beer.builder()
                    .beerName("12")
                    .beerStyle(BeerStyle.PILSNER)
                    .upc("456")
                    .price(new BigDecimal("25.90"))
                    .quantityOnHand(40)
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();
            Beer beer2 = Beer.builder()
                    .beerName("10")
                    .beerStyle(BeerStyle.STOUT)
                    .upc("444")
                    .price(new BigDecimal("12.90"))
                    .quantityOnHand(120)
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();
            Beer beer3 = Beer.builder()
                    .beerName("1000")
                    .beerStyle(BeerStyle.IPA)
                    .upc("99")
                    .price(new BigDecimal("50.90"))
                    .quantityOnHand(12)
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
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

    private void loadBeerOrderData() {
        List<Beer> beers = beerRepository.findAll();
        List<Customer> customers = customerRepository.findAll();
        Iterator<Beer> beerIterator = beers.iterator();

        customers.forEach(customer -> {
            beerOrderRepository.save(BeerOrder.builder()
                    .customer(customer)
                    .beerOrderLines(Set.of(
                            BeerOrderLine.builder()
                                    .beer(beerIterator.next())
                                    .orderQuantity(44)
                                    .build(),
                            BeerOrderLine.builder()
                                    .beer(beerIterator.next())
                                    .orderQuantity(17)
                                    .build()
                    ))
                    .build());

            beerOrderRepository.save(BeerOrder.builder()
                    .customer(customer)
                    .beerOrderLines(Set.of(
                            BeerOrderLine.builder()
                                    .beer(beerIterator.next())
                                    .orderQuantity(44)
                                    .build(),
                            BeerOrderLine.builder()
                                    .beer(beerIterator.next())
                                    .orderQuantity(17)
                                    .build()
                    ))
                    .build());
        });
    }
}
