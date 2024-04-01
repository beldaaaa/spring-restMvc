package springframework.spring6restmvc.bootstrap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import springframework.spring6restmvc.repositories.BeerRepository;
import springframework.spring6restmvc.repositories.CustomerRepository;
import springframework.spring6restmvc.services.BeerCsvService;
import springframework.spring6restmvc.services.BeerCsvServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(BeerCsvServiceImpl.class)//its test splice test so Spring context does not do a full component scan
    //and limits it only to the data components in the repositories ->import it explicitly
class BootStrapDataTest {

    @Autowired
    BeerRepository beerRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    BeerCsvService beerCsvService;

    BootStrapData bootStrapData;

    @BeforeEach
    void setUp() {
        bootStrapData = new BootStrapData(beerRepository, customerRepository, beerCsvService);
    }

    @Test
    void run() throws Exception {
        bootStrapData.run(null);
        assertThat(beerRepository.count()).isEqualTo(2413);
        assertThat(customerRepository.count()).isEqualTo(3);
    }

}